package lk.ijse.paymentserver.repo;

import lk.ijse.paymentserver.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByPaymentId(UUID paymentId);

    Optional<Payment> findByUserIdAndParkingId(UUID userId, UUID parkingId);
}
