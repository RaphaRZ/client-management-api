## Overview

REST API for managing clients and their contacts. Each client can have one or more associated contacts.

## Technical Requirements

The application must provide:

* Create a client: `POST /clients`
* Create a contact associated with an existing client: `POST /contacts`
* List all clients with their contacts: `GET /clients`
* List contacts for a specific client: `GET /clients/{id}/contacts`
* Spring Boot + Spring Data JPA
* PostgreSQL database
* `Client` and `Contact` entities with `@OneToMany` / `@ManyToOne` relationship

## Code Requirements

The code should follow good development practices, including:

* Clear separation of responsibilities (`controller`, `service`, `repository`)
* DTOs for request and response objects
* Proper exception handling
* Use of Lombok
* Docker for PostgreSQL setup
* Automated tests
* Swagger/OpenAPI documentation

## License

This project is licensed under the MIT License. See the `LICENSE` file for more information.
