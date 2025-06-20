package lk.ijse.parkingservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingDTO {
    private UUID id;
    private String location;
    private int LocationCode;
    private String City;
    private boolean Available;
    private String email;
    private int payAmount;

}
