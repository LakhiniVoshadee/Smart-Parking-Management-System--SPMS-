package lk.ijse.parkingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingSpaceDTO {

    private UUID parkingId;

    private String location;

    private int locationCode;

    private boolean isAvailable;

    private UUID userId;

    private LocalDateTime reservedAt;

    private String zone;

    private Double pricePerHour;

    private LocalDateTime lastUpdated;
}