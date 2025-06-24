package lk.ijse.paymentserver.service;

import lk.ijse.paymentserver.dto.PaymentDTO;

import java.util.UUID;

public interface PaymentService {
    PaymentDTO initiatePayment(UUID userId, UUID parkingId, Double amount, String paymentMethod);

    PaymentDTO simulateTransaction(UUID paymentId, String transactionStatus);

    PaymentDTO generateReceipt(UUID paymentId);
}
