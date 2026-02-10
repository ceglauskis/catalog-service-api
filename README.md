# Catalog Service API

Backend REST API built with Spring Boot, focused on clean architecture, testability and real-world backend practices.

This project models a product catalog domain and is being evolved incrementally with enterprise-grade concerns such as security, testing, performance and maintainability.

---

## Overview

The Catalog Service API provides CRUD operations for products and categories, following layered architecture and RESTful principles.

The project is structured to reflect real backend systems, prioritizing:
- Clear separation of concerns
- Business-driven services
- Consistent error handling
- Automated testing

---

## Current Features

- CRUD operations for Products and Categories
- Pagination and sorting with Spring Data
- H2 in-memory database for development and testing
- Layered architecture (Controller / Service / Repository)
- DTO pattern for API boundaries
- Custom exception handling
- Unit tests and web layer tests
- Integration tests (in progress)

---

## Architecture & Design Decisions

### Layered Architecture
The project follows a classic layered structure:
- **Resources (Controllers):** HTTP layer and request/response mapping
- **Services:** Business logic and transactional boundaries
- **Repositories:** Data access using Spring Data JPA
- **DTOs:** Decoupling domain entities from API contracts

This approach improves maintainability, testability and clarity.

---

### Transactions & Consistency
- Read operations use `@Transactional(readOnly = true)`
- Write operations are transactional by default
- Delete operations use `Propagation.SUPPORTS` with existence checks to ensure predictable behavior

---

### Exception Handling
Domain-specific exceptions are used to represent business and persistence errors:
- `ResourceNotFoundException`
- `DatabaseException`

This allows consistent API responses and better error semantics.

---

### Testing Strategy
The project adopts a multi-level testing approach:
- **Unit tests:** Services and business logic
- **Web tests:** Controller layer validation
- **Integration tests:** Full context validation with database

This ensures confidence in refactoring and future evolution.

---

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA / Hibernate
- H2 Database
- JUnit 5
- Mockito / MockBean
- MockMvc

---

## Roadmap

The project is actively evolving and the following improvements are planned:

### Security
- [ ] OAuth2 authentication with JWT (stateless security)
- [ ] Role-based access control for protected endpoints

### Business Use Cases
- [ ] User signup and onboarding flow
- [ ] Domain-driven use cases with validation and transactional consistency

### Persistence & Performance
- [ ] Query optimization using `JOIN FETCH` to prevent N+1 problems
- [ ] Database migration to PostgreSQL (production-like setup)

### Testing & Quality
- [ ] API tests using RestAssured
- [ ] Code coverage analysis with JaCoCo
- [ ] Improved integration test coverage

---

## Project Status

This project is under active development and continuously evolving as part of a backend engineering portfolio.
