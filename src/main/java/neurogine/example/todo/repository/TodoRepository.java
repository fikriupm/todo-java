package neurogine.example.todo.repository;

import neurogine.example.todo.entity.TodoEntity;
import neurogine.example.todo.enums.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
  
  // Find all todos for a specific user
  List<TodoEntity> findByUserId(Long userId);
  
  // Find todos by status
  List<TodoEntity> findByUserIdAndStatus(Long userId, TodoStatus status);
  
  // Find todos by title containing keyword
  List<TodoEntity> findByUserIdAndTitleContainingIgnoreCase(Long userId, String keyword);
  
  // Count todos by status
  long countByUserIdAndStatus(Long userId, TodoStatus status);

  List<TodoEntity> findByUserIdAndIsFavoriteTrue(Long userId);
}