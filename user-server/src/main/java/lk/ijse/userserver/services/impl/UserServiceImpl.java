package lk.ijse.userserver.services.impl;

import lk.ijse.userserver.dto.UserDTO;
import lk.ijse.userserver.entity.User;
import lk.ijse.userserver.repo.UserRepository;
import lk.ijse.userserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    // In-memory storage for booking history and logs
    private final Map<UUID, List<Map<String, Object>>> bookingHistory = new ConcurrentHashMap<>();
    private final Map<UUID, List<String>> userLogs = new ConcurrentHashMap<>();

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUid(user.getUid());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        // Exclude password from response
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    private User toEntity(UserDTO dto) {
        User user = new User();
        user.setUid(dto.getUid());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Hash password
        user.setRole(dto.getRole());
        user.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        return user;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return null; // Email already exists
        }
        User user = toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    @Override
    public UserDTO authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword())) {
            return toDTO(userOptional.get());
        }
        return null; // Authentication failed
    }

    @Override
    public UserDTO getUserProfile(UUID uid) {
        return userRepository.findById(uid).map(this::toDTO).orElse(null);
    }

    @Override
    public UserDTO updateUserProfile(UUID uid, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(uid);
        if (userOptional.isPresent() && userDTO.getEmail() != null && !userDTO.getEmail().equals(userOptional.get().getEmail())) {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                return null; // Email already in use
            }
        }
        return userOptional.map(user -> {
            user.setFullName(userDTO.getFullName() != null ? userDTO.getFullName() : user.getFullName());
            user.setEmail(userDTO.getEmail() != null ? userDTO.getEmail() : user.getEmail());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            user.setRole(userDTO.getRole() != null ? userDTO.getRole() : user.getRole());
            user.setCreatedAt(user.getCreatedAt()); // Keep original createdAt
            User updatedUser = userRepository.save(user);
            return toDTO(updatedUser);
        }).orElse(null);
    }

    @Override
    public List<UserDTO> getBookingHistory(UUID uid) {
        List<Map<String, Object>> history = bookingHistory.getOrDefault(uid, new ArrayList<>());
        return history.stream().map(entry -> {
            UserDTO dto = new UserDTO();
            dto.setUid(uid);
            dto.setFullName((String) entry.get("location"));
            dto.setCreatedAt((LocalDateTime) entry.get("timestamp"));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void addBookingLog(UUID uid, LocalDateTime timestamp, String action) {
        userLogs.computeIfAbsent(uid, k -> new ArrayList<>()).add(String.format("%s - %s", timestamp, action));
    }

    @Override
    public List<String> getUserLogs(UUID uid) {
        return userLogs.getOrDefault(uid, new ArrayList<>());
    }
}