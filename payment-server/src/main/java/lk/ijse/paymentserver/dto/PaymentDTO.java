package lk.ijse.paymentserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {

    private UUID paymentId;

    private UUID userId;

    private UUID parkingId;

    private Double amount;

    private String paymentMethod;

    private String transactionStatus;

    private LocalDateTime transactionTime;

    private LocalDateTime receiptGeneratedAt;

    private LocalDateTime lastUpdated;
}