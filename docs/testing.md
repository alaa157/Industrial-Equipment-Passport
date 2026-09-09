# Testing guide

## Test layers

### Backend tests

Backend tests live below each service's `src/test` directory and use Maven:

```bash
cd backend/auth-service && mvn test
cd backend/api-gateway && mvn test
```

Run `mvn test` from any other backend service directory when its tests are added. The current tests include auth refresh-token coverage, auth application context coverage, and gateway application context coverage. Tests that load Spring configuration need the configured dependencies available (or an appropriate test profile).

### Frontend checks

```bash
cd frontend
pnpm lint
pnpm build
```

The frontend currently uses TypeScript through the Next.js build and ESLint through the package script. There is no separate unit-test script in `frontend/package.json`.

### End-to-end tests

The Playwright project is in `tests/e2e`. Its base URL is `http://localhost:3000`, uses Chromium, retries once, and retains traces on failure.

Start the application, then run:

```bash
cd tests/e2e
pnpm install
pnpm test
```

Use `pnpm test:ui` for interactive debugging. The suite verifies login, dashboard navigation, equipment, maintenance, inspections, spare parts, notifications, and audit pages. The equipment-create scenario also checks that the equipment registration form can be populated.

## Test data

Development migrations seed users, roles, catalog data, equipment, inspections, downtime, faults, parts, and technicians. E2E tests currently log in with the seeded `admin` account and password `Admin123!ChangeMe`; keep those credentials out of production and rotate or replace them in shared environments.

## Troubleshooting failures

- A login or page-load failure usually means the frontend cannot reach the gateway, or the gateway cannot validate the configured JWT secret.
- Empty dashboards commonly indicate that the equipment/maintenance migrations have not run or the frontend is pointed at the wrong API URL.
- Event-driven assertions can lag behind the initiating request; check RabbitMQ queues and the notification/audit service logs.
- Attachment tests require a writable `FILE_STORAGE_PATH` and, in Docker, the `storage/` volume mount.
- If a Spring context test fails while connecting to infrastructure, verify PostgreSQL, Redis, and RabbitMQ host/port/password settings before changing application code.

## What to add with new features

Add or update the relevant backend unit/context test, a migration test or fixture when schema changes, and a Playwright scenario when the user-visible workflow changes. Verify both authorized and unauthorized roles for new endpoints.
