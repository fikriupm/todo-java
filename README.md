# Todo Application

A RESTful Todo API built with Spring Boot, featuring JWT authentication, user management, and comprehensive todo operations.

## Features

- 🔐 JWT-based authentication
- 👤 User registration and login
- ✅ CRUD operations for todos
- 📊 Todo status management (NEW, IN_PROGRESS, COMPLETED)
- ⭐ Favorite todos
- 🔍 Search functionality
- 📈 Statistics dashboard

## Tech Stack

- **Framework:** Spring Boot 4.0.1
- **Security:** Spring Security + JWT
- **Database:** MySQL + Spring Data JPA
- **Build Tool:** Maven
- **Java Version:** 17+

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL database

### Configuration

1. Create a MySQL database
2. Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

```bash
# Unix/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The application will start at `http://localhost:8080`


## Todo Status Flow

```
NEW → IN_PROGRESS → COMPLETED
 ↑__________________________|
        (reopen)
```

### Status Enum
- `NEW` - Just created, not started
- `IN_PROGRESS` - Currently working on it
- `COMPLETED` - Finished

## Documentation

For detailed API documentation, see [HELP.md](HELP.md)

## License

This project is for educational purposes.
