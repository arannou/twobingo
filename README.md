# Twobingo

Twobingo is a Spring Boot backend for a bingo-style grid application that manages grids, users, cells, and participation relationships.

## Project Overview

This repository contains the backend service for Twobingo, including:
- REST API controllers
- Service layer for business logic
- JPA repositories for data access
- Domain models for `Grid`, `User`, `Cell`, and `Participation`

## Project Structure

```
src/
├── main/
│   ├── java/com/twobingo/
│   │   ├── app/               # Main Spring Boot application
│   │   ├── controller/        # REST API controllers
│   │   ├── model/             # JPA entity models
│   │   ├── repository/        # Spring Data JPA repositories
│   │   └── service/           # Business logic services
│   └── resources/
│       └── application.properties # Application configuration
└── test/                      # Unit tests
```

## Technology Stack

- Java 25
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- PostgreSQL
- H2 (optional for local testing)
- Maven

## Core Domain Models

### Grid
- `id` — primary key
- `name` — grid name
- `description` — grid description
- `size` — size of the grid
- `style` — display style
- `color` — grid color
- `isArchived` — archive status
- `creator_id` — reference to the `User` who created the grid
- `createdAt` — creation timestamp

### User
- `id` — primary key
- `username` — user name
- `avatarImagePath` — optional avatar path
- `password` — hashed password
- `createdAt` — creation timestamp

### Cell
- `id` — primary key
- `grid_id` — reference to the grid
- `content` — cell text
- `isChecked` — whether the cell is marked
- `createdAt` — creation timestamp

### Participation
- `id` — primary key
- `grid` — grid reference
- `user` — user reference
- `pseudo` — participant pseudonym
- `color` — participant color
- `isApproved` — approval status
- `createdAt` — creation timestamp

## API Endpoints

### Health Check

```
GET /api/twobingo/health
```

### User Endpoints

```
GET /api/twobingo/users
GET /api/twobingo/users/{id}
GET /api/twobingo/users/username/{username}
POST /api/twobingo/users
PUT /api/twobingo/users/{id}
DELETE /api/twobingo/users/{id}
GET /api/twobingo/users/{userId}/created-grids
GET /api/twobingo/users/{userId}/participating-grids
```

### Grid Endpoints

```
GET /api/twobingo/grids
GET /api/twobingo/grids/{id}
POST /api/twobingo/grids
PUT /api/twobingo/grids/{id}
DELETE /api/twobingo/grids/{id}
GET /api/twobingo/grids/{gridId}/cells
GET /api/twobingo/grids/{gridId}/users
```

## Getting Started

### Prerequisites

- Java 25 or higher
- Maven 3.6+
- PostgreSQL

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/twobingo-1.0.0.jar
```

The backend runs by default on `http://localhost:8081`.

## Database Configuration

### PostgreSQL (Default)

The application is configured in `src/main/resources/application.properties` to use PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/twobingo
spring.datasource.username=postgres
spring.datasource.password=1234
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

Create the database before running the app:

```sql
CREATE DATABASE twobingo;
```

### Optional H2 for Local Testing

To use H2 instead, uncomment or replace the datasource settings in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

Enable the H2 console at `/h2-console` if needed.

## Tests

Run unit tests with:

```bash
mvn test
```

## Build for Production

```bash
mvn clean package -DskipTests
```

This produces an executable JAR in the `target/` folder.

## Notes

- The backend uses standard Spring Boot layers: controller → service → repository.
- User and grid relationships are managed through `Participation` entities.
- The README focuses on backend usage; frontend files are located in the `frontend/` directory.


1. **Hot Reload**: Press `Ctrl+Shift+F9` in IntelliJ or use DevTools to reload the application
2. **Database Initialization**: Hibernate can auto-create tables with `spring.jpa.hibernate.ddl-auto=update`
3. **Logging**: Adjust logging levels in `application.properties` for debugging

## Technologies Used

- **Spring Boot 3.5.13**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL** (Default database)
- **H2 Database** (Optional for testing)
- **Spring Web**
- **Maven**

## Future Enhancements

- Add authentication and authorization
- Implement pagination and sorting
- Add API documentation (Swagger/OpenAPI)
- Add caching layer (Redis)
- Add batch operations
- Add audit logging
- Add transaction management
- Add validation groups
- Add DTO pattern for API responses
- Add comprehensive unit and integration tests

## License

This project is open-source and available under the MIT License.

## Support

For issues or questions, please create an issue in the repository.
