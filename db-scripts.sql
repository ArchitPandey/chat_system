CREATE SCHEMA IF NOT EXISTS chat_system;

CREATE TABLE chat_system.users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(20)
);

INSERT INTO chat_system.users (name, email, phone)
VALUES 
  ('John', 'john@example.com', '1234567890'),
  ('Doe', 'doe@example.com', '9876543210');
  
CREATE TABLE chat_system.messages (
    message_id SERIAL PRIMARY KEY,
    from_id INTEGER NOT NULL,
    to_id INTEGER NOT NULL,
    txt VARCHAR(1000) NOT NULL,
    sent_ts TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_from_user FOREIGN KEY (from_id) REFERENCES chat_system.users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_to_user FOREIGN KEY (to_id) REFERENCES chat_system.users(user_id) ON DELETE CASCADE
);
