
-- Account
CREATE SEQUENCE account_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE account
(
    id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    type VARCHAR NOT NULL,
    balance NUMERIC(38, 2) NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

-- Transaction
CREATE SEQUENCE transactions_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE transactions
(
    id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    balance NUMERIC(38, 2),
    time TIMESTAMP,
    CONSTRAINT pk_transactions PRIMARY KEY (id)
);

-- DataSourceErrorLog
CREATE SEQUENCE data_source_error_log_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE data_source_error_log
(
    id BIGINT NOT NULL,
    stack_trace_text VARCHAR,
    message VARCHAR,
    method_signature VARCHAR NOT NULL,
    CONSTRAINT pk_data_source_error_log PRIMARY KEY (id)
);

