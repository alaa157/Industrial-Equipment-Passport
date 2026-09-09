CREATE TABLE maintenance_requests(
id UUID PRIMARY KEY,
equipment_id UUID NOT NULL,
requester_id UUID NOT NULL,
title VARCHAR(180),
description VARCHAR(3000) NOT NULL,
type VARCHAR(30) NOT NULL,
status VARCHAR(30) NOT NULL,
priority VARCHAR(20) NOT NULL,
assigned_technician_id UUID,
estimated_duration_minutes INTEGER,
actual_duration_minutes INTEGER,
labor_notes VARCHAR(3000),
maintenance_cost NUMERIC(14,2),
completion_notes VARCHAR(2000),
due_date DATE,
created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_maintenance_equipment ON maintenance_requests(equipment_id);
CREATE INDEX idx_maintenance_status ON maintenance_requests(status);
CREATE INDEX idx_maintenance_priority ON maintenance_requests(priority);
CREATE INDEX idx_maintenance_due ON maintenance_requests(due_date);

CREATE TABLE maintenance_schedules(
id UUID PRIMARY KEY,
equipment_id UUID NOT NULL,
title VARCHAR(180) NOT NULL,
interval_days INTEGER NOT NULL CHECK(interval_days>0),
next_run_date DATE NOT NULL,
active BOOLEAN NOT NULL DEFAULT TRUE,
priority VARCHAR(20) NOT NULL
);

CREATE INDEX idx_schedule_next_run ON maintenance_schedules(next_run_date);
