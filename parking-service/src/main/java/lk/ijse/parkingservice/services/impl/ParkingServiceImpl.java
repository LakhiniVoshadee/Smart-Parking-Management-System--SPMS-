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
    private ParkingRepository parkingSpaceRepository;

    private ParkingSpaceDTO toDTO(ParkingSpace space) {
        ParkingSpaceDTO dto = new ParkingSpaceDTO();
        dto.setParkingId(space.getParkingId());
        dto.setLocation(space.getLocation());
        dto.setLocationCode(space.getLocationCode());
        dto.setAvailable(space.isAvailable());
        dto.setUserId(space.getUserId());
        dto.setReservedAt(space.getReservedAt());
        dto.setZone(space.getZone());
        dto.setPricePerHour(space.getPricePerHour());
        dto.setLastUpdated(space.getLastUpdated());
        return dto;
    }

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
        space.setLastUpdated(dto.getLastUpdated() != null ? dto.getLastUpdated() : LocalDateTime.now());
        return space;
    }

    @Override
    public List<ParkingSpaceDTO> listAllParkingSpaces() {
        return parkingSpaceRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ParkingSpaceDTO reserveParkingSpace(UUID parkingId, UUID userId) {
        Optional<ParkingSpace> spaceOptional = parkingSpaceRepository.findById(parkingId);
        return spaceOptional.filter(ParkingSpace::isAvailable).map(space -> {
            space.setAvailable(false);
            space.setUserId(userId);
            space.setReservedAt(LocalDateTime.now());
            space.setLastUpdated(LocalDateTime.now());
            return toDTO(parkingSpaceRepository.save(space));
        }).orElse(null);
    }

    @Override
    public ParkingSpaceDTO releaseParkingSpace(UUID parkingId) {
        Optional<ParkingSpace> spaceOptional = parkingSpaceRepository.findById(parkingId);
        return spaceOptional.filter(space -> !space.isAvailable()).map(space -> {
            space.setAvailable(true);
            space.setUserId(null);
            space.setReservedAt(null);
            space.setLastUpdated(LocalDateTime.now());
            return toDTO(parkingSpaceRepository.save(space));
        }).orElse(null);
    }

    @Override
    public ParkingSpaceDTO updateStatus(UUID parkingId, boolean isAvailable) {
        Optional<ParkingSpace> spaceOptional = parkingSpaceRepository.findById(parkingId);
        return spaceOptional.map(space -> {
            space.setAvailable(isAvailable);
            space.setLastUpdated(LocalDateTime.now());
            return toDTO(parkingSpaceRepository.save(space));
        }).orElse(null);
    }

    @Override
    public List<ParkingSpaceDTO> filterByLocation(String location) {
        return parkingSpaceRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ParkingSpaceDTO> filterByAvailability(boolean isAvailable) {
        return parkingSpaceRepository.findByIsAvailable(isAvailable).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ParkingSpaceDTO> filterByZone(String zone) {
        return parkingSpaceRepository.findByZone(zone).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ParkingSpaceDTO simulateIoTUpdate(UUID parkingId, boolean isAvailable, LocalDateTime lastUpdated) {
        Optional<ParkingSpace> spaceOptional = parkingSpaceRepository.findById(parkingId);
        return spaceOptional.map(space -> {
            space.setAvailable(isAvailable);
            space.setLastUpdated(lastUpdated != null ? lastUpdated : LocalDateTime.now());
            return toDTO(parkingSpaceRepository.save(space));
        }).orElse(null);
    }
    @Override
    public ParkingSpaceDTO saveParkingSpace(ParkingSpaceDTO parkingSpaceDTO, UUID userId) {
        if (parkingSpaceRepository.findByLocationCode(parkingSpaceDTO.getLocationCode()).isPresent()) {
            return null; // Location code must be unique
        }
        ParkingSpace space = toEntity(parkingSpaceDTO);
        space.setUserId(userId); // Associate with the user
        space.setAvailable(true); // Default to available
        space.setLastUpdated(LocalDateTime.now());
        return toDTO(parkingSpaceRepository.save(space));
    }

    @Override
    public ParkingSpaceDTO updateParkingSpace(UUID parkingId, ParkingSpaceDTO parkingSpaceDTO, UUID userId) {
        Optional<ParkingSpace> spaceOptional = parkingSpaceRepository.findById(parkingId);
        return spaceOptional.filter(space -> space.getUserId().equals(userId)).map(space -> {
            if (parkingSpaceDTO.getLocationCode() != space.getLocationCode() &&
                    parkingSpaceRepository.findByLocationCode(parkingSpaceDTO.getLocationCode()).isPresent()) {
                return null; // New location code must be unique
            }
            space.setLocation(parkingSpaceDTO.getLocation() != null ? parkingSpaceDTO.getLocation() : space.getLocation());
            space.setLocationCode(parkingSpaceDTO.getLocationCode() != 0 ? parkingSpaceDTO.getLocationCode() : space.getLocationCode());
            space.setZone(parkingSpaceDTO.getZone() != null ? parkingSpaceDTO.getZone() : space.getZone());
            space.setPricePerHour(parkingSpaceDTO.getPricePerHour() != null ? parkingSpaceDTO.getPricePerHour() : space.getPricePerHour());
            space.setLastUpdated(LocalDateTime.now());
            return toDTO(parkingSpaceRepository.save(space));
        }).orElse(null);
    }

    @Override
    public void deleteParkingSpace(UUID parkingId, UUID userId) {
        Optional<ParkingSpace> spaceOptional = parkingSpaceRepository.findById(parkingId);
        spaceOptional.filter(space -> space.getUserId().equals(userId)).ifPresent(parkingSpaceRepository::delete);
    }
}