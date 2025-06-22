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
@Table(name = "parking_space")
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "parking_id", updatable = false, nullable = false)
    private UUID parkingId;

    @Column(nullable = false, unique = true)
    private String location;

    @Column(unique = true)
    private int locationCode;

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    private boolean isAvailable;

    @Column(name = "user_id")
    private UUID userId; // Replaced email with userId

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(nullable = false)
    private String zone;

    @Column(name = "price_per_hour")
    private Double pricePerHour;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}