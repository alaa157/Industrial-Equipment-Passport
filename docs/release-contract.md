# Industrial Equipment Passport Release Contract

This document defines the Phase 0 release contract for making the Industrial
Equipment Passport deployable on one Linux VM with Docker Compose. It is a
baseline and acceptance contract; it does not change the application
topology or runtime behavior by itself.

## Baseline

| Item | Value |
| --- | --- |
| Baseline branch | `main` |
| Baseline commit | `498c872` (`Updated README.md file`) |
| Release branch | `deployability-plan-single-vm-compose` |
| Deployment target | One Linux VM running Docker Engine and Docker Compose v2 |
| Public entry point | Frontend on TCP `3000`; API gateway on TCP `8080` |
| Persistence | PostgreSQL databases, Redis, RabbitMQ, monitoring data, and attachment storage |

The baseline Compose file builds only the application containers and uses host
networking. PostgreSQL, Redis, and RabbitMQ are external prerequisites even
though the repository contains PostgreSQL initialization and monitoring
configuration. The deployment phases will replace this with a self-contained,
bridge-network Compose topology while preserving a local development path.

## Deployment contract

The target deployment must provide:

1. Reproducible application images from a clean checkout; host-built JARs,
   `.next`, and `node_modules` must not be prerequisites.
2. Compose-managed PostgreSQL, Redis, RabbitMQ, Prometheus, and Grafana.
3. A user-defined internal bridge network with Docker DNS service names.
4. Persistent named volumes for databases, broker/cache state, monitoring
   state, and uploaded attachments.
5. Healthchecks and startup ordering for infrastructure, services, gateway,
   and frontend.
6. Forward-only Flyway migrations with an explicit bootstrap/migration
   procedure and controlled development seed data.
7. Non-root application containers and deployment-time validation for secrets,
   credentials, URLs, and CORS origins.
8. Documented backup, restore, upgrade, smoke-test, and rollback procedures.

## Required ports

| Component | Port | Exposure |
| --- | ---: | --- |
| Frontend | `3000` | Public |
| API gateway | `8080` | Public or reverse-proxy-only |
| Auth service | `8081` | Internal |
| Equipment service | `8082` | Internal |
| Maintenance service | `8083` | Internal |
| Notification service | `8084` | Internal |
| Audit service | `8085` | Internal |
| PostgreSQL | `5432` | Internal |
| Redis | `6379` | Internal |
| RabbitMQ AMQP | `5672` | Internal |
| RabbitMQ management | `15672` | Administrator-only |
| Prometheus | `9090` | Administrator-only |
| Grafana | `3001` | Administrator-only |

Only the frontend and gateway should be reachable from the public network by
default. Infrastructure and domain-service ports should remain on the
internal Compose network or be restricted by the VM firewall.

## Environment contract

Required deployment variables include:

- `POSTGRES_USER`, `POSTGRES_PASSWORD`, and all five database names.
- `JWT_SECRET`, with at least 32 random bytes in production.
- `REDIS_PASSWORD`.
- `RABBITMQ_USER` and `RABBITMQ_PASSWORD`.
- `NEXT_PUBLIC_API_URL`.
- `CORS_ALLOWED_ORIGINS`.
- `FILE_STORAGE_PATH`.
- Service URLs used by the gateway.

Example or development credentials must be rejected when
`DEPLOYMENT_ENV=production`. Development seed credentials must be explicit,
documented as local-only, and never be used as production defaults.

## Smoke-test journey

After a fresh install and after each upgrade, verify this minimum user journey:

1. `docker compose config` succeeds with the deployment environment file.
2. PostgreSQL, Redis, RabbitMQ, Prometheus, and Grafana report healthy.
3. Each Spring service responds successfully to `/actuator/health`.
4. The gateway responds successfully to `/actuator/health`.
5. The frontend responds successfully at `/`.
6. Login succeeds through `POST /api/v1/auth/login`.
7. The authenticated user can load the dashboard.
8. Equipment search returns seeded or provisioned data.
9. A maintenance request can be viewed or created by an authorized role.
10. A domain event produces the expected notification/audit result.
11. Prometheus can scrape the gateway and service metrics.
12. Grafana loads the provisioned dashboard.

## Persistence and rollback artifacts

Before an upgrade, retain:

- A tagged application image set or immutable image digests.
- The exact deployment environment file stored outside Git.
- PostgreSQL logical dumps for all five databases.
- A consistent backup of `storage/` attachment bytes.
- The deployed Git commit and Compose configuration.
- Migration history and service logs covering the deployment window.

Rollback means restoring the previous image set and Compose configuration. A
database migration is not automatically reversible; any incompatible schema
change requires a tested forward-compatible rollback strategy and a verified
database backup before deployment.

## Release gates

### Staging gate

- Clean-checkout image builds succeed.
- Compose configuration validates without host-network assumptions.
- Migrations run successfully on empty databases.
- Upgrade migrations run successfully on a copy of representative data.
- All healthchecks and the smoke-test journey pass.
- Backup and restore procedures have been exercised.
- No example credentials or secrets are active.

### Production gate

- Staging gate is green.
- TLS/reverse-proxy and VM firewall rules are configured.
- Production secrets are injected through the approved secret mechanism.
- Monitoring and log retention are configured.
- Backup artifacts are stored outside the VM.
- A rollback owner and maintenance window are recorded.

