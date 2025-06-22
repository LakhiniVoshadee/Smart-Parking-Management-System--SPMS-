package lk.ijse.parkingservice.services;

import lk.ijse.parkingservice.dto.ParkingSpaceDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ParkingService {
    List<ParkingSpaceDTO> getAllParkingSpaces();

    List<ParkingSpaceDTO> getAvailableParkingSpaces();

    ParkingSpaceDTO getParkingSpaceById(UUID parkingId);

    ParkingSpaceDTO reserveParkingSpace(UUID parkingId, UUID userId);

    ParkingSpaceDTO releaseParkingSpace(UUID parkingId);

    ParkingSpaceDTO updateParkingStatus(UUID parkingId, boolean isAvailable);

    List<ParkingSpaceDTO> filterParkingSpacesByZone(String zone);

    ParkingSpaceDTO updateLastUpdated(UUID parkingId, LocalDateTime lastUpdated);
}
