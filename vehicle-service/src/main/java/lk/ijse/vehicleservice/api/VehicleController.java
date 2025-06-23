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
    public ResponseEntity<VehicleDTO> registerVehicle(@RequestBody VehicleDTO vehicleDTO, @RequestParam UUID userId) {
        VehicleDTO vehicle = vehicleService.registerVehicle(vehicleDTO, userId);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable UUID vehicleId, @RequestBody VehicleDTO vehicleDTO, @RequestParam UUID userId) {
        VehicleDTO vehicle = vehicleService.updateVehicle(vehicleId, vehicleDTO, userId);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/retrieve/{vehicleId}")
    public ResponseEntity<VehicleDTO> retrieveVehicle(@PathVariable UUID vehicleId) {
        VehicleDTO vehicle = vehicleService.retrieveVehicle(vehicleId);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VehicleDTO>> retrieveVehiclesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(vehicleService.retrieveVehiclesByUser(userId));
    }

    @PostMapping("/entry/{vehicleId}")
    public ResponseEntity<VehicleDTO> simulateEntry(@PathVariable UUID vehicleId, @RequestParam(required = false) LocalDateTime entryTime) {
        VehicleDTO vehicle = vehicleService.simulateEntry(vehicleId, entryTime);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
    }

    @PostMapping("/exit/{vehicleId}")
    public ResponseEntity<VehicleDTO> simulateExit(@PathVariable UUID vehicleId, @RequestParam(required = false) LocalDateTime exitTime) {
        VehicleDTO vehicle = vehicleService.simulateExit(vehicleId, exitTime);
        return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.badRequest().build();
    }
}