# OpenPay UPI Gateway

A modular, high-performance UPI payment gateway system built for reliability, compliance, and scalability.

---

## ✅ Phase 1: Database Component

* **Database:** PostgreSQL 16
* **Schema Management:** Flyway SQL migrations
* **DB GUI:** DBeaver
* **Testing:** Verified locally on Windows (Native)

### Core Tables

* **`transactions`**: Main ledger for all UPI transactions
* **`transaction_history`**: Audit log of all transaction state changes
* **`idempotency_keys`**: Prevents duplicate processing of API requests

### Status

* ✅ Database schema fully designed, migrated, and validated
* ✅ Integrity constraints rigorously enforced
* ✅ Test data successfully inserted and verified
* ✅ Version-controlled in Git

---

## ✅ Phase 2: API Service (Spring Boot)

* **Framework:** Spring Boot 3.x
* **Language & Build Tool:** Java 21, Maven
* **Validation:** Jakarta Validation API (with custom constraints)
* **Error Handling:** Global exception handling with structured responses

### APIs Implemented

* **`POST /pay`**: Initiate a new transaction
* **`GET /transaction/{id}/status`**: Retrieve the status of a transaction

### Status

* ✅ API service fully implemented and integrated with the database
* ✅ Complete DTO validation, including custom UPI ID validations
* ✅ Robust global error handling
* ✅ Local API and database testing completed successfully
* ✅ Version-controlled and merged into main branch

---

## 🚀 Next Steps: Shared Utilities (In Progress)

**Shared Libraries & Utilities:**

* Common DTOs, validation logic, and exception handling
* Idempotency handling service (in-memory & Redis fallback)
* Enhanced logging (SLF4J/Logback with MDC context)
* Basic health check component (`/health` endpoint)

### Status

* ⏳ In active development
* 📋 Tasks clearly outlined and being executed

---

## 🛠 Quickstart Guide

**Local Database Setup:**

1. **Install PostgreSQL 16** locally.
2. **Create database and user:**

   ```sql
   CREATE DATABASE openpay_db;
   CREATE USER openpay_user WITH ENCRYPTED PASSWORD 'openpay_pass';
   GRANT ALL PRIVILEGES ON DATABASE openpay_db TO openpay_user;
   ```
3. **Run migrations**:

   * Use Flyway (`mvn flyway:migrate`) or apply via DBeaver/psql.
4. **Verify schema** by inserting/selecting test data.

**Local API Service Setup:**

1. **Clone the repository:**

   ```shell
   git clone https://github.com/gracemann365/openpay.git
   ```
2. **Run API service:**

   ```shell
   cd api-service
   mvn spring-boot:run
   ```
3. **Test endpoints via `curl`:**

   ```shell
   curl -X POST http://localhost:8080/pay -H "Content-Type: application/json" \
   -d '{"senderUpi":"alice@upi","receiverUpi":"bob@upi","amount":100.25}'

   curl http://localhost:8080/transaction/1/status
   ```

---

## 🎯 Upcoming Modules

* **Redis Integration:** Memurai-based Redis queues
* **Worker Service:** Async transaction processing
* **Full Native E2E Test:** Complete local system integration and validation

---
