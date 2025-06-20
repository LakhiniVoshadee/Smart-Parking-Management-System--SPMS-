package lk.ijse.parkingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID parking_id;

    @Column(nullable = false, unique = true)
    private String location;

    @Column(unique = true)
    private int locationCode;

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    private boolean isAvailable;

    @Column(nullable = false)
    private LocalDateTime reservedAt;

    @Column(nullable = false)
    private String zone;

    @Column
    private Double pricePerHour;

    @Column(nullable = false)
    private String email;

    @Column
    private LocalDateTime lastUpdated;
}