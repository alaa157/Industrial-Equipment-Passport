CREATE TABLE maintenance_attachments(
id UUID PRIMARY KEY,
maintenance_id UUID NOT NULL,
original_filename VARCHAR(255) NOT NULL,
stored_filename VARCHAR(255) NOT NULL UNIQUE,
content_type VARCHAR(120) NOT NULL,
size_bytes BIGINT NOT NULL CHECK(size_bytes>0),
relative_path VARCHAR(500) NOT NULL,
created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_maintenance_attachment_request ON maintenance_attachments(maintenance_id);
