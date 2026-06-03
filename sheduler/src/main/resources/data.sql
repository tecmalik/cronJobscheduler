CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    information TEXT
    );

CREATE TABLE IF NOT EXISTS scheduler_log (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    information TEXT,
    status VARCHAR(50) NOT NULL,
    retry_count INT DEFAULT 0,
    error_message TEXT
    );