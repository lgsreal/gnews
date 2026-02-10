# Testcontainers Setup & Adapter Strategy

## Overview
This project validates the Favorites feature using integration tests. Initially designed to use **Testcontainers with PostgreSQL**, the strategy was adapted to use **H2 Database** due to the absence of a Docker Runtime in the CI/CD execution environment.

## Original Architecture (Testcontainers)
The intended architecture uses `org.testcontainers:postgresql` to spin up a Docker container with PostgreSQL 16.
- **Image**: `postgres:16-alpine` (lightweight and fast).
- **Service Connection**: Uses Spring Boot 3.1+ `@ServiceConnection` to automatically inject datasource properties without manual `DynamicPropertySource` configuration.
- **Wait Strategy**: Default strategy (listening on port 5432).

## Adaptation (H2 Fallback)
Due to `RunTimeException: Could not find a valid Docker environment`, `Testcontainers` annotations were commented out, allowing Spring Boot to fall back to the in-memory H2 database configured in `pom.xml`.

### Enabling Testcontainers
To enable PostgreSQL testing on an environment with Docker:
1. Ensure Docker Desktop or Engine is running.
2. Uncomment `@Testcontainers`, `@Container`, and `@ServiceConnection` in `FavoriteIntegrationTest.java`.
3. Verify `mvn test` uses the containerized database.

## Dependencies
- `org.testcontainers:junit-jupiter`
- `org.testcontainers:postgresql`
- `org.springframework.boot:spring-boot-testcontainers`
