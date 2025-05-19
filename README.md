# OpenPay UPI Gateway

A modular, high-performance UPI payment gateway system built for reliability, compliance, and scalability.

## Phase 1: Database Component

- **Database:** PostgreSQL 16 (migrated from Oracle for better containerization)
- **Schema Management:** Flyway SQL migrations
- **DB GUI:** DBeaver
- **Tested locally on Windows (Native, not WSL/Docker)**

### Core Tables

- `transactions` — Main ledger for all UPI transactions
- `transaction_history` — Full audit log of transaction state changes
- `idempotency_keys` — Prevents duplicate processing for API requests

### Status

- ✅ Database schema designed, migrated, and verified
- ✅ Constraints and integrity checks enforced
- ✅ Test data persisted and validated
- ✅ Version-controlled via git

---

## Quickstart

**To set up the database locally:**

1. Ensure PostgreSQL 16 is installed and running.
2. Create the database and user:
   ```sql
   CREATE DATABASE openpay_db;
   CREATE USER openpay_user WITH ENCRYPTED PASSWORD 'openpay_pass';
   GRANT ALL PRIVILEGES ON DATABASE openpay_db TO openpay_user;
   ```
3. Apply migrations in `database/migrations/` via DBeaver or psql.
4. Test inserts/selects to verify schema and constraints.

---
