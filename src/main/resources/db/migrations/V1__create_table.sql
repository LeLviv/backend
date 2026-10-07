DROP TABLE IF EXISTS;

CREATE TABLE users (
  id UUID PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  surname VARCHAR(64) NOT NULL,
  leocard_number BIGINT,
  password_hash VARCHAR(64) NOT NULL,
  date_created DATE NOT NULL,
);