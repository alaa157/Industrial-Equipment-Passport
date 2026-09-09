CREATE TABLE part_replacements(
id UUID PRIMARY KEY,
equipment_id UUID NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
part_id UUID NOT NULL REFERENCES spare_parts(id),
maintenance_id UUID,
quantity INTEGER NOT NULL CHECK(quantity>0),
replaced_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_part_replacement_equipment ON part_replacements(equipment_id);
CREATE INDEX idx_part_replacement_maintenance ON part_replacements(maintenance_id);
