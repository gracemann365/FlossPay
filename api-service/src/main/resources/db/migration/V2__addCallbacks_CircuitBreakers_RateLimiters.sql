-- V2__add_callbacks_circuitBreakers_rateLimiters.sql
-- Adds non-core operational tables for OpenPay UPI Gateway
-- Includes webhook callbacks, service circuit breaker states, and client rate limiting

-- =======================================
-- Table: webhook_callbacks
-- Manages webhook/callback delivery and retries for external systems
-- =======================================
CREATE TABLE webhook_callbacks (
  callback_id       BIGSERIAL PRIMARY KEY,
  transaction_id    BIGINT         NOT NULL,
  url               VARCHAR(255)   NOT NULL,
  status            VARCHAR(10)    NOT NULL,
  last_attempted_at TIMESTAMPTZ,
  attempts          INTEGER        DEFAULT 0,
  CONSTRAINT fk_cb_tx FOREIGN KEY(transaction_id) REFERENCES transactions(id)
);

-- =======================================
-- Table: service_circuit_breakers
-- Tracks circuit breaker state for integrated external services
-- =======================================
CREATE TABLE service_circuit_breakers (
  service_name      VARCHAR(50)    PRIMARY KEY,
  state             VARCHAR(10)    NOT NULL,
  failure_count     INTEGER        DEFAULT 0,
  last_failure_at   TIMESTAMPTZ
);

-- =======================================
-- Table: client_rate_limits
-- Maintains API request quota state for each client
-- =======================================
CREATE TABLE client_rate_limits (
  client_id         VARCHAR(64)    PRIMARY KEY,
  tokens            INTEGER        DEFAULT 100,
  last_refill       TIMESTAMPTZ    DEFAULT NOW()
);

