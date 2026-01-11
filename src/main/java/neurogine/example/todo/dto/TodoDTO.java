package neurogine.example.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import neurogine.example.todo.enums.TodoStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {
  
  private Long id;
  private String title;
  private String description;
  private String icon;
  private Boolean isFavorite;
  private TodoStatus status;
  private Long userId;
  private String username;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}