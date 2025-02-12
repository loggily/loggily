CREATE TABLE IF NOT EXISTS logs (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMPTZ NOT NULL,
    environment VARCHAR(255) NULL,
    host VARCHAR(255) NULL,
    application VARCHAR(255) NULL,
    severity_number INT NOT NULL,
    trace_id VARCHAR(255) NULL,
    structured_log JSONB NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_logs_environment_application_timestamp ON logs (environment, application, timestamp);

CREATE INDEX IF NOT EXISTS idx_logs_trace_id_timestamp ON logs (trace_id, timestamp) WHERE trace_id IS NOT NULL;