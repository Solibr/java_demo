
-- Client
AlTER TABLE client
    ADD client_id UUID NOT NULL;

-- Account
AlTER TABLE account
    ADD account_id UUID NOT NULL,
    ADD frozen_amount NUMERIC(38, 2),
    ADD status VARCHAR NOT NULL;

AlTER TABLE account
    DROP COLUMN client_id,
    ADD COLUMN client_id UUID NOT NULL;

-- Transaction
AlTER TABLE transactions
    ADD transaction_id UUID NOT NULL,
    ADD status VARCHAR;

AlTER TABLE transactions
    DROP COLUMN account_id,
    ADD COLUMN account_id UUID NOT NULL;
