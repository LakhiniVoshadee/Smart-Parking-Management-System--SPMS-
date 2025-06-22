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
    private ParkingService parkingService;

    @GetMapping("/spaces")
    public ResponseEntity<List<ParkingSpaceDTO>> getAllParkingSpaces() {
        List<ParkingSpaceDTO> spaces = parkingService.getAllParkingSpaces();
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/available-spaces")
    public ResponseEntity<List<ParkingSpaceDTO>> getAvailableParkingSpaces() {
        List<ParkingSpaceDTO> spaces = parkingService.getAvailableParkingSpaces();
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/space/{id}")
    public ResponseEntity<ParkingSpaceDTO> getParkingSpaceById(@PathVariable UUID id) {
        ParkingSpaceDTO space = parkingService.getParkingSpaceById(id);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.notFound().build();
    }

    @PostMapping("/reserve/{id}")
    public ResponseEntity<ParkingSpaceDTO> reserveParkingSpace(@PathVariable UUID id, @RequestParam UUID userId) {
        ParkingSpaceDTO space = parkingService.reserveParkingSpace(id, userId);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/release/{id}")
    public ResponseEntity<ParkingSpaceDTO> releaseParkingSpace(@PathVariable UUID id) {
        ParkingSpaceDTO space = parkingService.releaseParkingSpace(id);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<ParkingSpaceDTO> updateParkingStatus(@PathVariable UUID id, @RequestParam boolean isAvailable) {
        ParkingSpaceDTO space = parkingService.updateParkingStatus(id, isAvailable);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.notFound().build();
    }

    @GetMapping("/filter-by-zone")
    public ResponseEntity<List<ParkingSpaceDTO>> filterParkingSpacesByZone(@RequestParam String zone) {
        List<ParkingSpaceDTO> spaces = parkingService.filterParkingSpacesByZone(zone);
        return ResponseEntity.ok(spaces);
    }

    @PutMapping("/update-last-updated/{id}")
    public ResponseEntity<ParkingSpaceDTO> updateLastUpdated(@PathVariable UUID id, @RequestParam LocalDateTime lastUpdated) {
        ParkingSpaceDTO space = parkingService.updateLastUpdated(id, lastUpdated);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.notFound().build();
    }

}
