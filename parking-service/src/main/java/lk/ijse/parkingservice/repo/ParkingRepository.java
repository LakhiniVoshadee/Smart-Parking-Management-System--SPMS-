package lk.ijse.parkingservice.repo;

import lk.ijse.parkingservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingSpace, UUID> {

    List<ParkingSpace> findByIsAvailableTrue();

    List<ParkingSpace> findByZone(String zone);

    ParkingSpace findByLocation(String location);

    ParkingSpace findByLocationCode(int locationCode);

    List<ParkingSpace> findByUserId(UUID userId);

    boolean existsByLocation(String location);

    boolean existsByLocationCode(int locationCode);
}
