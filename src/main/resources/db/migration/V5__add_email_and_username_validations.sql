ALTER TABLE users ADD CONSTRAINT users_username_length_check
    CHECK (char_length(username) >= 2);
ALTER TABLE users ADD CONSTRAINT users_email_format_check
    CHECK (email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$');
CREATE UNIQUE INDEX users_email_unique ON users (LOWER(email));
