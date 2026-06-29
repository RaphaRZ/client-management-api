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

## Code Requirements

The code should follow good development practices, including:

* Clear separation of responsibilities (`controller`, `service`, `repository`)
* DTOs for request and response objects
* Proper exception handling
* Use of Lombok
* Docker for PostgreSQL setup
* Automated tests
* Swagger/OpenAPI documentation

## Business Rules
* Clients must have a first name, last name, and document. Contacts are optional
* The document must be unique in the database. The system does not allow two clients with the same document
* A client can have multiple contacts
* Every contact must belong to exactly one client
* When a client is deleted, all of their associated contacts must also be deleted

## License

This project is licensed under the MIT License. See the `LICENSE` file for more information.
