# Todo Application - Architecture Visualization

## 1. LAYERED ARCHITECTURE DIAGRAM

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT (Frontend)                        │
│                   (Web Browser / Mobile App)                     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP Requests
                              │ (JSON)
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SECURITY LAYER                               │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  JwtRequestFilter                                        │  │
│  │  - Extracts JWT from Authorization header               │  │
│  │  - Validates token signature & expiration               │  │
│  │  - Sets Spring Security context                         │  │
│  │  - Runs on EVERY request (OncePerRequestFilter)         │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│              CONTROLLER LAYER (REST Endpoints)                  │
│                                                                 │
│  ┌──────────────────┐         ┌──────────────────┐            │
│  │ UserController   │         │ TodoController   │            │
│  ├──────────────────┤         ├──────────────────┤            │
│  │ POST /register   │         │ POST /todos      │            │
│  │ POST /login      │         │ GET  /todos      │            │
│  │ GET  /profile    │         │ GET  /todos/{id} │            │
│  │                  │         │ PUT  /todos/{id} │            │
│  │                  │         │ PATCH /todos/{id}│            │
│  │                  │         │ DELETE /todos/{id}           │
│  │                  │         │ GET  /todos/search           │
│  │                  │         │ GET  /statistics             │
│  └──────────────────┘         └──────────────────┘            │
│                                                                 │
│  Responsibilities:                                             │
│  - Accept HTTP requests                                        │
│  - Validate input (DTOs)                                       │
│  - Call service layer                                          │
│  - Return HTTP responses                                       │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SERVICE LAYER (Business Logic)               │
│                                                                 │
│  ┌──────────────────────────┐  ┌──────────────────────────┐   │
│  │   UserService            │  │   TodoService            │   │
│  ├──────────────────────────┤  ├──────────────────────────┤   │
│  │ registerProfile()        │  │ createTodo()             │   │
│  │ authenticateAndGenerate  │  │ getAllTodos()            │   │
│  │    Token()               │  │ getTodoById()            │   │
│  │ getCurrentUser()         │  │ updateTodo()             │   │
│  │ getUserProfile()         │  │ deleteTodo()             │   │
│  │ isAccountActive()        │  │ startTodo()              │   │
│  │ toDTO() / toEntity()     │  │ completeTodo()           │   │
│  │                          │  │ reopenTodo()             │   │
│  │                          │  │ toggleFavorite()         │   │
│  │ Uses:                    │  │ searchTodos()            │   │
│  │ - PasswordEncoder        │  │ getStatistics()          │   │
│  │ - AuthenticationManager  │  │ getTodosByStatus()       │   │
│  │ - JwtUtil                │  │                          │   │
│  │                          │  │ Uses:                    │   │
│  │                          │  │ - TodoRepository         │   │
│  │                          │  │ - UserService            │   │
│  └──────────────────────────┘  └──────────────────────────┘   │
│                                                                 │
│  Responsibilities:                                             │
│  - Business logic & validation                                 │
│  - Access control (owner checks)                               │
│  - Entity ↔ DTO conversions                                    │
│  - Coordinate between repositories                             │
│  - Authentication & token generation                           │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│               REPOSITORY LAYER (Data Access)                    │
│                                                                 │
│  ┌────────────────────────┐  ┌────────────────────────┐        │
│  │ UserRepository         │  │ TodoRepository         │        │
│  ├────────────────────────┤  ├────────────────────────┤        │
│  │ findByEmail()          │  │ findByUserId()         │        │
│  │ save()                 │  │ findByUserIdAndStatus()│        │
│  │ (JPA methods)          │  │ save()                 │        │
│  │                        │  │ findById()             │        │
│  │                        │  │ delete()               │        │
│  │                        │  │ (JPA methods)          │        │
│  └────────────────────────┘  └────────────────────────┘        │
│                                                                 │
│  Responsibilities:                                             │
│  - Database queries (via JPA)                                  │
│  - No business logic                                           │
│  - Just CRUD + custom queries                                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│              DATABASE LAYER (Persistence)                       │
│                                                                 │
│  MySQL Database: localhost:3306/todo                           │
│                                                                 │
│  ┌──────────────────┐              ┌──────────────────┐        │
│  │ users table      │              │ todos table      │        │
│  ├──────────────────┤              ├──────────────────┤        │
│  │ id (PK)          │              │ id (PK)          │        │
│  │ username         │◄────────────┤│ user_id (FK) ────┤        │
│  │ email (UNIQUE)   │ (1:N)        │ title            │        │
│  │ password         │              │ description      │        │
│  │ isActive         │              │ status (ENUM)    │        │
│  │ createdAt        │              │ isFavorite       │        │
│  │ updatedAt        │              │ icon             │        │
│  │                  │              │ createdAt        │        │
│  │                  │              │ updatedAt        │        │
│  └──────────────────┘              └──────────────────┘        │
│                                                                 │
│  ORM: Hibernate/JPA                                            │
│  DDL Auto: update (auto-creates/updates tables)               │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. REQUEST FLOW - END TO END

### Example: User logs in and creates a todo

```
Step 1: LOGIN REQUEST
═══════════════════
Client: POST /api/v1.0/login
        Body: { "email": "john@example.com", "password": "pass123" }
                    │
                    ▼
        HttpSecurity allows /login without auth
                    │
                    ▼
        UserController.login(AuthDTO)
                    │
                    ▼
        UserService.authenticateAndGenerateToken()
        ├─ AuthenticationManager.authenticate(email, password)
        │  └─ Checks password against BCrypt hash
        ├─ JwtUtil.generateToken(email)
        │  └─ Creates signed JWT token
        └─ Returns { user: UserDTO, token: JWT }
                    │
                    ▼
Response: 200 OK
{
  "user": { "id": 1, "username": "john", "email": "john@example.com" },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
Client stores token in localStorage


Step 2: CREATE TODO REQUEST (Authenticated)
════════════════════════════════════════════
Client: POST /api/v1.0/todos
        Header: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
        Body: { "title": "Learn Spring", "description": "..." }
                    │
                    ▼
        [SECURITY LAYER]
        JwtRequestFilter.doFilterInternal()
        ├─ Extract token from "Bearer ..." header
        ├─ JwtUtil.validateToken(token)
        │  └─ Verify signature & expiration
        ├─ JwtUtil.extractUsername(token) → "john@example.com"
        ├─ UserDetailsService.loadUserByUsername("john@example.com")
        │  └─ Returns UserDetails with authorities
        └─ Set SecurityContextHolder with authenticated user
                    │
                    ▼
        [CONTROLLER LAYER]
        TodoController.createTodo(TodoDTO)
        ├─ Parse and validate TodoDTO
        └─ Call TodoService.createTodo()
                    │
                    ▼
        [SERVICE LAYER]
        TodoService.createTodo(TodoDTO)
        ├─ UserService.getCurrentUser()
        │  └─ Get "john" from SecurityContextHolder
        ├─ Convert TodoDTO → TodoEntity
        ├─ Set user_id = john's ID
        └─ Call TodoRepository.save(entity)
                    │
                    ▼
        [REPOSITORY LAYER]
        TodoRepository.save(TodoEntity)
        ├─ JPA converts to SQL INSERT
        └─ Execute: INSERT INTO todos (title, description, user_id, status, ...)
                    │
                    ▼
        [DATABASE LAYER]
        MySQL stores record
        ├─ id = 42 (auto-increment)
        ├─ user_id = 1 (john)
        ├─ status = "NEW" (default)
        └─ Return inserted entity with ID
                    │
                    ▼
        [SERVICE LAYER - Return]
        Convert TodoEntity → TodoDTO
        ├─ Exclude internal fields
        └─ Return clean DTO
                    │
                    ▼
        [CONTROLLER LAYER - Return]
        Return ResponseEntity
                    │
                    ▼
Response: 201 CREATED
{
  "id": 42,
  "title": "Learn Spring",
  "description": "...",
  "status": "NEW",
  "isFavorite": false,
  "createdAt": "2026-01-19T10:30:00"
}
```

---

## 3. CROSS-CUTTING CONCERNS

```
┌─────────────────────────────────────────────────────────────────┐
│                  UTILITY & HELPER CLASSES                       │
│                  (Used across layers)                           │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────┐
│ Security Components      │
├──────────────────────────┤
│ JwtUtil                  │
│ ├─ generateToken()       │
│ ├─ validateToken()       │
│ ├─ extractUsername()     │
│ └─ getExpirationDate()   │
│                          │
│ JwtRequestFilter         │
│ ├─ Extract JWT from      │
│ │  "Authorization" header│
│ ├─ Validate & Set        │
│ │  SecurityContext       │
│ └─ Run on every request  │
│                          │
│ AppUserDetailsService    │
│ └─ loadUserByUsername()  │
│    (for authentication)  │
└──────────────────────────┘

┌──────────────────────────┐
│ Configuration            │
├──────────────────────────┤
│ SecurityConfig           │
│ ├─ Define public routes  │
│ ├─ Set CORS policy       │
│ ├─ Add JWT filter        │
│ ├─ Disable CSRF          │
│ ├─ Use stateless sessions│
│ └─ Configure password    │
│    encoder (BCrypt)      │
└──────────────────────────┘

┌──────────────────────────┐
│ Data Transfer Objects    │
├──────────────────────────┤
│ UserDTO                  │
│ ├─ id, username, email   │
│ └─ NO password field     │
│                          │
│ TodoDTO                  │
│ ├─ id, title, description│
│ ├─ status, isFavorite    │
│ └─ timestamps            │
│                          │
│ AuthDTO                  │
│ ├─ email, password       │
│ └─ For login only        │
└──────────────────────────┘

┌──────────────────────────┐
│ Enums                    │
├──────────────────────────┤
│ TodoStatus               │
│ ├─ NEW                   │
│ ├─ IN_PROGRESS           │
│ └─ COMPLETED             │
└──────────────────────────┘
```

---

## 4. DEPENDENCY FLOW CHART

```
                    TodoApplication (Main)
                            │
                            ▼
                    SpringBootApplication
                    (Auto-configures everything)
                            │
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
  SecurityConfig      UserController      TodoController
        │                   │                   │
        ├─────────┬─────────┘                   │
        │         │                            │
        ▼         ▼                            ▼
  JwtRequestFilter      UserService       TodoService
        │                 │                   │
        └─────────┬───────┘                   │
                  │                           │
                  ├──────────────────────────┤
                  │                          │
                  ▼                          ▼
              JwtUtil              TodoRepository
                                        │
                  ├──────────────┬───────┘
                  │              │
                  ▼              ▼
          UserRepository    Database (MySQL)
                  │              │
                  └──────────────┘
```

---

## 5. DATA TRANSFORMATION FLOW

```
CLIENT REQUEST (JSON)
        │
        ▼
┌─────────────────────────────┐
│ Controller                  │
│ Receives: JSON              │
│ @RequestBody → TodoDTO      │
└─────────────────────────────┘
        │
        ▼
┌─────────────────────────────┐
│ Service                     │
│ TodoDTO → TodoEntity        │
│ (toEntity() method)         │
│ + Add currentUser           │
│ + Add timestamps            │
└─────────────────────────────┘
        │
        ▼
┌─────────────────────────────┐
│ Repository                  │
│ Save TodoEntity to Database │
│ (JPA/Hibernate handles SQL) │
└─────────────────────────────┘
        │
        ▼
┌─────────────────────────────┐
│ Database                    │
│ Persisted as SQL records    │
└─────────────────────────────┘
        │
        ▼ (On Read)
┌─────────────────────────────┐
│ Service                     │
│ TodoEntity → TodoDTO        │
│ (toDTO() method)            │
│ Removes passwords/internals │
└─────────────────────────────┘
        │
        ▼
┌─────────────────────────────┐
│ Controller                  │
│ TodoDTO converted to JSON   │
│ @ResponseBody               │
└─────────────────────────────┘
        │
        ▼
CLIENT RESPONSE (JSON)
```

---

## 6. KEY ARCHITECTURAL DECISIONS

| Decision | Reason |
|----------|--------|
| **Layered Architecture** | Separation of concerns, easy to test, maintainable |
| **DTOs** | Hide internal structure, exclude sensitive data (passwords) |
| **Stateless JWT** | Scalable, no server-side sessions, REST-compliant |
| **Service Layer** | Centralizes business logic, access control, validation |
| **Repository Pattern** | Decouples data access, easy to swap implementations |
| **BCrypt** | Industry-standard password hashing, salted & slow |
| **Spring Security** | Standard framework for authentication/authorization |
| **JPA/Hibernate** | Object-Relational Mapping, type-safe queries |

---

## 7. AUTHENTICATION & AUTHORIZATION

```
┌─────────────────────────────────────────┐
│         AUTHENTICATION (Who are you?)   │
├─────────────────────────────────────────┤
│                                         │
│  Login: email + password verification  │
│  ↓                                      │
│  Generate JWT token                    │
│  ↓                                      │
│  Client includes token in requests     │
│  ↓                                      │
│  JwtRequestFilter validates token      │
│  ↓                                      │
│  Set user in SecurityContext           │
│                                         │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│      AUTHORIZATION (What can you do?)   │
├─────────────────────────────────────────┤
│                                         │
│  Public endpoints (permit all):         │
│  - /register, /login, /profile          │
│                                         │
│  Protected endpoints (authenticated):   │
│  - All /todos/* endpoints               │
│                                         │
│  Owner checks (in Service):             │
│  - User can only access their own todos │
│  - If todo.user.id ≠ currentUser.id     │
│    → throw AccessDeniedException        │
│                                         │
└─────────────────────────────────────────┘
```

---

## 8. FOLDER STRUCTURE MATCHES ARCHITECTURE

```
src/main/java/neurogine/example/todo/
│
├── config/                          [CONFIGURATION LAYER]
│   └── SecurityConfig.java         (Spring Security setup)
│
├── controller/                      [CONTROLLER LAYER]
│   ├── UserController.java         (User REST endpoints)
│   └── TodoController.java         (Todo REST endpoints)
│
├── service/                         [SERVICE LAYER]
│   ├── UserService.java            (User business logic)
│   ├── TodoService.java            (Todo business logic)
│   └── AppUserDetailsService.java  (Spring Security integration)
│
├── repository/                      [REPOSITORY LAYER]
│   ├── UserRepository.java         (User data access)
│   └── TodoRepository.java         (Todo data access)
│
├── entity/                          [DATABASE ENTITIES]
│   ├── UserEntity.java
│   └── TodoEntity.java
│
├── dto/                             [DATA TRANSFER OBJECTS]
│   ├── UserDTO.java
│   ├── TodoDTO.java
│   └── AuthDTO.java
│
├── enums/                           [CONSTANTS]
│   └── TodoStatus.java
│
├── security/                        [SECURITY UTILITIES]
│   └── JwtRequestFilter.java       (JWT validation filter)
│
├── util/                            [UTILITIES]
│   └── JwtUtil.java                (JWT token management)
│
└── TodoApplication.java             [MAIN ENTRY POINT]
```

---

## 9. TYPICAL REQUEST EXECUTION PATH

```
HTTP Request
    ↓
Spring DispatcherServlet
    ↓
JwtRequestFilter (validates JWT)
    ↓
Route to correct Controller
    ↓
Controller validates input
    ↓
Service validates business rules
    ↓
Repository executes database query
    ↓
Database returns data
    ↓
Service transforms Entity → DTO
    ↓
Controller returns HTTP Response
    ↓
Client receives JSON
```

---

This layered architecture provides:
- ✅ **Separation of Concerns** - Each layer has specific responsibility
- ✅ **Testability** - Easy to unit test each layer independently
- ✅ **Maintainability** - Easy to locate and modify code
- ✅ **Scalability** - Can add features without affecting existing layers
- ✅ **Security** - Authentication & authorization properly isolated
