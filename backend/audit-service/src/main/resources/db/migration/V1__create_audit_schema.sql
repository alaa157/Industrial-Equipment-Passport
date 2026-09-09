CREATE TABLE audit_logs(
id UUID PRIMARY KEY,
user_id UUID,
action VARCHAR(120) NOT NULL,
entity_type VARCHAR(80) NOT NULL,
entity_id UUID,
timestamp TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
ip_address VARCHAR(64),
correlation_id VARCHAR(100),
old_values TEXT,
new_values TEXT
);

CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type,entity_id);
CREATE INDEX idx_audit_user ON audit_logs(user_id);
