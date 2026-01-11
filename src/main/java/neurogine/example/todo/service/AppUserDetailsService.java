package neurogine.example.todo.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import neurogine.example.todo.entity.UserEntity;
import neurogine.example.todo.repository.UserRepositroy;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

  private final UserRepositroy userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    UserEntity existingProfile = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Profile with email " + email + " not found"));
    return User.builder()
        .username(existingProfile.getEmail())
        .password(existingProfile.getPassword())
        .authorities(Collections.emptyList())
        .build();
  }
  
}
