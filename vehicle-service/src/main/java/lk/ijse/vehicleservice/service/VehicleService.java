package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.VehicleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VehicleService {
    VehicleDTO registerVehicle(VehicleDTO vehicleDTO);

    VehicleDTO updateVehicle(UUID vehicleId, VehicleDTO vehicleDTO);

    VehicleDTO getVehicleById(UUID vehicleId);

    List<VehicleDTO> getVehiclesByUserId(UUID userId);

    VehicleDTO simulateEntry(UUID vehicleId, LocalDateTime entryTime);

    VehicleDTO simulateExit(UUID vehicleId, LocalDateTime exitTime);
}
