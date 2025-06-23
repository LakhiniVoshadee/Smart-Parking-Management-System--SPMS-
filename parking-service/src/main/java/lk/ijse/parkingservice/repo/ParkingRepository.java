package lk.ijse.parkingservice.repo;

import lk.ijse.parkingservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingSpace, UUID> {

    List<ParkingSpace> findByLocationContainingIgnoreCase(String location);

    List<ParkingSpace> findByIsAvailable(boolean isAvailable);

    Optional<ParkingSpace> findByLocationCode(int locationCode);

    List<ParkingSpace> findByZone(String zone);
}
