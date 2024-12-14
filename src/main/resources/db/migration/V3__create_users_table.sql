CREATE TABLE users (
      id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
      name       VARCHAR(255) NOT NULL,
      email      VARCHAR NOT NULL UNIQUE,
      password   VARCHAR NOT NULL,
      role       VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'USER'))
);

-- password for admin is admin

INSERT INTO users (name, email, password, role)
VALUES ('admin','admin@admin', '$2a$12$guy63NYAFXWf1kQJ20CNrO92nvk9fq5aMVgNlQFNcWJBnllXjWrb2', 'ADMIN');