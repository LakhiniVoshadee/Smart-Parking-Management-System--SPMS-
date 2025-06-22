package lk.ijse.parkingservice.repo;

import lk.ijse.parkingservice.entity.User;
import lk.ijse.parkingservice.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Find user by email for authentication
    Optional<User> findByEmail(String email);

    // Find users by role (e.g., OWNER, ADMIN)
    List<User> findByRole(UserRole role);
}