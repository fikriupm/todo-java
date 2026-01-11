package neurogine.example.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import neurogine.example.todo.entity.UserEntity;

public interface UserRepositroy extends JpaRepository<UserEntity, Long> {

  // select * from users where email = ?
  Optional<UserEntity> findByEmail(String email);
  
}