package neurogine.example.todo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import neurogine.example.todo.dto.AuthDTO;
import neurogine.example.todo.dto.UserDTO;
import neurogine.example.todo.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  
  /**
   * Register a new user account
   * 
   * @param userDTO the user data transfer object containing registration details
   * @return ResponseEntity with the registered user details and HTTP 201 Created status
   */
  @PostMapping("/register")
  public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
    UserDTO registeredUser = userService.registerProfile(userDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO) {
    try {
      // we already set the isActive to true during registration
      // if(!userService.isAccountActive(authDTO.getEmail())) {
      //   return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Account is not active"));
      // }
      Map<String, Object> response = userService.authenticateAndGenerateToken(authDTO);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email or password"));
    }
  }

  @GetMapping("/profile")
  public ResponseEntity<UserDTO> getProfile() {
    UserDTO userDTO = userService.getUserProfile(null);
    return ResponseEntity.ok(userDTO);
  }
  
}
