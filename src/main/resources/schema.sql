CREATE TABLE IF NOT EXISTS logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    environment VARCHAR(255) NULL,
    host VARCHAR(255) NULL,
    application VARCHAR(255) NULL,
    severity_number INT NOT NULL,
    trace_id VARCHAR(255) NULL,
    structured_log TEXT NOT NULL
);