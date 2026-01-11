package neurogine.example.todo.controller;

import lombok.RequiredArgsConstructor;
import neurogine.example.todo.dto.TodoDTO;
import neurogine.example.todo.enums.TodoStatus;
import neurogine.example.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {
  
  private final TodoService todoService;

  /**
   * Create a new todo
   * POST /todos
   */
  @PostMapping
  public ResponseEntity<TodoDTO> createTodo(@RequestBody TodoDTO todoDTO) {
    TodoDTO created = todoService.createTodo(todoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  /**
   * Get all todos (optionally filter by status)
   * GET /todos
   * GET /todos?status=COMPLETED
   */
  @GetMapping
  public ResponseEntity<List<TodoDTO>> getAllTodos(
    @RequestParam(required = false) TodoStatus status
  ) {
    List<TodoDTO> todos;
    if (status != null) {
      todos = todoService.getTodosByStatus(status);
    } else {
      todos = todoService.getAllTodos();
    }
    return ResponseEntity.ok(todos);
  }

  /**
   * Get single todo by ID
   * GET /todos/{id}
   */
  @GetMapping("/{id}")
  public ResponseEntity<TodoDTO> getTodoById(@PathVariable Long id) {
    TodoDTO todo = todoService.getTodoById(id);
    return ResponseEntity.ok(todo);
  }

  /**
   * Update todo (title, description, status, icon)
   * PUT /todos/{id}
   */
  @PutMapping("/{id}")
  public ResponseEntity<TodoDTO> updateTodo(
    @PathVariable Long id,
    @RequestBody TodoDTO todoDTO
  ) {
    TodoDTO updated = todoService.updateTodo(id, todoDTO);
    return ResponseEntity.ok(updated);
  }

  /**
   * Update only status
   * PATCH /todos/{id}/status
   * Body: { "status": "IN_PROGRESS" }
   */
  @PatchMapping("/{id}/status")
  public ResponseEntity<TodoDTO> updateStatus(
    @PathVariable Long id,
    @RequestBody Map<String, String> body
  ) {
    TodoStatus newStatus = TodoStatus.valueOf(body.get("status"));
    TodoDTO updated = todoService.updateStatus(id, newStatus);
    return ResponseEntity.ok(updated);
  }

  /**
   * Start working on todo (NEW → IN_PROGRESS)
   * PATCH /todos/{id}/start
   */
  @PatchMapping("/{id}/start")
  public ResponseEntity<TodoDTO> startTodo(@PathVariable Long id) {
    TodoDTO updated = todoService.startTodo(id);
    return ResponseEntity.ok(updated);
  }

  /**
   * Mark todo as completed
   * PATCH /todos/{id}/complete
   */
  @PatchMapping("/{id}/complete")
  public ResponseEntity<TodoDTO> completeTodo(@PathVariable Long id) {
    TodoDTO updated = todoService.completeTodo(id);
    return ResponseEntity.ok(updated);
  }

  /**
   * Reopen completed todo
   * PATCH /todos/{id}/reopen
   */
  @PatchMapping("/{id}/reopen")
  public ResponseEntity<TodoDTO> reopenTodo(@PathVariable Long id) {
    TodoDTO updated = todoService.reopenTodo(id);
    return ResponseEntity.ok(updated);
  }

  /**
   * Delete todo
   * DELETE /todos/{id}
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
    todoService.deleteTodo(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Toggle favorite status
   * PATCH /todos/{id}/favorite
   */
  @PatchMapping("/{id}/favorite")
  public ResponseEntity<TodoDTO> toggleFavorite(@PathVariable Long id) {
    TodoDTO updated = todoService.toggleFavorite(id);
    return ResponseEntity.ok(updated);
  }

  /**
   * Get all favorite todos
   * GET /todos/favorites
   */
  @GetMapping("/favorites")
  public ResponseEntity<List<TodoDTO>> getFavoriteTodos() {
    List<TodoDTO> favorites = todoService.getFavoriteTodos();
    return ResponseEntity.ok(favorites);
  }

  /**
   * Search todos by keyword
   * GET /todos/search?keyword=...
   */
  @GetMapping("/search")
  public ResponseEntity<List<TodoDTO>> searchTodos(@RequestParam String keyword) {
    List<TodoDTO> todos = todoService.searchTodos(keyword);
    return ResponseEntity.ok(todos);
  }

  /**
   * Get statistics (count by status)
   * GET /todos/statistics
   */
  @GetMapping("/statistics")
  public ResponseEntity<Map<String, Long>> getStatistics() {
    Map<String, Long> stats = todoService.getStatistics();
    return ResponseEntity.ok(stats);
  }
}