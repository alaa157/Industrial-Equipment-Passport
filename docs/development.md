# Development guide

## Prerequisites

- Java 21 and Maven (the services use Spring Boot; check each `pom.xml` for the exact parent/plugin versions).
- Node.js with pnpm for the Next.js frontend and Playwright for end-to-end tests.
- Docker and Docker Compose.
- PostgreSQL, Redis, and RabbitMQ. The checked-in compose file does not create these dependencies.

## First-time setup

```bash
cp .env.example .env
```

Set a real `JWT_SECRET` (at least 32 bytes) before starting services. Create the five databases listed in `infrastructure/postgres/init/01-databases.sql`, using the credentials in `.env`, and make sure the RabbitMQ user/vhost and Redis password match the environment.

Install frontend dependencies:

```bash
cd frontend
pnpm install
cd ..
```

Flyway migrations run automatically when each backend service starts. The auth and equipment migrations include development users and sample equipment data.

## Run locally

Run infrastructure first, then start the backend services. Each can be run from its module directory with:

```bash
mvn spring-boot:run
```

The service ports are 8081 (auth), 8082 (equipment), 8083 (maintenance), 8084 (notifications), and 8085 (audit). Start the gateway on 8080 and the frontend in another terminal:

```bash
cd frontend
pnpm dev
```

The browser application uses `NEXT_PUBLIC_API_URL`, which defaults to `http://localhost:8080` in `.env.example`. When using Docker, `docker compose up --build` builds the application containers; use a `.env` whose service URLs are resolvable from the containers.

## Configuration

Important variables are:

| Variable | Purpose |
| --- | --- |
| `JWT_SECRET` | Shared signing/verification key |
| `JWT_ACCESS_EXPIRATION_MINUTES` / `JWT_REFRESH_EXPIRATION_DAYS` | Token lifetimes |
| `SPRING_DATASOURCE_PASSWORD` | Database password passed by compose |
| `SPRING_DATA_REDIS_*` | Auth Redis connection settings |
| `RABBITMQ_*` | RabbitMQ connection settings |
| `AUTH_SERVICE_URL` … `AUDIT_SERVICE_URL` | Gateway upstream URLs |
| `NEXT_PUBLIC_API_URL` | Frontend API base URL |
| `CORS_ALLOWED_ORIGINS` | Comma-separated browser origins |
| `FILE_STORAGE_PATH` | Shared attachment storage root |

Do not commit `.env`, production secrets, refresh tokens, or uploaded files.

## Project conventions

- Keep service data ownership local; communicate between services through IDs and domain events rather than cross-database joins.
- Use UTC timestamps (`Instant`/`TIMESTAMPTZ`) and ISO dates for date-only fields.
- Add schema changes as a new Flyway migration; do not edit an applied migration.
- Keep API paths under `/api/v1` so gateway routing and frontend calls remain consistent.
- Put shared uploaded files under `storage/` during local development.

## Useful commands

```bash
# Backend unit tests (run from a service directory)
mvn test

# Frontend checks/build
cd frontend
pnpm lint
pnpm build
pnpm start

# End-to-end tests (frontend and API must be running)
cd tests/e2e
pnpm install
pnpm test
pnpm test:ui
```

The default E2E login expects the development admin account seeded by the auth data seeder/migrations. Treat that credential as development-only and change it before sharing an environment.
