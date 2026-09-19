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

**Status: complete**

- Replace backend `COPY target/*.jar` Dockerfiles with multi-stage Maven builds.
- Build the frontend dependencies and Next.js output inside the image.
- Add `.dockerignore` files and image version metadata.
- Run application processes as non-root users.
- Verify all images build from a clean checkout.

### Phase 2 — Runtime hardening and health

**Status: complete**

- Add container healthchecks for Actuator endpoints and frontend readiness.
- Add JVM/container memory settings appropriate for a single VM.
- Make service startup failures explicit and observable.
- Ensure attachment directories are writable only by the application user.
- Validate non-root runtime behavior.

Phase 1 Java images now include Actuator healthchecks, non-root users, and
bounded JVM memory defaults. Phase 2 adds an HTTP readiness check to the
frontend, explicitly binds Spring services to all container interfaces, and
keeps the runtime contract compatible with the bridge-network Compose work in
Phase 3.

### Phase 3 — Compose network and service discovery

**Status: complete**

- Remove `network_mode: host` from the production Compose topology.
- Add the internal bridge network.
- Configure service URLs, PostgreSQL URLs, Redis, RabbitMQ, and Prometheus
  targets using Docker DNS names.
- Add health-gated `depends_on` relationships.
- Keep any host-network workaround in a separate local-only override.

The production Compose file now uses a named internal bridge network, exposes
only the frontend and gateway, and injects Docker-DNS service names for
gateway, PostgreSQL, Redis, and RabbitMQ endpoints. Prometheus targets sibling
containers by service name rather than targeting its own container through
`localhost`. Infrastructure services and health-gated startup ordering remain
Phase 4 work.

### Phase 4 — Infrastructure and persistence

**Status: complete**

- Add PostgreSQL 17 with initialization for all five databases.
- Add Redis and RabbitMQ with credentials, healthchecks, and persistent
  volumes.
- Add Prometheus and Grafana with service-name scrape targets, datasource
  provisioning, dashboards, and persistent state.
- Add named attachment storage and document backup boundaries.

The production Compose file now provisions PostgreSQL, Redis, RabbitMQ,
Prometheus, and Grafana with persistent named volumes, healthchecks, and
health-gated application startup. PostgreSQL initialization creates all five
service databases, RabbitMQ loads the repository definitions, and Grafana is
provisioned against the Compose Prometheus service. Migration orchestration and
production environment rejection remain Phase 5 work.

### Phase 5 — Migration, seed, and environment control

**Status: complete**

- Add an explicit one-shot migration/bootstrap service or command.
- Keep Flyway migrations forward-only and gate application startup on success.
- Separate development seed behavior from production initialization.
- Validate missing, weak, and default credentials.
- Validate URLs, CORS origins, storage paths, and deployment environment.

Phase 5 adds explicit one-shot migration services for each Flyway-backed
database. Application services wait for their corresponding migration job to
complete successfully and then start with Flyway disabled. Development-only
seed migrations are isolated under `db/migration/dev` and enabled through the
development `FLYWAY_LOCATIONS` setting; production uses only the base
migrations and does not activate the development admin seeder. The deployment
environment validation script rejects missing values, weak production
credentials, invalid API URLs, and non-absolute storage paths.

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
