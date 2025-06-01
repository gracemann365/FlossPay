

# 🚀 **OpenPay UPI Gateway**

A modular, ultra-reliable UPI payment platform—**Spring Boot, PostgreSQL, Redis, and bulletproof design**.
*For engineers who care about code quality, compliance, and extensibility.*

---

## 🏗️ **Project at a Glance**

| Service            | Tech Stack                          | Status        | Description                                                |
| ------------------ | ----------------------------------- | ------------- | ---------------------------------------------------------- |
| **API Service**    | Java 21, Spring Boot 3.x            | ✅ Audit Ready | REST endpoints, request validation, business logic         |
| **Worker Service** | Java 21, Spring Boot, Redis Streams | ✅ Audit Ready | Async background processing, idempotency, error resilience |
| **Database**       | PostgreSQL 16, Flyway               | ✅ Audit Ready | Transactional ledger, audit trail, migrations              |
| **Shared Libs**    | DTOs, Exceptions, Validation        | ✅ Audit Ready | Common contracts, reusable logic                           |

---

## 🔥 **Why OpenPay?**

* **Enterprise-Ready**: Every module, method, and migration is documented, E2E tested, and ready for onboarding or compliance review.
* **Real UPI Principles**: Models true payment gateway flows—no toy examples, all core infra is modular and auditable.
* **Native-First Dev**: No Docker, no cloud lock-in—run everything on your laptop, debug in seconds.
* **Built for Handover**: All code, migrations, and docs are written so you can onboard a new team in under 30 minutes.

---

## ⚡️ **Architecture Overview**

```
+---------+    +-------------------+     +--------------------+
| Client  |--->|  API Service      |<--->|  Database (PGSQL)  |
| (curl)  |    | (Spring Boot)     |     | (Transactions,     |
+---------+    +-------------------+     |  Idempotency,      |
                   |                     |  Audit Trail)      |
                   v                     +--------------------+
            +-------------------+
            | Worker Service    |
            | (Async, Redis)    |
            +-------------------+
```

---

## 🔑 **Key Components & Flows**

### Data Model

* **transactions**: UPI payments ledger, single row per payment
* **transaction\_history**: Status/audit trail for every change
* **idempotency\_keys**: Guarantees once-only transaction execution

### REST APIs

* `POST /pay` — Initiate a new payment (queues job)
* `GET /transaction/{id}/status` — Poll status

### Async Processing

* **API** writes job → **Redis Stream** → **Worker** picks up and processes → **DB** updated

---

## 🚀 **Get Started (Local Quickstart)**

### 1. Prerequisites

* Java 21+
* Maven 3.9+
* PostgreSQL 16+
* Redis (Memurai/Redis 6+ recommended for Windows)

### 2. Database Setup

```sql
CREATE DATABASE openpay_db;
CREATE USER openpay_user WITH ENCRYPTED PASSWORD 'openpay_pass';
GRANT ALL PRIVILEGES ON DATABASE openpay_db TO openpay_user;
```

Run migrations:

```sh
mvn -pl api-service flyway:migrate
```

### 3. Run Services

```sh
cd api-service
mvn spring-boot:run
# In a second terminal:
cd ../worker-service
mvn spring-boot:run
```

### 4. Test API

```sh
curl -X POST http://localhost:8080/pay -H "Content-Type: application/json" \
     -d '{"senderUpi":"alice@upi","receiverUpi":"bob@upi","amount":100.25}'

curl http://localhost:8080/transaction/1/status
```

---

## 🧪 **Dev/Test Checklist**

* [x] E2E tested: `/pay` → Redis → Worker → DB
* [x] Flyway migrations: Clean schema, audit-trail ready
* [x] All contracts and exceptions documented (JavaDoc)
* [x] Ready for new features, onboarding, and production hardening

---

## 🗺 **Roadmap**

* [ ] Full transaction audit-trail APIs
* [ ] Circuit breakers & rate limiters
* [ ] Advanced monitoring & observability
* [ ] Cloud deploy/Docker guide (optional)
* [ ] Security hardening (PCI, sensitive data redaction)

---

## 🤝 **Contributing**

1. Fork and clone the repo
2. Branch: `feature/your-feature`
3. Keep code, docs, and commit messages clean
4. Open a PR, include context and testing evidence

---

## 👤 **Maintainer**

* David Grace ([gracemann365 on GitHub](https://github.com/gracemann365))

---

## 📄 **License**

MIT

---

## 💬 **For Questions or Onboarding**

Open an issue or reach out on GitHub—full handoff docs available on request.

---

