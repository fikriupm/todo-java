package neurogine.example.todo.service;

import lombok.RequiredArgsConstructor;
import neurogine.example.todo.dto.TodoDTO;
import neurogine.example.todo.entity.TodoEntity;
import neurogine.example.todo.entity.UserEntity;
import neurogine.example.todo.enums.TodoStatus;
import neurogine.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
  
  private final TodoRepository todoRepository;
  private final UserService userService;

  /**
   * Create a new todo (status = NEW by default)
   */
  public TodoDTO createTodo(TodoDTO todoDTO) {
    UserEntity currentUser = userService.getCurrentUser();
    
    // Use toEntity method - cleaner!
    TodoEntity todo = toEntity(todoDTO, currentUser);
    TodoEntity savedTodo = todoRepository.save(todo);
    return toDTO(savedTodo);
  }

  /**
   * Get all todos for current user
   */
  public List<TodoDTO> getAllTodos() {
    UserEntity currentUser = userService.getCurrentUser();
    return todoRepository.findByUserId(currentUser.getId())
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  /**
   * Get todos by status (NEW, IN_PROGRESS, COMPLETED)
   */
  public List<TodoDTO> getTodosByStatus(TodoStatus status) {
    UserEntity currentUser = userService.getCurrentUser();
    return todoRepository.findByUserIdAndStatus(currentUser.getId(), status)
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  /**
   * Get single todo by ID
   */
  public TodoDTO getTodoById(Long id) {
    UserEntity currentUser = userService.getCurrentUser();
    TodoEntity todo = todoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Todo not found"));
    
    if (!todo.getUser().getId().equals(currentUser.getId())) {
      throw new RuntimeException("Access denied");
    }
    
    return toDTO(todo);
  }

  /**
   * Update todo (can change title, description, status)
   */
  public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
    UserEntity currentUser = userService.getCurrentUser();
    TodoEntity todo = todoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Todo not found"));
    
    if (!todo.getUser().getId().equals(currentUser.getId())) {
      throw new RuntimeException("Access denied");
    }
    
    // Update fields
    if (todoDTO.getTitle() != null) {
      todo.setTitle(todoDTO.getTitle());
    }
    if (todoDTO.getDescription() != null) {
      todo.setDescription(todoDTO.getDescription());
    }
    if (todoDTO.getStatus() != null) {
      todo.setStatus(todoDTO.getStatus());
    }
    if (todoDTO.getIcon() != null) {
      todo.setIcon(todoDTO.getIcon());
    }
    
    TodoEntity updatedTodo = todoRepository.save(todo);
    return toDTO(updatedTodo);
  }

  /**
   * Change todo status (NEW → IN_PROGRESS → COMPLETED)
   */
  public TodoDTO updateStatus(Long id, TodoStatus newStatus) {
    UserEntity currentUser = userService.getCurrentUser();
    TodoEntity todo = todoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Todo not found"));
    
    if (!todo.getUser().getId().equals(currentUser.getId())) {
      throw new RuntimeException("Access denied");
    }
    
    todo.setStatus(newStatus);
    TodoEntity updatedTodo = todoRepository.save(todo);
    return toDTO(updatedTodo);
  }

  /**
   * Start working on a todo (NEW → IN_PROGRESS)
   */
  public TodoDTO startTodo(Long id) {
    return updateStatus(id, TodoStatus.IN_PROGRESS);
  }

  /**
   * Mark todo as completed
   */
  public TodoDTO completeTodo(Long id) {
    return updateStatus(id, TodoStatus.COMPLETED);
  }

  /**
   * Reopen a completed todo (COMPLETED → NEW)
   */
  public TodoDTO reopenTodo(Long id) {
    return updateStatus(id, TodoStatus.NEW);
  }

  /**
   * Delete todo
   */
  public void deleteTodo(Long id) {
    UserEntity currentUser = userService.getCurrentUser();
    TodoEntity todo = todoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Todo not found"));
    
    if (!todo.getUser().getId().equals(currentUser.getId())) {
      throw new RuntimeException("Access denied");
    }
    
    todoRepository.delete(todo);
  }

  // /**
  //  * Search todos by keyword
  //  */
  // public List<TodoDTO> searchTodos(String keyword) {
  //   UserEntity currentUser = userService.getCurrentUser();
  //   return todoRepository.findByUserIdAndTitleContainingIgnoreCase(currentUser.getId(), keyword)
  //     .stream()
  //     .map(this::toDTO)
  //     .collect(Collectors.toList());
  // }

  /**
   * Toggle favorite status
   * PATCH /todos/{id}/favorite
   */
  public TodoDTO toggleFavorite(Long id) {
    UserEntity currentUser = userService.getCurrentUser();
    TodoEntity todo = todoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Todo not found"));
    
    if (!todo.getUser().getId().equals(currentUser.getId())) {
      throw new RuntimeException("Access denied");
    }
    
    todo.setIsFavorite(!todo.getIsFavorite());
    TodoEntity updated = todoRepository.save(todo);
    return toDTO(updated);
  }

  /**
   * Get all favorite todos
   */
  public List<TodoDTO> getFavoriteTodos() {
    UserEntity currentUser = userService.getCurrentUser();
    List<TodoEntity> favorites = todoRepository.findByUserIdAndIsFavoriteTrue(currentUser.getId());
    return favorites.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * Search todos by keyword
   */
  public List<TodoDTO> searchTodos(String keyword) {
    UserEntity currentUser = userService.getCurrentUser();
    List<TodoEntity> todos = todoRepository.findByUserId(currentUser.getId());
    
    return todos.stream()
      .filter(t -> t.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                   (t.getDescription() != null && t.getDescription().toLowerCase().contains(keyword.toLowerCase())))
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  /**
   * Get statistics (count by status)
   */
    public Map<String, Long> getStatistics() {
      UserEntity currentUser = userService.getCurrentUser();
      List<TodoEntity> todos = todoRepository.findByUserId(currentUser.getId());
    
    Map<String, Long> stats = Map.of(
      "new", todos.stream().filter(t -> t.getStatus() == TodoStatus.NEW).count(),
      "inProgress", todos.stream().filter(t -> t.getStatus() == TodoStatus.IN_PROGRESS).count(),
      "completed", todos.stream().filter(t -> t.getStatus() == TodoStatus.COMPLETED).count(),
      "total", (long) todos.size(),
      "favorites", todos.stream().filter(TodoEntity::getIsFavorite).count()
    );
    
    return stats;
  }
  /**
   * Convert Entity to DTO
   */
  private TodoDTO toDTO(TodoEntity todo) {
    return TodoDTO.builder()
      .id(todo.getId())
      .title(todo.getTitle())
      .description(todo.getDescription())
      .status(todo.getStatus())
      .icon(todo.getIcon())
      .isFavorite(todo.getIsFavorite())
      .userId(todo.getUser().getId())
      .username(todo.getUser().getUsername())
      .createdAt(todo.getCreatedAt())
      .updatedAt(todo.getUpdatedAt())
      .build();
  }

    private TodoEntity toEntity(TodoDTO todoDTO, UserEntity user) {
    return TodoEntity.builder()
      .title(todoDTO.getTitle())
      .description(todoDTO.getDescription())
      .status(todoDTO.getStatus() != null ? todoDTO.getStatus() : TodoStatus.NEW)
      .icon(todoDTO.getIcon())
      .isFavorite(todoDTO.getIsFavorite() != null ? todoDTO.getIsFavorite() : false)
      .user(user)
      .build();
  }
}