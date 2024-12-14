CREATE TABLE refresh_token
(
    id          UUID      PRIMARY KEY DEFAULT uuid_generate_v4(),
    token       VARCHAR   NOT NULL,
    user_id     UUID      NOT NULL,
    revoked     BOOLEAN   NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);