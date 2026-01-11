package neurogine.example.todo.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import neurogine.example.todo.dto.AuthDTO;
import neurogine.example.todo.dto.UserDTO;
import neurogine.example.todo.entity.UserEntity;
import neurogine.example.todo.repository.UserRepositroy;
import neurogine.example.todo.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepositroy userRepositroy;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public UserDTO registerProfile(UserDTO userDTO) {

    UserEntity newUser = toEntity(userDTO);
    newUser.setIsActive(true); // Activate the account upon registration
    newUser = userRepositroy.save(newUser);
    return toDTO(newUser);
  }

  public UserEntity toEntity(UserDTO userDTO) {
    return UserEntity.builder()
      .id(userDTO.getId())
      .username(userDTO.getUsername())
      .email(userDTO.getEmail())
      .password(passwordEncoder.encode(userDTO.getPassword()))
      .createdAt(userDTO.getCreatedAt())
      .updatedAt(userDTO.getUpdatedAt())
      .build();
  }

  public UserDTO toDTO(UserEntity userEntity) {
    return UserDTO.builder()
      .id(userEntity.getId())
      .username(userEntity.getUsername())
      .email(userEntity.getEmail())
      .createdAt(userEntity.getCreatedAt())
      .updatedAt(userEntity.getUpdatedAt())
      .build();
  }

  public boolean isAccountActive(String email) {
    return userRepositroy.findByEmail(email)
      .map(UserEntity::getIsActive)
      .orElse(false);
  }

  public UserEntity getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userRepositroy.findByEmail(authentication.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public UserDTO getUserProfile(String email) {

    UserEntity currentUser = null;
    if (email == null) {
      currentUser = getCurrentUser();
    } else {
      currentUser = userRepositroy.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    return UserDTO.builder()
      .id(currentUser.getId())
      .username(currentUser.getUsername())
      .email(currentUser.getEmail())
      .createdAt(currentUser.getCreatedAt())
      .updatedAt(currentUser.getUpdatedAt())
      .build();
  }

  public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO){
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        authDTO.getEmail(), authDTO.getPassword()
      ));

      String token = jwtUtil.generateToken(authDTO.getEmail());
      return Map.of(
        "user", getUserProfile(authDTO.getEmail()),
        "token", token
      );
    } catch (Exception e) {
      throw new RuntimeException("Invalid Credentials" + e.getMessage());
    }
  }


  


  
}
