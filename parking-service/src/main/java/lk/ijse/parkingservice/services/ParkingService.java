package lk.ijse.parkingservice.services;

import lk.ijse.parkingservice.dto.ParkingSpaceDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ParkingService {
    List<ParkingSpaceDTO> listAllParkingSpaces();

    ParkingSpaceDTO reserveParkingSpace(UUID parkingId, UUID userId);

    ParkingSpaceDTO releaseParkingSpace(UUID parkingId);

    ParkingSpaceDTO updateStatus(UUID parkingId, boolean isAvailable);

    List<ParkingSpaceDTO> filterByLocation(String location);

    List<ParkingSpaceDTO> filterByAvailability(boolean isAvailable);

    List<ParkingSpaceDTO> filterByZone(String zone);

    ParkingSpaceDTO simulateIoTUpdate(UUID parkingId, boolean isAvailable, LocalDateTime lastUpdated);

    ParkingSpaceDTO saveParkingSpace(ParkingSpaceDTO parkingSpaceDTO, UUID userId);

    ParkingSpaceDTO updateParkingSpace(UUID parkingId, ParkingSpaceDTO parkingSpaceDTO, UUID userId);

    void deleteParkingSpace(UUID parkingId, UUID userId);
}
