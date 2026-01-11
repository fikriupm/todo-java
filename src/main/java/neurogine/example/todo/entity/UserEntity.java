package neurogine.example.todo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  @Column(unique = true)
  private String email;
  private String password;

  @Column(updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
  @CreationTimestamp
  private LocalDateTime updatedAt;
  @Column(nullable = false)
  private Boolean isActive;

  @PrePersist
  public void prePersist() {
    if (this.isActive == null) {
      this.isActive = true;
    }
  }
}
