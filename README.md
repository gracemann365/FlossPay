# OpenPay UPI Gateway

**OpenPay UPI Gateway** is a modular, high-performance payment system designed for seamless, compliant, and scalable UPI transaction processing in Java/Spring Boot.

---

## Overview

OpenPay is architected for real-world reliability and modularity, featuring:

- Clear separation of API, worker, and shared libraries
- Database migration/versioning with Flyway
- Native local development (no Docker required)
- Designed for rapid extensibility and enterprise compliance

---

## System Architecture

- **Database:** PostgreSQL 16 (with Flyway migrations)
- **API Service:** Java 21, Spring Boot 3.x (RESTful, validation, error handling)
- **Worker Service:** Asynchronous Redis-based transaction processor (in development)
- **Shared Libraries:** DTOs, exceptions, validation, idempotency logic

```

+-----------+      +-------------------+      +-------------------+
\|  Client   | ---> |   API Service     | ---> |   Database        |
\|  (curl)   |      | (Spring Boot)     |      |  (PostgreSQL)     |
+-----------+      +-------------------+      +-------------------+
|
v
+-------------------+
\| Worker Service    |
\| (Async, Redis)    |
+-------------------+

```

---

## Core Components

### Database Schema

- **transactions:** Main UPI transaction ledger
- **transaction_history:** Audit trail for all status/state changes
- **idempotency_keys:** Ensures safe, duplicate-free processing

### Key APIs

- `POST /pay` — Initiate a new payment transaction
- `GET /transaction/{id}/status` — Retrieve transaction status by ID

---

## Quickstart Guide

### Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 16

### Database Setup

```sql
CREATE DATABASE openpay_db;
CREATE USER openpay_user WITH ENCRYPTED PASSWORD 'openpay_pass';
GRANT ALL PRIVILEGES ON DATABASE openpay_db TO openpay_user;
```

Run migrations (from the project root):

```sh
mvn -pl api-service flyway:migrate
```

### Running the API Service

```sh
cd api-service
mvn spring-boot:run
```

Test the endpoints (with sample data):

```sh
curl -X POST http://localhost:8080/pay -H "Content-Type: application/json" \
     -d '{"senderUpi":"alice@upi","receiverUpi":"bob@upi","amount":100.25}'

curl http://localhost:8080/transaction/1/status
```

---

## Roadmap

- **Worker Service** (Async Redis queue consumer)
- **Advanced Testing:** Full local E2E, integration, and audit
- **Monitoring/Health:** System and API health checks
- **Production hardening:** Security, error handling, deployment docs

---

## Contributing

1. Fork this repo and clone your fork.
2. Open a feature branch (`feature/your-feature`) and commit descriptive messages.
3. Run tests and ensure your code follows the established structure and documentation.
4. Open a pull request; include context on your changes.

---

## License

Distributed under the MIT License.

---

**Maintainer:** David Grace
[GitHub: gracemann365](https://github.com/gracemann365)
