CREATE TABLE equipment_types(
id UUID PRIMARY KEY,
name VARCHAR(100) NOT NULL UNIQUE,
description VARCHAR(500)
);

CREATE TABLE sites(
id UUID PRIMARY KEY,
code VARCHAR(30) NOT NULL UNIQUE,
name VARCHAR(150) NOT NULL,
address VARCHAR(500),
active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE buildings(
id UUID PRIMARY KEY,
site_id UUID NOT NULL REFERENCES sites(id),
code VARCHAR(30) NOT NULL,
name VARCHAR(150) NOT NULL,
CONSTRAINT uk_building_site_code UNIQUE(site_id,code)
);

CREATE INDEX idx_building_site ON buildings(site_id);

CREATE TABLE areas(
id UUID PRIMARY KEY,
building_id UUID NOT NULL REFERENCES buildings(id),
code VARCHAR(30) NOT NULL,
name VARCHAR(150) NOT NULL,
CONSTRAINT uk_area_building_code UNIQUE(building_id,code)
);

CREATE INDEX idx_area_building ON areas(building_id);

CREATE TABLE equipment(
id UUID PRIMARY KEY,
asset_code VARCHAR(60) NOT NULL UNIQUE,
serial_number VARCHAR(120) NOT NULL UNIQUE,
name VARCHAR(180) NOT NULL,
description VARCHAR(1000),
manufacturer VARCHAR(150) NOT NULL,
model VARCHAR(150) NOT NULL,
equipment_type_id UUID NOT NULL REFERENCES equipment_types(id),
purchase_date DATE,
installation_date DATE,
warranty_expiration DATE,
status VARCHAR(30) NOT NULL,
criticality VARCHAR(20) NOT NULL,
site_id UUID REFERENCES sites(id),
building_id UUID REFERENCES buildings(id),
area_id UUID REFERENCES areas(id),
responsible_department VARCHAR(120),
responsible_technician_id UUID,
notes VARCHAR(2000),
created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_equipment_asset_code ON equipment(asset_code);
CREATE INDEX idx_equipment_serial ON equipment(serial_number);
CREATE INDEX idx_equipment_status ON equipment(status);
CREATE INDEX idx_equipment_criticality ON equipment(criticality);
CREATE INDEX idx_equipment_site ON equipment(site_id);

CREATE TABLE attachments(
id UUID PRIMARY KEY,
equipment_id UUID NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
original_filename VARCHAR(255) NOT NULL,
stored_filename VARCHAR(255) NOT NULL UNIQUE,
content_type VARCHAR(120) NOT NULL,
size_bytes BIGINT NOT NULL CHECK(size_bytes>0),
relative_path VARCHAR(500) NOT NULL,
created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_attachment_equipment ON attachments(equipment_id);

CREATE TABLE inspections(
id UUID PRIMARY KEY,
equipment_id UUID NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
inspector_id UUID NOT NULL,
inspection_date DATE NOT NULL,
checklist VARCHAR(1200) NOT NULL,
result VARCHAR(30) NOT NULL,
notes VARCHAR(2000),
next_inspection_date DATE,
created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inspection_equipment ON inspections(equipment_id);
CREATE INDEX idx_inspection_date ON inspections(inspection_date);

CREATE TABLE spare_parts(
id UUID PRIMARY KEY,
part_number VARCHAR(80) NOT NULL UNIQUE,
name VARCHAR(180) NOT NULL,
description VARCHAR(1000),
manufacturer VARCHAR(150),
quantity INTEGER NOT NULL CHECK(quantity>=0),
minimum_quantity INTEGER NOT NULL CHECK(minimum_quantity>=0),
unit_cost NUMERIC(14,2) NOT NULL CHECK(unit_cost>=0),
location VARCHAR(150),
created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_part_quantity ON spare_parts(quantity);

CREATE TABLE equipment_parts(
id UUID PRIMARY KEY,
equipment_id UUID NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
part_id UUID NOT NULL REFERENCES spare_parts(id),
quantity_installed INTEGER NOT NULL CHECK(quantity_installed>0),
CONSTRAINT uk_equipment_part UNIQUE(equipment_id,part_id)
);

CREATE TABLE downtime_records(
id UUID PRIMARY KEY,
equipment_id UUID NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
reason VARCHAR(500) NOT NULL,
start_time TIMESTAMPTZ NOT NULL,
end_time TIMESTAMPTZ,
related_maintenance_id UUID,
notes VARCHAR(1000)
);

CREATE INDEX idx_downtime_equipment ON downtime_records(equipment_id);
CREATE INDEX idx_downtime_start ON downtime_records(start_time);

CREATE TABLE fault_reports(
id UUID PRIMARY KEY,
equipment_id UUID NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
reporter_id UUID NOT NULL,
title VARCHAR(180) NOT NULL,
description VARCHAR(3000) NOT NULL,
severity VARCHAR(20) NOT NULL,
reported_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
status VARCHAR(40) NOT NULL,
maintenance_request_id UUID
);

CREATE INDEX idx_fault_equipment ON fault_reports(equipment_id);
CREATE INDEX idx_fault_status ON fault_reports(status);
CREATE INDEX idx_fault_severity ON fault_reports(severity);
