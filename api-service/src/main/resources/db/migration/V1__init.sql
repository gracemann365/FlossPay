CREATE TABLE transactions (
  id                BIGSERIAL PRIMARY KEY,
  sender_upi        VARCHAR(100)    NOT NULL,
  receiver_upi      VARCHAR(100)    NOT NULL,
  amount            NUMERIC(15,2)   NOT NULL,
  status            VARCHAR(10)     NOT NULL,
  created_at        TIMESTAMPTZ     DEFAULT NOW(),
  updated_at        TIMESTAMPTZ
);

CREATE INDEX idx_tx_status_created ON transactions(status, created_at);

CREATE TABLE transaction_history (
  history_id        BIGSERIAL PRIMARY KEY,
  transaction_id    BIGINT          NOT NULL,
  prev_status       VARCHAR(10)     NOT NULL,
  new_status        VARCHAR(10)     NOT NULL,
  changed_at        TIMESTAMPTZ     DEFAULT NOW(),
  CONSTRAINT fk_history_tx FOREIGN KEY(transaction_id) REFERENCES transactions(id)
);

CREATE TABLE idempotency_keys (
  idempotency_key   VARCHAR(64)     PRIMARY KEY,
  transaction_id    BIGINT          NOT NULL,
  created_at        TIMESTAMPTZ     DEFAULT NOW(),
  CONSTRAINT fk_idem_tx FOREIGN KEY(transaction_id) REFERENCES transactions(id)
);
