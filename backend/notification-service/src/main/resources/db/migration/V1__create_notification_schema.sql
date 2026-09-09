CREATE TABLE notifications(
id UUID PRIMARY KEY,
user_id UUID,
title VARCHAR(150) NOT NULL,
message VARCHAR(1000) NOT NULL,
event_type VARCHAR(80) NOT NULL,
entity_id UUID,
created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
read_at TIMESTAMPTZ
);

CREATE INDEX idx_notification_user ON notifications(user_id);
CREATE INDEX idx_notification_read ON notifications(read_at);
