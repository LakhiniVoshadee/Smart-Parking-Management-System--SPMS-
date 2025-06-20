package lk.ijse.parkingservice.repo;

import lk.ijse.parkingservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingSpace, UUID> {

    // Find all available parking spaces
    List<ParkingSpace> findByIsAvailableTrue();

    // Find parking spaces by zone
    List<ParkingSpace> findByZone(String zone);

    // Find parking space by location
    ParkingSpace findByLocation(String location);

    // Find parking space by location code
    ParkingSpace findByLocationCode(int locationCode);

    // Find parking spaces reserved by a user
    List<ParkingSpace> findByUserId(UUID userId);
}
