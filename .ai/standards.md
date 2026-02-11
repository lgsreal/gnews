# Coding Standards

## General
- **Java Version**: 17 (Minimum)
- **Code Style**: Clean code, readability > complex abstractions.
- **Testing**: Desirable but not mandatory. If implementing, use JUnit 5.

## Java Language Features
- **DTOs**: MUST use `record` for all immutable Data Transfer Objects.
- **Optionals**: Use `Optional` ONLY for return types of methods that might produce no result. NEVER use `Optional` in fields or method parameters.
- **Streams**: Use Java Stream API for all collection filtering, mapping, and sorting operations. No manual loops for these tasks.

## Application Structure (Layered)
- `controller`: Thin layer. Handles HTTP request/response only. Validates input. Delegates to Service.
- `service`: Contains ALL business logic.
- `repository`: Data access layer (in-memory simulation).
- `domain`: Domain models (POJOs/Records).
- `dto`: Request and Response records.
- `config`: Spring configuration classes.
- `error`: Global exception handling and error responses.

## REST Conventions
- **Response Format**: Consistent JSON envelopes/schemas for all endpoints.
- **Validation**: MUST use `jakarta.validation` annotations (`@NotNull`, `@Size`, etc.) on DTOs.
- **HTTP Methods**: Proper use of GET for retrieval, logic in Service layer.
