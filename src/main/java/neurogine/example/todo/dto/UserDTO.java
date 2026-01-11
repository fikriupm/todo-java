package neurogine.example.todo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

  private Long id;
  private String username;
  private String email;
  private String password;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
