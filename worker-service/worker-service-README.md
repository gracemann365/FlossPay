# OpenPay UPI Gateway — Worker Service

---

## 📦 Module Overview

**`worker-service/`**  
This module implements the background worker responsible for processing queued payment transactions, updating statuses in the database, and integrating with UPI/NPCI payment rails (simulated).

- **Type:** Headless Spring Boot application (no public HTTP endpoints by default)
- **Role:** Consumes jobs from Redis Stream (`transactions.main`), updates transaction status, and performs payment network calls.

---

## 🛠️ Responsibilities

- **Polls** the Redis Stream for new transaction jobs (`transactions.main`)
- **Updates** transaction status in PostgreSQL (e.g., queued → processing → completed/failed)
- **Integrates** with the NPCI/UPI payment gateway (simulated via `NpciUpiGatewayClient`)
- **Logs** all processing steps for observability and debugging
- **Extensible:** Easily supports retries, DLQ, metrics, circuit breaking, and more

---

## 🗂️ Package Structure

```text
worker-service/
│
├── client/
│   └── NpciUpiGatewayClient.java     # Interface for payment network calls
├── config/
│   └── RedisConfig.java              # RedisTemplate and bean configuration
├── model/
│   └── TransactionEntity.java  # DB entity for transactions
├── processor/
│   └── TransactionWorkerConsumer.java # Main background worker/consumer
├── repository/
│   └── TransactionRepository.java # JPA repository for transactions
└── WorkerApplication.java            # Main Spring Boot entrypoint
```

````

---

## 🚀 How to Run

```sh
# Prerequisites: Java 21+, PostgreSQL, Redis running, Flyway migrations applied
cd worker-service
./mvnw clean package
java -jar target/worker-service-*.jar
```

- **Configuration:** Edit `src/main/resources/application.yml` for DB/Redis URLs and credentials
- **Environment:** Defaults to port 8081 (change in `application.yml` if you enable endpoints)

---

## 🔄 Integration Points

- **Redis Stream:** Reads from `transactions.main` (populated by api-service)
- **Database:** Updates the `transactions` table (shared with api-service)
- **Client Integration:** Simulates real UPI/NPCI payments; replace `NpciUpiGatewayClient` with live implementation for prod

---

## 🔒 Security & Ops

- **No HTTP API exposed by default** (can be enabled for health/probes if needed)
- **Do not** use default DB/Redis credentials in production!
- **Log aggregation:** All status changes and errors are logged (configure via `application.yml` and logging backend)
- **Circuit breakers and rate limiting** can be enabled using the database tables defined in `/database/db/migration`

---

## 👤 Ownership & Contact

- **Primary Owner:** David Grace
- **Contact:** \[Your Email or Slack]
- **Last Updated:** 2024-06-01

---

## 🧪 Testing & Extensibility

- Unit/integration tests should cover:

  - Stream polling logic
  - Transaction status transitions
  - UPI/NPCI client calls (mocked)

- For reliability: Add retry, DLQ, and circuit breaker logic as your scale grows

---

## 📚 Related Modules

- [`api-service`](../api-service): Public REST API, transaction producer
- [`shared-libs`](../shared-libs): DTOs, exceptions, validations
- [`database`](../database): Migration scripts, schema docs

---
````
