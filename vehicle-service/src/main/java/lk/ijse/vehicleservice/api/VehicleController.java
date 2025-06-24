package lk.ijse.vehicleservice.api;

import lk.ijse.vehicleservice.dto.VehicleDTO;

import lk.ijse.vehicleservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/register")
    public ResponseEntity<VehicleDTO> registerVehicle(@RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO registeredVehicle = vehicleService.registerVehicle(vehicleDTO);
        return registeredVehicle != null ? ResponseEntity.ok(registeredVehicle) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable UUID vehicleId, @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicleDTO);
        return updatedVehicle != null ? ResponseEntity.ok(updatedVehicle) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable UUID vehicleId) {
        VehicleDTO vehicle = vehicleService.getVehicleById(vehicleId);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByUserId(@PathVariable UUID userId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping("/entry/{vehicleId}")
    public ResponseEntity<VehicleDTO> simulateEntry(@PathVariable UUID vehicleId, @RequestParam LocalDateTime entryTime) {
        VehicleDTO vehicle = vehicleService.simulateEntry(vehicleId, entryTime);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/exit/{vehicleId}")
    public ResponseEntity<VehicleDTO> simulateExit(@PathVariable UUID vehicleId, @RequestParam LocalDateTime exitTime) {
        VehicleDTO vehicle = vehicleService.simulateExit(vehicleId, exitTime);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.badRequest().build();
    }
}