## Zorvyn Backend

Spring Boot REST API for user management, financial records, authentication, and dashboard analytics.

### Tech Stack

- Java 21
- Spring Boot 
- Spring Web
- Spring Data JPA
- Spring Security with JWT
- Bean Validation
- MySQL
- Maven

### Features

- JWT-based stateless authentication
- Role-based access control for `VIEWER`, `ANALYST`, and `ADMIN`
- User management APIs
- Financial record CRUD with soft delete
- Dashboard summary, trends, and category analytics
- Centralized validation and exception handling

### Prerequisites

- Java 21
- Maven 3.9+ or the included Maven Wrapper
- MySQL 8+
- Git

### Database Setup

Create a MySQL database before starting the app:

```sql
CREATE DATABASE finance_db;
```

### Environment Variables

The application reads sensitive values from environment variables. Set these before running the app:

| Variable | Required | Description |
|---|---:|---|
| `DB_URL` | No | JDBC URL for MySQL. Defaults to `jdbc:mysql://localhost:3306/finance_db` |
| `DB_USERNAME` | No | Database username. Defaults to `root` |
| `DB_PASSWORD` | Yes | Database password |
| `JWT_SECRET` | Yes | Base64-encoded secret used to sign JWTs |
| `JWT_EXPIRATION` | No | JWT expiration in milliseconds. Defaults to `86400000` |
| `APP_ADMIN_SECRET` | Yes | Bootstrap secret used for admin-related setup |

Example PowerShell setup:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/finance_db"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-db-password"
$env:JWT_SECRET = "your-base64-encoded-jwt-secret"
$env:JWT_EXPIRATION = "86400000"
$env:APP_ADMIN_SECRET = "your-admin-secret"
```

Example Bash setup:

```bash
export DB_URL="jdbc:mysql://localhost:3306/finance_db"
export DB_USERNAME="root"
export DB_PASSWORD="your-db-password"
export JWT_SECRET="your-base64-encoded-jwt-secret"
export JWT_EXPIRATION="86400000"
export APP_ADMIN_SECRET="your-admin-secret"
```

### Local Development Setup

1. Clone the repository.
2. Create the MySQL database.
3. Set the environment variables above.
4. Verify `src/main/resources/application.properties` uses placeholders and not real secrets.
5. Run the application.

### Run the Application

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Or with Maven directly:

```bash
mvn spring-boot:run
```

The API starts on port `8080` by default.

### API Overview

#### Auth

- `POST /api/auth/register`
- `POST /api/auth/login`

#### Users

- `GET /api/users/list`
- `GET /api/users/{id}`
- `POST /api/users/create`
- `PUT /api/users/{id}`
- `PATCH /api/users/{id}/status`
- `DELETE /api/users/{id}`

#### Records

- `GET /api/records`
- `POST /api/records`
- `PUT /api/records/{id}`
- `PATCH /api/records/{id}`
- `DELETE /api/records/{id}`

#### Dashboard

- `GET /api/dashboard/summary`
- `GET /api/dashboard/trends?period=daily|weekly|monthly`
- `GET /api/dashboard/categories`

### Security Notes

- Authentication is stateless and JWT-based.
- Passwords are stored as BCrypt hashes.
- Secrets should never be committed to Git.
- Use a local-only file or environment variables for machine-specific values.

### Project Structure

- `Config` - Security and Swagger configuration
- `Controller` - HTTP endpoints
- `DTO` - Request and response models
- `Entity` - JPA entities
- `Enums` - Domain enums
- `Exception` - Custom exceptions and global handler
- `Repository` - Database access
- `Security` - JWT and authentication helpers
- `Service` - Business logic

### Notes

- Dashboard aggregates exclude soft-deleted records.
- Record reads support filtering and pagination.
- Admin-only endpoints are protected with method-level security.
