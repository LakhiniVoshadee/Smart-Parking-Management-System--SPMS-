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
        vehicle.setLastUpdated(dto.getLastUpdated() != null ? dto.getLastUpdated() : LocalDateTime.now());
        return vehicle;
    }

    @Override
    public VehicleDTO registerVehicle(VehicleDTO vehicleDTO, UUID userId) {
        if (vehicleRepository.findByVin(vehicleDTO.getVin()).isPresent() ||
                vehicleRepository.findByLicensePlate(vehicleDTO.getLicensePlate()).isPresent()) {
            return null; // VIN and license plate must be unique
        }
        Vehicle vehicle = toEntity(vehicleDTO);
        vehicle.setUserId(userId); // Link to user
        vehicle.setLastUpdated(LocalDateTime.now());
        return toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleDTO updateVehicle(UUID vehicleId, VehicleDTO vehicleDTO, UUID userId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        return vehicleOptional.filter(vehicle -> vehicle.getUserId().equals(userId)).map(vehicle -> {
            if ((vehicleDTO.getVin() != null && !vehicleDTO.getVin().equals(vehicle.getVin()) &&
                    vehicleRepository.findByVin(vehicleDTO.getVin()).isPresent()) ||
                    (vehicleDTO.getLicensePlate() != null && !vehicleDTO.getLicensePlate().equals(vehicle.getLicensePlate()) &&
                            vehicleRepository.findByLicensePlate(vehicleDTO.getLicensePlate()).isPresent())) {
                return null; // New VIN or license plate must be unique
            }
            vehicle.setVin(vehicleDTO.getVin() != null ? vehicleDTO.getVin() : vehicle.getVin());
            vehicle.setMake(vehicleDTO.getMake() != null ? vehicleDTO.getMake() : vehicle.getMake());
            vehicle.setModel(vehicleDTO.getModel() != null ? vehicleDTO.getModel() : vehicle.getModel());
            vehicle.setYear(vehicleDTO.getYear() != 0 ? vehicleDTO.getYear() : vehicle.getYear());
            vehicle.setLicensePlate(vehicleDTO.getLicensePlate() != null ? vehicleDTO.getLicensePlate() : vehicle.getLicensePlate());
            vehicle.setLastUpdated(LocalDateTime.now());
            return toDTO(vehicleRepository.save(vehicle));
        }).orElse(null);
    }

    @Override
    public VehicleDTO retrieveVehicle(UUID vehicleId) {
        return vehicleRepository.findById(vehicleId).map(this::toDTO).orElse(null);
    }

    @Override
    public List<VehicleDTO> retrieveVehiclesByUser(UUID userId) {
        return vehicleRepository.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public VehicleDTO simulateEntry(UUID vehicleId, LocalDateTime entryTime) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        return vehicleOptional.map(vehicle -> {
            vehicle.setEntryTime(entryTime != null ? entryTime : LocalDateTime.now());
            vehicle.setExitTime(null); // Clear exit time on entry
            vehicle.setLastUpdated(LocalDateTime.now());
            return toDTO(vehicleRepository.save(vehicle));
        }).orElse(null);
    }

    @Override
    public VehicleDTO simulateExit(UUID vehicleId, LocalDateTime exitTime) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        return vehicleOptional.filter(vehicle -> vehicle.getEntryTime() != null && vehicle.getExitTime() == null).map(vehicle -> {
            vehicle.setExitTime(exitTime != null ? exitTime : LocalDateTime.now());
            vehicle.setLastUpdated(LocalDateTime.now());
            return toDTO(vehicleRepository.save(vehicle));
        }).orElse(null);
    }
}
