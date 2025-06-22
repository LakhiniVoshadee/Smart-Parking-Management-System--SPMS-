package lk.ijse.parkingservice.services.impl;

import lk.ijse.parkingservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingservice.entity.ParkingSpace;
import lk.ijse.parkingservice.repo.ParkingRepository;
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
            return null; // Reservation failed
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
            return null; // Release failed
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
}
