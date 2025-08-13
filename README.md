# API Gateway with Authentication & Authorization

This Spring Boot application serves as an API Gateway with JWT-based authentication and role-based authorization.

## Features

- **JWT Authentication**: Secure token-based authentication
- **Role-based Authorization**: Support for USER, MODERATOR, and ADMIN roles
- **API Gateway**: Proxy requests to backend services with user context
- **User Management**: Registration and login endpoints
- **Security**: All gateway endpoints are protected and require authentication

## Default Users

The application comes with pre-configured users:

| Username  | Password | Roles           | Description                    |
|-----------|----------|-----------------|--------------------------------|
| admin     | admin123 | ADMIN, USER     | Full access to all endpoints   |
| user      | user123  | USER            | Basic user access              |
| moderator | mod123   | MODERATOR, USER | Moderation and user privileges |

## API Endpoints

### Authentication Endpoints (Public)

- `POST /api/auth/signin` - Login with username/password
- `POST /api/auth/signup` - Register a new user

### Test Endpoints

- `GET /api/test/public` - Public endpoint (no auth required)
- `GET /api/test/user` - Requires USER role or higher
- `GET /api/test/moderator` - Requires MODERATOR role or higher  
- `GET /api/test/admin` - Requires ADMIN role

### Gateway Endpoints (Authentication Required)

- `GET /api/gateway/health` - Gateway health check
- `ALL /api/gateway/**` - Proxy all requests to backend service

#### Authorization Rules for Gateway:
- `GET` requests: Require USER role or higher
- `POST` requests: Require USER role or higher
- `PUT` requests: Require ADMIN role
- `DELETE` requests: Require ADMIN role

## Usage Examples

### 1. Login
```bash
curl -X POST http://localhost:8082/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "roles": ["ROLE_ADMIN", "ROLE_USER"]
}
```

### 2. Access Protected Endpoint
```bash
curl -X GET http://localhost:8082/api/test/admin \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. Use Gateway to Proxy Request
```bash
curl -X GET http://localhost:8082/api/gateway/ \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Register New User
```bash
curl -X POST http://localhost:8082/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123",
    "roles": ["USER"]
  }'
```

## Configuration

The application uses the following configuration in `application.properties`:

- **Server Port**: 8082
- **JWT Secret**: Configurable secret key for token signing
- **JWT Expiration**: 24 hours (86400000 ms)
- **Database**: H2 in-memory database
- **Target Service**: Configurable backend service URL

## Backend Service Integration

The gateway forwards requests to the configured backend service (`target.service.url`) and adds the following headers:

- `X-User-Name`: Username of the authenticated user
- `X-Gateway-Forwarded`: "true" to indicate the request came through the gateway

## Security Features

1. **JWT Tokens**: Stateless authentication using JSON Web Tokens
2. **Password Encryption**: BCrypt password hashing
3. **Role-based Access Control**: Fine-grained authorization using Spring Security
4. **CORS Support**: Configurable cross-origin resource sharing
5. **Request Validation**: Input validation for registration and login

## Development

### Build and Run
```bash
mvn clean compile
mvn spring-boot:run
```

### Database Console
Access H2 console at: http://localhost:8082/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

### Docker
```bash
# Build
docker build -t api-gateway .

# Run
docker run -p 8082:8082 api-gateway
```

## Architecture

```
Client Request → API Gateway → Authentication Check → Authorization Check → Backend Service
                     ↓                    ↓                    ↓
                JWT Validation     Role Verification    Add User Context
```

The API Gateway acts as a single entry point for all client requests, handling authentication, authorization, and request forwarding to backend services.
