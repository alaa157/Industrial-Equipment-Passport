INSERT INTO equipment_types(id,name,description) VALUES
('30000000-0000-0000-0000-000000000001','CNC Machining Center','Computer numerical control machining equipment'),
('30000000-0000-0000-0000-000000000002','Hydraulic Press','Industrial hydraulic forming and pressing equipment'),
('30000000-0000-0000-0000-000000000003','Industrial Compressor','Compressed-air generation equipment'),
('30000000-0000-0000-0000-000000000004','Conveyor System','Material handling conveyor equipment'),
('30000000-0000-0000-0000-000000000005','Industrial Pump','Process-fluid pumping equipment'),
('30000000-0000-0000-0000-000000000006','Packaging Machine','Automated packaging and sealing equipment');

INSERT INTO sites(id,code,name,address) VALUES
('31000000-0000-0000-0000-000000000001','FAC-A','Delta Precision Manufacturing','Industrial Zone A, Kafr El-Sheikh'),
('31000000-0000-0000-0000-000000000002','FAC-B','Nile Process Systems','Industrial Zone B, Kafr El-Sheikh');

INSERT INTO buildings(id,site_id,code,name) VALUES
('32000000-0000-0000-0000-000000000001','31000000-0000-0000-0000-000000000001','BLD-P','Production Hall'),
('32000000-0000-0000-0000-000000000002','31000000-0000-0000-0000-000000000001','BLD-U','Utilities Building'),
('32000000-0000-0000-0000-000000000003','31000000-0000-0000-0000-000000000002','BLD-S','Process Hall');

INSERT INTO areas(id,building_id,code,name) VALUES
('33000000-0000-0000-0000-000000000001','32000000-0000-0000-0000-000000000001','A-CNC','CNC Cell'),
('33000000-0000-0000-0000-000000000002','32000000-0000-0000-0000-000000000001','A-PRESS','Press Line'),
('33000000-0000-0000-0000-000000000003','32000000-0000-0000-0000-000000000002','A-AIR','Compressed Air Room'),
('33000000-0000-0000-0000-000000000004','32000000-0000-0000-0000-000000000003','A-PUMP','Pump Station');

INSERT INTO equipment(
id,asset_code,serial_number,name,description,manufacturer,model,equipment_type_id,
purchase_date,installation_date,warranty_expiration,status,criticality,
site_id,building_id,area_id,responsible_department,notes
) VALUES
('11111111-1111-1111-1111-111111111111','CNC-PRD-001','DMG-5X-2024-001','5-Axis CNC Machining Center','High-precision machining cell for aluminum and steel components','DMG MORI','DMU 50','30000000-0000-0000-0000-000000000001','2024-02-15','2024-04-01','2027-04-01','ACTIVE','CRITICAL','31000000-0000-0000-0000-000000000001','32000000-0000-0000-0000-000000000001','33000000-0000-0000-0000-000000000001','Production Engineering','Primary five-axis machining asset.'),
('11111111-1111-1111-1111-111111111112','PRESS-PRD-002','SCH-HP-2023-002','Hydraulic Forming Press','High-force forming press for stamped components','Schuler','HPX-1600','30000000-0000-0000-0000-000000000002','2023-05-04','2023-07-12','2026-07-12','UNDER_MAINTENANCE','HIGH','31000000-0000-0000-0000-000000000001','32000000-0000-0000-0000-000000000001','33000000-0000-0000-0000-000000000002','Production Engineering','Hydraulic seals are under corrective maintenance.'),
('11111111-1111-1111-1111-111111111113','CMP-UTL-001','ATL-AIR-2022-003','Industrial Air Compressor','Plant compressed air supply','Atlas Copco','GA 75','30000000-0000-0000-0000-000000000003','2022-08-20','2022-09-15','2025-09-15','ACTIVE','HIGH','31000000-0000-0000-0000-000000000001','32000000-0000-0000-0000-000000000002','33000000-0000-0000-0000-000000000003','Utilities','Duty compressor for production floor.'),
('11111111-1111-1111-1111-111111111114','PMP-PRC-004','KSB-PMP-2021-004','Process Cooling Pump','Cooling-water circulation pump','KSB','Etanorm 080','30000000-0000-0000-0000-000000000005','2021-11-03','2022-01-18','2025-01-18','OUT_OF_SERVICE','CRITICAL','31000000-0000-0000-0000-000000000002','32000000-0000-0000-0000-000000000003','33000000-0000-0000-0000-000000000004','Utilities','Waiting for replacement mechanical seal.'),
('11111111-1111-1111-1111-111111111115','PKG-LIN-001','BOSCH-PKG-2025-005','Automated Packaging Line','Automated filling and carton sealing line','Bosch','SVE 2520','30000000-0000-0000-0000-000000000006','2025-01-16','2025-03-08','2028-03-08','ACTIVE','MEDIUM','31000000-0000-0000-0000-000000000001','32000000-0000-0000-0000-000000000001','33000000-0000-0000-0000-000000000002','Packaging','New high-throughput line.');

INSERT INTO spare_parts(id,part_number,name,description,manufacturer,quantity,minimum_quantity,unit_cost,location) VALUES
('40000000-0000-0000-0000-000000000001','BRG-6208-2RS','Deep Groove Bearing 6208','Motor shaft bearing','SKF',18,8,42.50,'Central Maintenance Store'),
('40000000-0000-0000-0000-000000000002','SEAL-HYD-45','Hydraulic Rod Seal 45mm','High-pressure hydraulic seal','Parker',5,8,63.75,'Hydraulic Store'),
('40000000-0000-0000-0000-000000000003','FILTER-AIR-75','Compressor Intake Filter','Replacement intake filter','Atlas Copco',12,4,29.90,'Utilities Store'),
('40000000-0000-0000-0000-000000000004','MECH-SEAL-80','Mechanical Seal 80mm','Process-pump mechanical seal','KSB',2,3,185.00,'Pump Store'),
('40000000-0000-0000-0000-000000000005','BELT-SPB-2240','SPB 2240 Drive Belt','Industrial drive belt','Optibelt',14,6,31.50,'Central Maintenance Store');

INSERT INTO inspections(id,equipment_id,inspector_id,inspection_date,checklist,result,notes,next_inspection_date) VALUES
('50000000-0000-0000-0000-000000000001','11111111-1111-1111-1111-111111111111','20000000-0000-0000-0000-000000000003','2026-08-20','Emergency stop; spindle guard; lubrication; electrical cabinet; coolant level','PASSED','All critical safety checks passed.','2026-09-20'),
('50000000-0000-0000-0000-000000000002','11111111-1111-1111-1111-111111111112','20000000-0000-0000-0000-000000000003','2026-08-28','Hydraulic leakage; pressure stability; safety interlock; hose condition','FAILED','Hydraulic rod seal leakage requires corrective intervention.','2026-09-05'),
('50000000-0000-0000-0000-000000000003','11111111-1111-1111-1111-111111111113','20000000-0000-0000-0000-000000000003','2026-08-25','Oil level; intake filter; temperature; pressure; vibration','PASSED_WITH_WARNINGS','Intake filter approaching replacement threshold.','2026-09-25');

INSERT INTO equipment_parts(id,equipment_id,part_id,quantity_installed) VALUES
('60000000-0000-0000-0000-000000000001','11111111-1111-1111-1111-111111111111','40000000-0000-0000-0000-000000000001',4),
('60000000-0000-0000-0000-000000000002','11111111-1111-1111-1111-111111111113','40000000-0000-0000-0000-000000000003',1);

INSERT INTO downtime_records(id,equipment_id,reason,start_time,end_time,notes) VALUES
('70000000-0000-0000-0000-000000000001','11111111-1111-1111-1111-111111111112','Hydraulic seal leakage','2026-08-28T08:15:00Z','2026-08-28T13:45:00Z','Inspection-related downtime.'),
('70000000-0000-0000-0000-000000000002','11111111-1111-1111-1111-111111111114','Mechanical seal failure','2026-08-30T04:30:00Z',NULL,'Pump remains unavailable pending spare part.');

INSERT INTO fault_reports(id,equipment_id,reporter_id,title,description,severity,status,reported_at) VALUES
('80000000-0000-0000-0000-000000000001','11111111-1111-1111-1111-111111111112','20000000-0000-0000-0000-000000000003','Hydraulic oil leak','Visible leakage around hydraulic cylinder rod seal.','HIGH','CONVERTED_TO_MAINTENANCE','2026-08-28T08:20:00Z'),
('80000000-0000-0000-0000-000000000002','11111111-1111-1111-1111-111111111114','20000000-0000-0000-0000-000000000003','Pump vibration','Abnormal vibration observed during startup.','CRITICAL','OPEN','2026-08-30T04:35:00Z');
