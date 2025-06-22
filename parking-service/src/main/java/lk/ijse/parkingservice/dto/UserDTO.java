package lk.ijse.parkingservice.dto;


import lk.ijse.parkingservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private UUID uid;

    private String fullName;

    private String email;

    private String password; // Included for registration, excluded in responses if needed

    private UserRole role;

    private LocalDateTime createdAt;
}