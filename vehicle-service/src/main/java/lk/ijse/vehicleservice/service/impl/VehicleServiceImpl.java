package lk.ijse.vehicleservice.service.impl;

import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.repo.VehicleRepository;
import lk.ijse.vehicleservice.service.VehicleService;
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
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    private VehicleDTO toDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setVin(vehicle.getVin());
        dto.setMake(vehicle.getMake());
        dto.setModel(vehicle.getModel());
        dto.setYear(vehicle.getYear());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setUserId(vehicle.getUserId());
        dto.setEntryTime(vehicle.getEntryTime());
        dto.setExitTime(vehicle.getExitTime());
        dto.setLastUpdated(vehicle.getLastUpdated());
        return dto;
    }

    private Vehicle toEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(dto.getVehicleId());
        vehicle.setVin(dto.getVin());
        vehicle.setMake(dto.getMake());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setUserId(dto.getUserId());
        vehicle.setEntryTime(dto.getEntryTime());
        vehicle.setExitTime(dto.getExitTime());
        vehicle.setLastUpdated(dto.getLastUpdated());
        return vehicle;
    }

    @Override
    public VehicleDTO registerVehicle(VehicleDTO vehicleDTO) {
        if (vehicleRepository.findByVin(vehicleDTO.getVin()).isPresent() ||
                vehicleRepository.findByLicensePlate(vehicleDTO.getLicensePlate()).isPresent()) {
            return null; // Duplicate VIN or licensePlate
        }
        Vehicle vehicle = toEntity(vehicleDTO);
        vehicle.setLastUpdated(LocalDateTime.now());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return toDTO(savedVehicle);
    }

    @Override
    public VehicleDTO updateVehicle(UUID vehicleId, VehicleDTO vehicleDTO) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        return vehicleOptional.map(vehicle -> {
            if (!vehicle.getVin().equals(vehicleDTO.getVin()) &&
                    vehicleRepository.findByVin(vehicleDTO.getVin()).isPresent()) {
                return null; // VIN already exists
            }
            if (!vehicle.getLicensePlate().equals(vehicleDTO.getLicensePlate()) &&
                    vehicleRepository.findByLicensePlate(vehicleDTO.getLicensePlate()).isPresent()) {
                return null; // License plate already exists
            }
            vehicle.setVin(vehicleDTO.getVin());
            vehicle.setMake(vehicleDTO.getMake());
            vehicle.setModel(vehicleDTO.getModel());
            vehicle.setYear(vehicleDTO.getYear());
            vehicle.setLicensePlate(vehicleDTO.getLicensePlate());
            // userId is not updated for simplicity
            vehicle.setLastUpdated(LocalDateTime.now());
            return toDTO(vehicleRepository.save(vehicle));
        }).orElse(null);
    }

    @Override
    public VehicleDTO getVehicleById(UUID vehicleId) {
        return vehicleRepository.findById(vehicleId).map(this::toDTO).orElse(null);
    }

    @Override
    public List<VehicleDTO> getVehiclesByUserId(UUID userId) {
        return vehicleRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO simulateEntry(UUID vehicleId, LocalDateTime entryTime) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        return vehicleOptional.map(vehicle -> {
            if (vehicle.getEntryTime() == null || vehicle.getExitTime() != null) {
                vehicle.setEntryTime(entryTime);
                vehicle.setExitTime(null);
                vehicle.setLastUpdated(LocalDateTime.now());
                return toDTO(vehicleRepository.save(vehicle));
            }
            return null; // Vehicle is currently parked
        }).orElse(null);
    }

    @Override
    public VehicleDTO simulateExit(UUID vehicleId, LocalDateTime exitTime) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        return vehicleOptional.map(vehicle -> {
            if (vehicle.getEntryTime() != null && vehicle.getExitTime() == null) {
                vehicle.setExitTime(exitTime);
                vehicle.setLastUpdated(LocalDateTime.now());
                return toDTO(vehicleRepository.save(vehicle));
            }
            return null; // Vehicle is not currently parked
        }).orElse(null);
    }
}
