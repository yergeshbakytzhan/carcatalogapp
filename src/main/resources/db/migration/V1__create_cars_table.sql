CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE cars (
     id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     make       VARCHAR(255) NOT NULL,
     model      VARCHAR(255) NOT NULL,
     year       INT CHECK (year >= 1886 AND year <= EXTRACT(YEAR FROM CURRENT_DATE)),
     price      NUMERIC CHECK (price > 0),
     vin        CHAR(17) UNIQUE NOT NULL
);