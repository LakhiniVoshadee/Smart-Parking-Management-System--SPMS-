package lk.ijse.parkingservice.api;

import lk.ijse.parkingservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingservice.services.ParkingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingSpaceService;

    @GetMapping("/spaces")
    public ResponseEntity<List<ParkingSpaceDTO>> listAllParkingSpaces() {
        return ResponseEntity.ok(parkingSpaceService.listAllParkingSpaces());
    }

    @PostMapping("/reserve/{parkingId}")
    public ResponseEntity<ParkingSpaceDTO> reserveParkingSpace(@PathVariable UUID parkingId, @RequestParam UUID userId) {
        ParkingSpaceDTO space = parkingSpaceService.reserveParkingSpace(parkingId, userId);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/release/{parkingId}")
    public ResponseEntity<ParkingSpaceDTO> releaseParkingSpace(@PathVariable UUID parkingId) {
        ParkingSpaceDTO space = parkingSpaceService.releaseParkingSpace(parkingId);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/status/{parkingId}")
    public ResponseEntity<ParkingSpaceDTO> updateStatus(@PathVariable UUID parkingId, @RequestParam boolean isAvailable) {
        ParkingSpaceDTO space = parkingSpaceService.updateStatus(parkingId, isAvailable);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/filter/location")
    public ResponseEntity<List<ParkingSpaceDTO>> filterByLocation(@RequestParam String location) {
        return ResponseEntity.ok(parkingSpaceService.filterByLocation(location));
    }

    @GetMapping("/filter/availability")
    public ResponseEntity<List<ParkingSpaceDTO>> filterByAvailability(@RequestParam boolean isAvailable) {
        return ResponseEntity.ok(parkingSpaceService.filterByAvailability(isAvailable));
    }

    @GetMapping("/filter/zone")
    public ResponseEntity<List<ParkingSpaceDTO>> filterByZone(@RequestParam String zone) {
        return ResponseEntity.ok(parkingSpaceService.filterByZone(zone));
    }

    @PostMapping("/iot/{parkingId}")
    public ResponseEntity<ParkingSpaceDTO> simulateIoTUpdate(@PathVariable UUID parkingId, @RequestParam boolean isAvailable,
                                                             @RequestParam(required = false) LocalDateTime lastUpdated) {
        ParkingSpaceDTO space = parkingSpaceService.simulateIoTUpdate(parkingId, isAvailable, lastUpdated);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.badRequest().build();
    }
    @PostMapping("/save")
    public ResponseEntity<ParkingSpaceDTO> saveParkingSpace(@RequestBody ParkingSpaceDTO parkingSpaceDTO, @RequestParam UUID userId) {
        ParkingSpaceDTO space = parkingSpaceService.saveParkingSpace(parkingSpaceDTO, userId);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/update/{parkingId}")
    public ResponseEntity<ParkingSpaceDTO> updateParkingSpace(@PathVariable UUID parkingId, @RequestBody ParkingSpaceDTO parkingSpaceDTO, @RequestParam UUID userId) {
        ParkingSpaceDTO space = parkingSpaceService.updateParkingSpace(parkingId, parkingSpaceDTO, userId);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{parkingId}")
    public ResponseEntity<Void> deleteParkingSpace(@PathVariable UUID parkingId, @RequestParam UUID userId) {
        parkingSpaceService.deleteParkingSpace(parkingId, userId);
        return ResponseEntity.ok().build();
    }
}