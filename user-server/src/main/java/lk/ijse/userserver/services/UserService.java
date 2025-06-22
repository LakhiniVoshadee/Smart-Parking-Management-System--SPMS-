package lk.ijse.userserver.services;

import lk.ijse.userserver.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);

    UserDTO authenticateUser(String email, String password);

    UserDTO getUserProfile(UUID uid);

    UserDTO updateUserProfile(UUID uid, UserDTO userDTO);

    List<UserDTO> getBookingHistory(UUID uid); // In-memory for now

    void addBookingLog(UUID uid, LocalDateTime timestamp, String action);

    List<String> getUserLogs(UUID uid); // In-memory for now
}