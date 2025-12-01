CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public;

CREATE TABLE addresses (
 id BIGSERIAL PRIMARY KEY,
 street VARCHAR(120) NOT NULL,
 city VARCHAR(80) NOT NULL,
 state VARCHAR(60) NOT NULL,
 zip_code VARCHAR(15) NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    phone VARCHAR(20),

    address_id BIGINT NOT NULL,

    CONSTRAINT fk_user_address
        FOREIGN KEY (address_id)
        REFERENCES addresses(id)
        ON DELETE RESTRICT,

    CONSTRAINT uq_user_address UNIQUE (address_id)
);

CREATE TABLE wallets (
    id BIGSERIAL PRIMARY KEY,
    balance NUMERIC(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    user_id BIGINT NOT NULL UNIQUE,

    CONSTRAINT fk_wallet_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
CREATE TABLE virtual_cards (
    id BIGSERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    card_limit NUMERIC(10,2) NOT NULL,
    card_status VARCHAR(20) NOT NULL,

    user_id BIGINT NOT NULL UNIQUE,

    CONSTRAINT fk_vcard_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(512) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    user_id BIGINT NOT NULL UNIQUE,

    CONSTRAINT fk_refresh_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,

    amount NUMERIC(19,2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    sender_wallet_id BIGINT NOT NULL,
    receiver_wallet_id BIGINT NOT NULL,

    CONSTRAINT fk_tx_sender
        FOREIGN KEY (sender_wallet_id)
        REFERENCES wallets(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_tx_receiver
        FOREIGN KEY (receiver_wallet_id)
        REFERENCES wallets(id)
        ON DELETE RESTRICT
);

CREATE TABLE pix_payments (
    id BIGSERIAL PRIMARY KEY,

    amount NUMERIC(19,2) NOT NULL,
    pix_key VARCHAR(120) NOT NULL,
    qr_code_payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expiration_time TIMESTAMP NOT NULL,
    paid_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason VARCHAR(200),

    transaction_id BIGINT NOT NULL UNIQUE,

    CONSTRAINT fk_pix_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES transactions(id)
        ON DELETE CASCADE
);