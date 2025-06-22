package lk.ijse.parkingservice.services.impl;

import lk.ijse.parkingservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingservice.entity.ParkingSpace;
import lk.ijse.parkingservice.entity.User;
import lk.ijse.parkingservice.enums.UserRole;
import lk.ijse.parkingservice.repo.ParkingRepository;
import lk.ijse.parkingservice.repo.UserRepository;
import lk.ijse.parkingservice.services.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private UserRepository userRepository;

    // Convert entity to DTO
    private ParkingSpaceDTO toDTO(ParkingSpace parkingSpace) {
        ParkingSpaceDTO dto = new ParkingSpaceDTO();
        dto.setParkingId(parkingSpace.getParkingId());
        dto.setLocation(parkingSpace.getLocation());
        dto.setLocationCode(parkingSpace.getLocationCode());
        dto.setAvailable(parkingSpace.isAvailable());
        dto.setUserId(parkingSpace.getUserId());
        dto.setReservedAt(parkingSpace.getReservedAt());
        dto.setZone(parkingSpace.getZone());
        dto.setPricePerHour(parkingSpace.getPricePerHour());
        dto.setLastUpdated(parkingSpace.getLastUpdated());
        return dto;
    }

    // Convert DTO to entity
    private ParkingSpace toEntity(ParkingSpaceDTO dto) {
        ParkingSpace space = new ParkingSpace();
        space.setParkingId(dto.getParkingId());
        space.setLocation(dto.getLocation());
        space.setLocationCode(dto.getLocationCode());
        space.setAvailable(dto.isAvailable());
        space.setUserId(dto.getUserId());
        space.setReservedAt(dto.getReservedAt());
        space.setZone(dto.getZone());
        space.setPricePerHour(dto.getPricePerHour());
        space.setLastUpdated(dto.getLastUpdated());
        return space;
    }

    @Override
    public List<ParkingSpaceDTO> getAllParkingSpaces() {
        return parkingRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSpaceDTO> getAvailableParkingSpaces() {
        return parkingRepository.findByIsAvailableTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParkingSpaceDTO getParkingSpaceById(UUID parkingId) {
        return parkingRepository.findById(parkingId)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public ParkingSpaceDTO reserveParkingSpace(UUID parkingId, UUID userId) {
        Optional<ParkingSpace> optionalSpace = parkingRepository.findById(parkingId);
        return optionalSpace.map(space -> {
            if (space.isAvailable()) {
                space.setAvailable(false);
                space.setUserId(userId);
                space.setReservedAt(LocalDateTime.now());
                space.setLastUpdated(LocalDateTime.now());
                ParkingSpace savedSpace = parkingRepository.save(space);
                return toDTO(savedSpace);
            }
            return null;
        }).orElse(null);
    }

    @Override
    public ParkingSpaceDTO releaseParkingSpace(UUID parkingId) {
        Optional<ParkingSpace> optionalSpace = parkingRepository.findById(parkingId);
        return optionalSpace.map(space -> {
            if (!space.isAvailable()) {
                space.setAvailable(true);
                space.setUserId(null);
                space.setReservedAt(null);
                space.setLastUpdated(LocalDateTime.now());
                ParkingSpace savedSpace = parkingRepository.save(space);
                return toDTO(savedSpace);
            }
            return null;
        }).orElse(null);
    }

    @Override
    public ParkingSpaceDTO updateParkingStatus(UUID parkingId, boolean isAvailable) {
        Optional<ParkingSpace> optionalSpace = parkingRepository.findById(parkingId);
        return optionalSpace.map(space -> {
            space.setAvailable(isAvailable);
            space.setLastUpdated(LocalDateTime.now());
            ParkingSpace savedSpace = parkingRepository.save(space);
            return toDTO(savedSpace);
        }).orElse(null);
    }

    @Override
    public List<ParkingSpaceDTO> filterParkingSpacesByZone(String zone) {
        return parkingRepository.findByZone(zone)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParkingSpaceDTO updateLastUpdated(UUID parkingId, LocalDateTime lastUpdated) {
        Optional<ParkingSpace> optionalSpace = parkingRepository.findById(parkingId);
        return optionalSpace.map(space -> {
            space.setLastUpdated(lastUpdated);
            ParkingSpace savedSpace = parkingRepository.save(space);
            return toDTO(savedSpace);
        }).orElse(null);
    }

    @Override
    public ParkingSpaceDTO saveParkingSpace(ParkingSpaceDTO parkingSpaceDTO, UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty() || (!userOptional.get().getRole().equals(UserRole.ADMIN) && !userOptional.get().getRole().equals(UserRole.OWNER))) {
            return null; // Unauthorized
        }

        if (parkingRepository.existsByLocation(parkingSpaceDTO.getLocation()) || parkingRepository.existsByLocationCode(parkingSpaceDTO.getLocationCode())) {
            return null; // Location or code already exists
        }

        ParkingSpace parkingSpace = toEntity(parkingSpaceDTO);
        parkingSpace.setAvailable(true);
        parkingSpace.setUserId(userId);
        parkingSpace.setLastUpdated(LocalDateTime.now());
        ParkingSpace savedSpace = parkingRepository.save(parkingSpace);
        return toDTO(savedSpace);
    }

    @Override
    public ParkingSpaceDTO updateParkingSpace(UUID parkingId, ParkingSpaceDTO parkingSpaceDTO, UUID userId) {
        Optional<ParkingSpace> optionalSpace = parkingRepository.findById(parkingId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (optionalSpace.isEmpty() || userOptional.isEmpty()) {
            return null; // Space or user not found
        }

        User user = userOptional.get();
        ParkingSpace space = optionalSpace.get();
        if (!user.getRole().equals(UserRole.ADMIN) && !space.getUserId().equals(userId)) {
            return null; // Unauthorized
        }

        if (parkingSpaceDTO.getLocation() != null && !parkingSpaceDTO.getLocation().equals(space.getLocation()) && parkingRepository.existsByLocation(parkingSpaceDTO.getLocation())) {
            return null; // New location already exists
        }

        if (parkingSpaceDTO.getLocationCode() != 0 && parkingSpaceDTO.getLocationCode() != space.getLocationCode() && parkingRepository.existsByLocationCode(parkingSpaceDTO.getLocationCode())) {
            return null; // New location code already exists
        }

        space.setLocation(parkingSpaceDTO.getLocation() != null ? parkingSpaceDTO.getLocation() : space.getLocation());
        space.setLocationCode(parkingSpaceDTO.getLocationCode() != 0 ? parkingSpaceDTO.getLocationCode() : space.getLocationCode());
        space.setZone(parkingSpaceDTO.getZone() != null ? parkingSpaceDTO.getZone() : space.getZone());
        space.setPricePerHour(parkingSpaceDTO.getPricePerHour() != null ? parkingSpaceDTO.getPricePerHour() : space.getPricePerHour());
        space.setLastUpdated(LocalDateTime.now());
        ParkingSpace updatedSpace = parkingRepository.save(space);
        return toDTO(updatedSpace);
    }

    @Override
    public void deleteParkingSpace(UUID parkingId, UUID userId) {
        Optional<ParkingSpace> optionalSpace = parkingRepository.findById(parkingId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (optionalSpace.isEmpty() || userOptional.isEmpty()) {
            return; // Space or user not found
        }

        User user = userOptional.get();
        ParkingSpace space = optionalSpace.get();
        if (!user.getRole().equals(UserRole.ADMIN) && !space.getUserId().equals(userId)) {
            return; // Unauthorized
        }

        parkingRepository.delete(space);
    }
}