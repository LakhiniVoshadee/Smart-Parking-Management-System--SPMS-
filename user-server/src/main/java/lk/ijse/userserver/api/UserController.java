package lk.ijse.userserver.api;

import lk.ijse.userserver.dto.UserDTO;
import lk.ijse.userserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO registeredUser = userService.registerUser(userDTO);
        return registeredUser != null ? ResponseEntity.ok(registeredUser) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserDTO> authenticateUser(@RequestParam String email, @RequestParam String password) {
        UserDTO user = userService.authenticateUser(email, password);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(401).build(); // Unauthorized
    }

    @GetMapping("/profile/{uid}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable UUID uid) {
        UserDTO user = userService.getUserProfile(uid);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/profile/{uid}")
    public ResponseEntity<UserDTO> updateUserProfile(@PathVariable UUID uid, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserProfile(uid, userDTO);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/booking-history/{uid}")
    public ResponseEntity<List<UserDTO>> getBookingHistory(@PathVariable UUID uid) {
        List<UserDTO> history = userService.getBookingHistory(uid);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/add-log/{uid}")
    public ResponseEntity<Void> addBookingLog(@PathVariable UUID uid, @RequestParam LocalDateTime timestamp, @RequestParam String action) {
        userService.addBookingLog(uid, timestamp, action);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logs/{uid}")
    public ResponseEntity<List<String>> getUserLogs(@PathVariable UUID uid) {
        List<String> logs = userService.getUserLogs(uid);
        return ResponseEntity.ok(logs);
    }
}