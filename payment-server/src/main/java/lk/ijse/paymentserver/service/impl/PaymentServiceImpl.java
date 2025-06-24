package lk.ijse.paymentserver.service.impl;

import lk.ijse.paymentserver.dto.PaymentDTO;
import lk.ijse.paymentserver.entity.Payment;
import lk.ijse.paymentserver.repo.PaymentRepository;
import lk.ijse.paymentserver.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setUserId(payment.getUserId());
        dto.setParkingId(payment.getParkingId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setTransactionStatus(payment.getTransactionStatus());
        dto.setTransactionTime(payment.getTransactionTime());
        dto.setReceiptGeneratedAt(payment.getReceiptGeneratedAt());
        dto.setLastUpdated(payment.getLastUpdated());
        return dto;
    }

    private Payment toEntity(PaymentDTO dto) {
        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setUserId(dto.getUserId());
        payment.setParkingId(dto.getParkingId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionStatus(dto.getTransactionStatus());
        payment.setTransactionTime(dto.getTransactionTime());
        payment.setReceiptGeneratedAt(dto.getReceiptGeneratedAt());
        payment.setLastUpdated(dto.getLastUpdated() != null ? dto.getLastUpdated() : LocalDateTime.now());
        return payment;
    }

    @Override
    public PaymentDTO initiatePayment(UUID userId, UUID parkingId, Double amount, String paymentMethod) {
        if (amount <= 0 || !isValidPaymentMethod(paymentMethod)) {
            return null; // Invalid amount or payment method
        }
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setParkingId(parkingId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionStatus("PENDING");
        payment.setTransactionTime(LocalDateTime.now());
        payment.setLastUpdated(LocalDateTime.now());
        return toDTO(paymentRepository.save(payment));
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        // Mock validation: Ensure paymentMethod matches a pattern (e.g., "VISA-XXXX" or "MASTERCARD-XXXX")
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) return false;
        return paymentMethod.matches("^(VISA|MASTERCARD)-\\d{4}$");
    }

    @Override
    public PaymentDTO simulateTransaction(UUID paymentId, String transactionStatus) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentId(paymentId);
        return paymentOptional.map(payment -> {
            if (!payment.getTransactionStatus().equals("PENDING")) {
                return null; // Only pending transactions can be updated
            }
            if (!transactionStatus.matches("^(SUCCESS|FAILED)$")) {
                return null; // Invalid status
            }
            payment.setTransactionStatus(transactionStatus);
            payment.setLastUpdated(LocalDateTime.now());
            return toDTO(paymentRepository.save(payment));
        }).orElse(null);
    }

    @Override
    public PaymentDTO generateReceipt(UUID paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentId(paymentId);
        return paymentOptional.filter(payment -> payment.getTransactionStatus().equals("SUCCESS")).map(payment -> {
            payment.setReceiptGeneratedAt(LocalDateTime.now());
            payment.setLastUpdated(LocalDateTime.now());
            return toDTO(paymentRepository.save(payment));
        }).orElse(null);
    }
}