# Database guide

## Database-per-service layout

The application uses PostgreSQL with one database per bounded context:

| Database | Owner | Main tables |
| --- | --- | --- |
| `passport_auth` | auth-service | `users`, `user_roles`, `refresh_tokens`, `permissions`, `role_permissions` |
| `passport_equipment` | equipment-service | catalogs, `equipment`, `attachments`, `inspections`, `spare_parts`, `equipment_parts`, `part_replacements`, `downtime_records`, `fault_reports` |
| `passport_maintenance` | maintenance-service | `maintenance_requests`, `maintenance_schedules`, `technicians`, `maintenance_attachments` |
| `passport_notification` | notification-service | `notifications` |
| `passport_audit` | audit-service | `audit_logs` |

The database names are created by `infrastructure/postgres/init/01-databases.sql`. Each service has its own Flyway directory under `src/main/resources/db/migration`; migrations are applied on startup.

## Ownership and relationships

Foreign keys are enforced inside each service database. Cross-service relationships such as `equipment_id`, `requester_id`, `technician_id`, and `maintenance_id` are UUID references by convention, not cross-database foreign keys. A service must not query another service's database directly.

The equipment context models the physical hierarchy `site -> building -> area -> equipment`, with equipment also connected to inspections, attachments, spare parts, downtime, and fault reports. Maintenance requests refer to equipment IDs and technician/user IDs without importing those records. Notifications and audit logs consume domain events and retain the relevant IDs and event payload.

## Migration rules

Create a new versioned SQL file, for example `V4__add_field.sql`, in the owning service's migration directory. Use a forward-only migration and preserve already-applied files. Keep `ddl-auto: validate`; it catches entity/schema drift without silently changing production data.

For destructive or large changes:

1. Add the new nullable/schema-compatible structure.
2. Deploy code that can read both forms and backfill in a controlled operation.
3. Enforce the final constraint only after existing data is valid.

## Seed data

Development-only seed migrations provide sample users, role assignments, equipment catalogs, assets, inspections, spare parts, and technicians. They are useful for local UI and E2E work but are not a production bootstrap strategy. Production credentials and data must be provisioned separately.

## Backups and files

Database backups do not include attachment content. Equipment and maintenance attachment metadata is stored in PostgreSQL while the bytes are stored below `FILE_STORAGE_PATH`; back up both consistently. The local compose setup mounts `./storage` into the two services that write files.

## Time and numeric conventions

Use UTC for `TIMESTAMPTZ` values and Java `Instant` values. Use `DATE`/`LocalDate` for calendar dates such as purchase, inspection, and due dates. Costs use PostgreSQL `NUMERIC(14,2)` and Java `BigDecimal`; do not use floating-point types for money.
