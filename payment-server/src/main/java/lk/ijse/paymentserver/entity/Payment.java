package lk.ijse.paymentserver.entity;

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
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", updatable = false, nullable = false)
    private UUID paymentId;

    @Column(name = "user_id", nullable = false)
    private UUID userId; // Links to User entity

    @Column(name = "parking_id", nullable = false)
    private UUID parkingId; // Links to ParkingSpace entity

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod; // e.g., "VISA-1234", mock card data

    @Column(nullable = false)
    private String transactionStatus; // e.g., "PENDING", "SUCCESS", "FAILED"

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "receipt_generated_at")
    private LocalDateTime receiptGeneratedAt;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}