package lk.ijse.vehicleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO {

    private UUID vehicleId;

    private String vin;

    private String make;

    private String model;

    private int year;

    private String licensePlate;

    private UUID userId;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private LocalDateTime lastUpdated;
}