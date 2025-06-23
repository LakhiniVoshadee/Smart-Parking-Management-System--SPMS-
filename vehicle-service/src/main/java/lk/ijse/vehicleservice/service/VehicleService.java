package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.VehicleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VehicleService {
    VehicleDTO registerVehicle(VehicleDTO vehicleDTO, UUID userId);

    VehicleDTO updateVehicle(UUID vehicleId, VehicleDTO vehicleDTO, UUID userId);

    VehicleDTO retrieveVehicle(UUID vehicleId);

    List<VehicleDTO> retrieveVehiclesByUser(UUID userId);

    VehicleDTO simulateEntry(UUID vehicleId, LocalDateTime entryTime);

    VehicleDTO simulateExit(UUID vehicleId, LocalDateTime exitTime);
}
