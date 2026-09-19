# Industrial Equipment Passport Deployment Plan

## Goal

Make the Industrial Equipment Passport reproducibly deployable on a single
Linux VM with Docker Compose while preserving `main` and keeping local
development practical.

## Target architecture

The release topology will use:

- Next.js frontend on `3000`.
- Spring Cloud Gateway on `8080`.
- Auth, equipment, maintenance, notification, and audit services on
  `8081–8085`.
- Compose-managed PostgreSQL with five databases.
- Compose-managed Redis and RabbitMQ.
- Compose-managed Prometheus and Grafana.
- A user-defined internal bridge network.
- Named volumes for databases, broker/cache state, monitoring state, and
  attachment storage.

The public surface is the frontend and gateway. Domain services and
infrastructure remain internal unless an administrator explicitly exposes
their management interfaces.

## Phases

### Phase 0 — Release contract and baseline

**Status: complete**

- Record the baseline branch and commit.
- Define the single-VM deployment target and port contract.
- Define required environment variables and production credential rules.
- Define fresh-install, upgrade, smoke-test, backup, and rollback expectations.
- Define staging and production release gates.

Artifacts:

- [Release contract](release-contract.md)
- This deployment plan

### Phase 1 — Reproducible image builds

**Status: planned**

- Replace backend `COPY target/*.jar` Dockerfiles with multi-stage Maven builds.
- Build the frontend dependencies and Next.js output inside the image.
- Add `.dockerignore` files and image version metadata.
- Run application processes as non-root users.
- Verify all images build from a clean checkout.

### Phase 2 — Runtime hardening and health

**Status: planned**

- Add container healthchecks for Actuator endpoints and frontend readiness.
- Add JVM/container memory settings appropriate for a single VM.
- Make service startup failures explicit and observable.
- Ensure attachment directories are writable only by the application user.
- Validate non-root runtime behavior.

### Phase 3 — Compose network and service discovery

**Status: planned**

- Remove `network_mode: host` from the production Compose topology.
- Add the internal bridge network.
- Configure service URLs, PostgreSQL URLs, Redis, RabbitMQ, and Prometheus
  targets using Docker DNS names.
- Add health-gated `depends_on` relationships.
- Keep any host-network workaround in a separate local-only override.

### Phase 4 — Infrastructure and persistence

**Status: planned**

- Add PostgreSQL 17 with initialization for all five databases.
- Add Redis and RabbitMQ with credentials, healthchecks, and persistent
  volumes.
- Add Prometheus and Grafana with service-name scrape targets, datasource
  provisioning, dashboards, and persistent state.
- Add named attachment storage and document backup boundaries.

### Phase 5 — Migration, seed, and environment control

**Status: planned**

- Add an explicit one-shot migration/bootstrap service or command.
- Keep Flyway migrations forward-only and gate application startup on success.
- Separate development seed behavior from production initialization.
- Validate missing, weak, and default credentials.
- Validate URLs, CORS origins, storage paths, and deployment environment.

### Phase 6 — Delivery, operations, and rollback

**Status: planned**

- Add CI checks for Maven tests, frontend checks, image builds, and Compose
  validation.
- Document first install, upgrade, backup, restore, and rollback.
- Add staging deployment smoke tests and log/metric checks.
- Verify the release gates in `docs/release-contract.md`.

### Phase 7 — Local demo and screenshot mode

**Status: planned**

- Add a local-only override for screenshot/demo execution when needed.
- Seed safe demo data without changing production defaults.
- Verify login, dashboard, equipment, maintenance, notifications, and audit
  workflows through the browser.
- Store only intentionally captured screenshots in the repository.

## Current baseline risks

The most important baseline risks are:

1. Images require host-built Maven JARs and frontend build artifacts.
2. Compose uses host networking and does not provision its infrastructure.
3. Service configuration defaults to `localhost`, which is incompatible with
   a bridge-network deployment.
4. Prometheus targets `localhost`, which will not scrape sibling containers.
5. Flyway runs implicitly during application startup rather than through an
   explicit release-controlled migration step.
6. Development credentials and default passwords are present in examples and
   seed migrations.
7. The current Java runtime containers do not declare non-root users or
   healthchecks.

Phase 1 should address image reproducibility first; later phases depend on
being able to build and run the complete stack from a clean checkout.

