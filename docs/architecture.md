# Architecture

## System overview

Industrial Equipment Passport is a browser application for registering industrial assets, tracking inspections and downtime, managing maintenance work, and recording operational events.

```text
Browser (Next.js :3000)
          |
          v
API gateway (Spring Cloud Gateway :8080)
   |       |        |          |        |
 Auth  Equipment  Maintenance Notifications Audit
 :8081   :8082      :8083        :8084      :8085
   |       |          |            |          |
 Postgres Postgres  Postgres    Postgres   Postgres
  auth   equipment maintenance notification audit
   |
 Redis (login rate limiting)

 Equipment/Maintenance -- RabbitMQ exchange: iep.domain.events -->
 Notifications, Audit, and cross-service consumers
```

The gateway is the public API entry point. It preserves the host header, applies CORS and JWT resource-server security, adds a correlation ID when one is absent, and routes `/api/v1/**` paths to the appropriate service. Services also expose Spring Boot Actuator health, info, and Prometheus endpoints locally.

## Services

| Service | Port | Responsibility | Database |
| --- | ---: | --- | --- |
| `api-gateway` | 8080 | Public routing, CORS, JWT validation, correlation IDs | None |
| `auth-service` | 8081 | Login, refresh tokens, users, roles, permissions | `passport_auth` |
| `equipment-service` | 8082 | Equipment registry, catalog, inspections, faults, downtime, parts, attachments | `passport_equipment` |
| `maintenance-service` | 8083 | Maintenance requests, technicians, workflow, attachments | `passport_maintenance` |
| `notification-service` | 8084 | User and broadcast notifications | `passport_notification` |
| `audit-service` | 8085 | Append-only operational event history | `passport_audit` |

Each service owns its schema and runs Flyway migrations at startup. JPA is configured with `ddl-auto: validate`, so the application expects the migrations to create the schema and does not generate tables.

## Authentication and authorization

The auth service issues an HS256 access token and a persisted, hashed refresh token. Access tokens contain the user ID as `sub`, plus `username`, `roles`, and resolved `permissions`. The default access lifetime is 15 minutes and the default refresh lifetime is 7 days.

The gateway and downstream services validate the shared JWT secret. Method-level authorization uses roles (`ADMIN`, `MANAGER`, `TECHNICIAN`, `INSPECTOR`, `VIEWER`); the permission catalog is also included in tokens for the UI and future fine-grained checks. Redis stores the short-lived login-attempt counter used by the auth rate limiter.

## Event flow

Equipment and maintenance publish JSON events to the durable topic exchange `iep.domain.events`, using the event type as the routing key. The envelope is:

```json
{
  "type": "MaintenanceCompletedEvent",
  "timestamp": "2026-09-09T10:00:00Z",
  "payload": { "maintenanceId": "...", "equipmentId": "..." }
}
```

Current durable consumers are:

- `notification.events` receives every event and creates a notification for the assigned technician/user when the payload identifies one.
- `audit.events` receives every event and stores the event type, entity ID, and payload.
- `maintenance.inspection-failures` converts `InspectionFailedEvent` into a high-priority corrective maintenance request.
- `equipment.maintenance-events` listens for `MaintenanceCompletedEvent` and returns equipment in `UNDER_MAINTENANCE` to `ACTIVE`.

## Storage

Uploaded equipment files are stored below `FILE_STORAGE_PATH/equipment`; maintenance files use the maintenance subdirectory. The Docker compose file mounts the repository `storage/` directory into equipment and maintenance containers, so that directory must be backed up alongside the databases.

## Deployment note

`docker-compose.yml` builds the application services only. PostgreSQL, Redis, and RabbitMQ must be supplied separately and made reachable using the environment variables documented in [development.md](development.md).
