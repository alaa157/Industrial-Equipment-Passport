CREATE TABLE permissions(
id UUID PRIMARY KEY,
code VARCHAR(100) NOT NULL UNIQUE,
description VARCHAR(255) NOT NULL
);

CREATE TABLE role_permissions(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
role VARCHAR(30) NOT NULL,
permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
CONSTRAINT uk_role_permission UNIQUE(role,permission_id)
);

CREATE INDEX idx_role_permissions_role ON role_permissions(role);

INSERT INTO permissions(id,code,description) VALUES
('00000000-0000-0000-0000-000000000101','equipment.read','View equipment'),
('00000000-0000-0000-0000-000000000102','equipment.write','Create and modify equipment'),
('00000000-0000-0000-0000-000000000103','equipment.status','Change equipment status'),
('00000000-0000-0000-0000-000000000104','maintenance.read','View maintenance'),
('00000000-0000-0000-0000-000000000105','maintenance.write','Create and modify maintenance'),
('00000000-0000-0000-0000-000000000106','inspection.write','Perform inspections'),
('00000000-0000-0000-0000-000000000107','parts.write','Manage spare parts'),
('00000000-0000-0000-0000-000000000108','fault.write','Report equipment faults'),
('00000000-0000-0000-0000-000000000109','audit.read','View audit history'),
('00000000-0000-0000-0000-000000000110','user.write','Manage users and roles');

INSERT INTO role_permissions(role,permission_id)
SELECT 'ADMIN',id FROM permissions;

INSERT INTO role_permissions(role,permission_id)
SELECT 'MANAGER',id FROM permissions
WHERE code IN('equipment.read','equipment.write','equipment.status','maintenance.read','maintenance.write','inspection.write','parts.write','fault.write','audit.read');

INSERT INTO role_permissions(role,permission_id)
SELECT 'TECHNICIAN',id FROM permissions
WHERE code IN('equipment.read','equipment.status','maintenance.read','maintenance.write','parts.write','fault.write');

INSERT INTO role_permissions(role,permission_id)
SELECT 'INSPECTOR',id FROM permissions
WHERE code IN('equipment.read','inspection.write','fault.write');

INSERT INTO role_permissions(role,permission_id)
SELECT 'VIEWER',id FROM permissions
WHERE code IN('equipment.read','maintenance.read','audit.read');
