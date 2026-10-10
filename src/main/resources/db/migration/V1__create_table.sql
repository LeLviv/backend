CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  surname VARCHAR(64) NOT NULL,
  password_hash VARCHAR(64) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  phone_number VARCHAR(12) NOT NULL CHECK(LENGTH(phone_number) = 12),
  role VARCHAR(32) NOT NULL
);