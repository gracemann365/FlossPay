-- V1__init.sql
-- Core tables for OpenPay UPI Gateway
-- Includes main transaction ledger, audit trail, and idempotency tracking

-- =======================================
-- Table: transactions
-- Main ledger for all UPI transactions
-- =======================================
CREATE TABLE transactions (
  transaction_id    VARCHAR(36)    PRIMARY KEY,
  type              VARCHAR(10)    NOT NULL CHECK (type IN ('PAY','COLLECT')),
  amount            NUMERIC(15,2)  NOT NULL,
  sender            VARCHAR(15)    NOT NULL,
  recipient         VARCHAR(15)    NOT NULL,
  status            VARCHAR(10)    NOT NULL,
  created_at        TIMESTAMPTZ    DEFAULT NOW(),
  updated_at        TIMESTAMPTZ
);

CREATE INDEX idx_tx_status_created ON transactions(status, created_at);

-- =======================================
-- Table: transaction_history
-- Audit trail for all transaction state changes
-- =======================================
CREATE TABLE transaction_history (
  history_id        BIGSERIAL PRIMARY KEY,
  transaction_id    VARCHAR(36)    NOT NULL,
  prev_status       VARCHAR(10)    NOT NULL,
  new_status        VARCHAR(10)    NOT NULL,
  changed_at        TIMESTAMPTZ    DEFAULT NOW(),
  CONSTRAINT fk_history_tx FOREIGN KEY(transaction_id) REFERENCES transactions(transaction_id)
);

-- =======================================
-- Table: idempotency_keys
-- Tracks idempotency for API requests to prevent duplicates
-- =======================================
CREATE TABLE idempotency_keys (
  idempotency_key   VARCHAR(64)    PRIMARY KEY,
  transaction_id    VARCHAR(36)    NOT NULL,
  created_at        TIMESTAMPTZ    DEFAULT NOW(),
  CONSTRAINT fk_idem_tx FOREIGN KEY(transaction_id) REFERENCES transactions(transaction_id)
);

-- - [x]  Migrations applied and verified
-- - [x]  Constraints enforced (proven by tests)
-- - [x]  Manual test data inserted and persisted

-- created  feature/database-component branch
-- PR -> reviewed -> merged into main 
--> Database component is ready for use in the project
