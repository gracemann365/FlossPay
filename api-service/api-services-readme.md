# OpenPay API Service

This is the **API Service** module for OpenPay UPI Gateway—  
the public-facing REST layer that powers payment initiation, status queries, and integration with the rest of the platform.

---

## Directory Structure

<pre>
api-service/
├── src/
│   ├── main/
│   │   ├── java/com/openpay/apiservice/
│   │   │   ├── config/        # Service, DB, Redis, and validation config classes
│   │   │   ├── controller/    # REST API controllers (@RestController)
│   │   │   ├── handler/       # Global exception & response handlers
│   │   │   ├── model/         # JPA entity models (DB mapping)
│   │   │   ├── repository/    # Spring Data JPA repositories (DAO layer)
│   │   │   ├── service/       # Core business/service layer logic
│   │   │   ├── util/          # Local utility/helper classes
│   │   │   └── Application.java # Main entry point for Spring Boot
│   │   └── resources/
│   │       ├── db/               # Flyway DB migration scripts
│   │       ├── application.yml   # Main service configuration
│   │       └── application.properties # (Optional: legacy/property config)
│   └── test/java/com/openpay/apiservice/ # Unit/integration tests
├── pom.xml         # Module-level Maven configuration
</pre>

---

## What Does This Module Do?

- **Exposes all public OpenPay APIs**: Receives, validates, and processes payment requests and transaction status queries.
- **Performs all request validation**: Uses both standard (Jakarta) and custom validation to ensure safe data.
- **Handles database writes/reads**: Uses JPA repositories to persist and query UPI transactions.
- **Publishes jobs to async queues**: Pushes payment jobs to Redis streams for downstream worker processing.
- **Centralizes error handling**: Uniform API error responses with global exception management.
- **Runs Flyway DB migrations**: Automatically applies schema changes on startup.
- **Is fully testable and locally runnable**: Just `mvn spring-boot:run` to launch.

---

## Key Packages Explained

- **controller/**  
  Hosts all REST API endpoints (`/pay`, `/transaction/{id}/status`, etc.).  
  Every incoming HTTP request starts here.

- **service/**  
  Implements the main business logic for payments, status checks, and data transformations.

- **model/**  
  Contains all JPA entity definitions, mapping the Java domain model to database tables.

- **repository/**  
  Provides the DAO/data-access layer, abstracting database CRUD/query operations via Spring Data.

- **handler/**  
  Defines global exception handlers and response wrappers for consistent API output and error reporting.

- **config/**  
  Bean and infrastructure configuration (DB, Redis, validation, etc.), centralized for clarity and testing.

- **util/**  
  Utility/helper functions—keep generic helpers here for code clarity.

---

## How This Fits In

This API service is the **entry point for all UPI transactions** in OpenPay.  
It handles request validation, security, database interaction, and offloads async work to Redis for worker consumption.

For architecture, see the root [README.md](../README.md).

---

## Quickstart

```sh
# Start the API service (after DB setup and migrations)
mvn spring-boot:run

# Try a payment (replace fields as needed)
curl -X POST http://localhost:8080/pay -H "Content-Type: application/json" \
     -d '{"senderUpi":"alice@upi","receiverUpi":"bob@upi","amount":100.25}'
