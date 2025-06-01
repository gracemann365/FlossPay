-- ============================================================================
-- OpenPay Database Schema (Flyway Migration Example)
-- ============================================================================

-- ============================================================================
-- transactions: Main payment transaction table
-- ============================================================================
CREATE TABLE transactions (
  id                BIGSERIAL PRIMARY KEY,           -- Auto-increment transaction ID
  sender_upi        VARCHAR(100)    NOT NULL,        -- UPI ID of sender
  receiver_upi      VARCHAR(100)    NOT NULL,        -- UPI ID of receiver
  amount            NUMERIC(15,2)   NOT NULL,        -- Transaction amount (₹, supports paise)
  status            VARCHAR(10)     NOT NULL,        -- Current status (e.g., queued, processed)
  created_at        TIMESTAMPTZ     DEFAULT NOW(),   -- Creation timestamp
  updated_at        TIMESTAMPTZ                        -- Last update timestamp (nullable)
);

-- For queries on status + recency (API/status dashboard)
CREATE INDEX idx_tx_status_created ON transactions(status, created_at);

-- ============================================================================
-- transaction_history: Audit trail for status changes/events
-- ============================================================================
CREATE TABLE transaction_history (
  history_id        BIGSERIAL PRIMARY KEY,           -- Auto-increment history record ID
  transaction_id    BIGINT          NOT NULL,        -- FK to transactions.id
  prev_status       VARCHAR(10)     NOT NULL,        -- Status before change
  new_status        VARCHAR(10)     NOT NULL,        -- Status after change
  changed_at        TIMESTAMPTZ     DEFAULT NOW(),   -- When the status changed
  CONSTRAINT fk_history_tx FOREIGN KEY(transaction_id) REFERENCES transactions(id)
);

-- ============================================================================
-- idempotency_keys: Prevents duplicate processing for same API request
-- ============================================================================
CREATE TABLE idempotency_keys (
  idempotency_key   VARCHAR(64)     PRIMARY KEY,     -- External idempotency key
  transaction_id    BIGINT          NOT NULL,        -- FK to transactions.id
  created_at        TIMESTAMPTZ     DEFAULT NOW(),   -- When key was stored
  CONSTRAINT fk_idem_tx FOREIGN KEY(transaction_id) REFERENCES transactions(id)
);

-- ============================================================================
-- Notes:
-- - Make sure your app uses UTC (TIMESTAMPTZ) for all times.
-- - Add more indices for high-volume prod workloads as needed.
-- ============================================================================
