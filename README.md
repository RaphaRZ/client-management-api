# Client Management API

## Overview

REST API for managing clients and their contacts, allowing creation, retrieval, updating, and deletion of client information and associated contacts.

Each client can have multiple contacts, while each contact belongs to exactly one client.

## Technologies

- Java 25
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok
- Jakarta Validation
- Docker
- Swagger/OpenAPI
- JUnit 5
- Mockito
- MockMvc

## API Endpoints

### Clients

| Method | Endpoint | Description |
|---|---|---|
| POST | `/clients` | Create a client |
| GET | `/clients` | List all clients with contacts |
| GET | `/clients/{id}` | Get a client by ID |
| GET | `/clients/{id}/contacts` | List contacts from a client |
| PUT | `/clients/{id}` | Update client information |
| DELETE | `/clients/{id}` | Delete a client and its contacts |

### Contacts

| Method | Endpoint | Description |
|---|---|---|
| POST | `/contacts` | Create a contact associated with a client |
| PUT | `/contacts/{id}` | Update a contact |
| DELETE | `/contacts/{id}` | Delete a contact |

## Code Structure

The application follows a layered architecture:

- **Controller:** Handles HTTP requests and responses
- **Service:** Contains business rules and application logic
- **Repository:** Responsible for database access
- **DTOs:** Separate API contracts from persistence entities
- **Mapper:** Handles entity and DTO conversions

## Business Rules

- Clients must have a first name, last name, and document.
- Contacts are optional when creating a client.
- Client documents must be unique.
- A client can have multiple contacts.
- Each contact belongs to exactly one client.
- Deleting a client also deletes all associated contacts.

## Testing

The project includes automated tests covering:

- Service layer unit tests using JUnit 5 and Mockito.
- Web layer tests using MockMvc.
- Validation scenarios.
- Exception handling scenarios.
- HTTP status contract validation.

## Documentation

API documentation is available through Swagger/OpenAPI.

## Database

The application uses PostgreSQL.

A Docker environment is provided to simplify local database setup.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more information.
