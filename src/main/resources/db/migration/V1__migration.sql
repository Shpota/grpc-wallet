CREATE TABLE balance(
    user_id  VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    amount   INTEGER,
    PRIMARY KEY (user_id, currency)
);
