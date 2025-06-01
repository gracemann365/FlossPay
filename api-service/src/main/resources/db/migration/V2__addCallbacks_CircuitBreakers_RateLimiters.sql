-- ============================================================================
-- V2__add_callbacks_circuitBreakers_rateLimiters.sql
-- Adds operational tables: webhooks, circuit breakers, and client rate limits.
-- For OpenPay UPI Gateway (Phase 2+ Operational Features)
-- ============================================================================

-- ============================================================================
-- webhook_callbacks: Manages outbound webhook delivery and retry states
-- ============================================================================
CREATE TABLE webhook_callbacks (
  callback_id       BIGSERIAL PRIMARY KEY,         -- Unique callback record
  transaction_id    BIGINT         NOT NULL,       -- FK to transactions.id
  url               VARCHAR(255)   NOT NULL,       -- Target webhook URL
  status            VARCHAR(10)    NOT NULL,       -- Current delivery status (e.g., pending, sent, failed)
  last_attempted_at TIMESTAMPTZ,                   -- Last attempt time
  attempts          INTEGER        DEFAULT 0,      -- Number of delivery attempts
  CONSTRAINT fk_cb_tx FOREIGN KEY(transaction_id) REFERENCES transactions(id)
);

-- ============================================================================
-- service_circuit_breakers: Tracks circuit breaker status for 3rd-party services
-- ============================================================================
CREATE TABLE service_circuit_breakers (
  service_name      VARCHAR(50)    PRIMARY KEY,    -- External service name
  state             VARCHAR(10)    NOT NULL,       -- Current breaker state (open, closed, half-open)
  failure_count     INTEGER        DEFAULT 0,      -- Number of recent failures
  last_failure_at   TIMESTAMPTZ                    -- Last failure timestamp
);

-- ============================================================================
-- client_rate_limits: Tracks API quota state for each client/application
-- ============================================================================
CREATE TABLE client_rate_limits (
  client_id         VARCHAR(64)    PRIMARY KEY,    -- Unique client identifier (API key, org, etc.)
  tokens            INTEGER        DEFAULT 100,    -- Available tokens (quota for leaky-bucket, etc.)
  last_refill       TIMESTAMPTZ    DEFAULT NOW()   -- Last quota refill timestamp
);

-- ============================================================================
-- Notes:
-- - Add indices as needed based on query/load profiles.
-- - These tables are operational/support (not core payment flows).
-- ============================================================================
