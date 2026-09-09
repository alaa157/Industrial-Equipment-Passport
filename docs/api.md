# API reference

All public endpoints are exposed through the gateway at `http://localhost:8080`. The API prefix is `/api/v1`. Except for login and refresh, send:

```http
Authorization: Bearer <access-token>
Content-Type: application/json
```

UUIDs are serialized as strings, timestamps are ISO-8601 UTC values, and paged Spring responses contain `content`, `number`, `size`, `totalElements`, and related page metadata.

## Authentication and users

| Method | Endpoint | Access | Body/query |
| --- | --- | --- | --- |
| POST | `/auth/login` | Public | `{ "usernameOrEmail": "...", "password": "..." }` |
| POST | `/auth/refresh` | Public | `{ "refreshToken": "..." }` |
| POST | `/auth/logout` | Public | `{ "refreshToken": "..." }` |
| GET | `/users/me` | Authenticated | Current user |
| GET | `/users` | `ADMIN` | All users |
| POST | `/admin/users` | `ADMIN` | `username`, `email`, `password`, `roles` |
| PATCH | `/admin/users/{id}/roles` | `ADMIN` | `{ "roles": ["TECHNICIAN"] }` |
| PATCH | `/admin/users/{id}/enabled?enabled=true` | `ADMIN` | Query flag |
| GET | `/admin/permissions` | `ADMIN` | Role-to-permission map |
| GET | `/admin/roles` | `ADMIN` | Role names |

Login and refresh return `accessToken`, `refreshToken`, `expiresInSeconds`, `userId`, `username`, and `roles`. Access tokens also carry roles and resolved permission codes.

## Equipment

| Method | Endpoint | Access | Notes |
| --- | --- | --- | --- |
| POST | `/equipment` | `ADMIN`, `MANAGER` | Create equipment; required fields include asset/serial number, name, manufacturer, model, `equipmentTypeId`, and `criticality` |
| GET | `/equipment` | Authenticated | Filters: `query`, `status`, `criticality`, `siteId`, `page`, `size`, `sort`, `direction` |
| GET | `/equipment/dashboard` | Authenticated | Aggregate equipment metrics |
| GET | `/equipment/{id}` | Authenticated | Equipment detail |
| PATCH | `/equipment/{id}` | `ADMIN`, `MANAGER` | Update editable descriptive fields |
| PATCH | `/equipment/{id}/status` | `ADMIN`, `MANAGER`, `TECHNICIAN` | `{ "status": "ACTIVE" }` |
| GET | `/equipment/{id}/qr` | Authenticated | QR image bytes |
| POST | `/equipment/{id}/inspections` | `ADMIN`, `MANAGER`, `INSPECTOR` | Inspection date, checklist, result, optional notes/next date |
| GET | `/equipment/{id}/inspections` | Authenticated | Inspection history |
| POST | `/equipment/{id}/downtime` | `ADMIN`, `MANAGER` | Reason, start/end, optional maintenance ID and notes |
| GET | `/equipment/{id}/downtime` | Authenticated | Downtime history |
| POST | `/equipment/{id}/faults` | `ADMIN`, `MANAGER`, `TECHNICIAN`, `INSPECTOR` | Title, description, severity |
| GET | `/equipment/{id}/faults` | Authenticated | Fault history |

Catalog endpoints are `GET /equipment/catalog/types`, `GET /equipment/catalog/sites`, `GET /equipment/catalog/buildings?siteId={id}`, and `GET /equipment/catalog/areas?buildingId={id}`.

Equipment attachments use multipart form data with a `file` part: `POST /equipment/{equipmentId}/attachments`, `GET` the same path to list, `GET /{attachmentId}/download` to download, and `DELETE /{attachmentId}` to remove.

## Spare parts

`GET /equipment/parts` lists parts, `GET /equipment/parts/low-stock` lists parts at or below minimum quantity, and `POST /equipment/parts` creates a part. Creation requires `partNumber`, `name`, `quantity`, `minimumQuantity`, and non-negative `unitCost`.

Consume inventory with `POST /equipment/parts/{id}/consume?quantity=1&equipmentId={equipmentId}&maintenanceId={optional-id}`. Installed parts and replacement history are available through `GET /equipment/parts/{partId}/equipment/{equipmentId}` and `GET /equipment/parts/equipment/{equipmentId}/history`.

## Maintenance

| Method | Endpoint | Access | Notes |
| --- | --- | --- | --- |
| POST | `/maintenance` | `ADMIN`, `MANAGER`, `TECHNICIAN` | Equipment ID, title, description, type, priority, optional due date |
| GET | `/maintenance` | Authenticated | Optional `status`, `technicianId` filters |
| GET | `/maintenance/search` | Authenticated | Optional `query`, `status`, `priority`, `technicianId`, `page`, `size` |
| GET | `/maintenance/dashboard` | Authenticated | Aggregate maintenance metrics |
| GET | `/maintenance/{id}` | Authenticated | Detail |
| PATCH | `/maintenance/{id}/assign` | `ADMIN`, `MANAGER`, `TECHNICIAN` | `{ "technicianId": "..." }` |
| POST | `/maintenance/{id}/start` | `ADMIN`, `MANAGER`, `TECHNICIAN` | Move to in-progress |
| POST | `/maintenance/{id}/hold` | `ADMIN`, `MANAGER`, `TECHNICIAN` | Put on hold |
| POST | `/maintenance/{id}/complete` | `ADMIN`, `MANAGER`, `TECHNICIAN` | Duration, cost, labor notes, completion notes |
| POST | `/maintenance/{id}/cancel` | `ADMIN`, `MANAGER`, `TECHNICIAN` | Cancel request |

Maintenance attachments use `POST /maintenance/{maintenanceId}/attachments` with a multipart `file`, `GET` to list, and `GET /{attachmentId}/download` to retrieve.

Technicians are listed with `GET /technicians`; `POST /technicians` is restricted to `ADMIN` and `MANAGER`.

## Notifications and audit

- `GET /notifications` lists broadcast and current-user notifications.
- `GET /notifications/unread-count` returns the unread count.
- `POST /notifications/{id}/read` marks a notification read; users cannot mark another user's private notification read.
- `GET /audit?page=0&size=50` returns newest-first audit events. Page size is capped at 200 and access is limited to `ADMIN`, `MANAGER`, and `VIEWER`.

## Errors and health

Validation failures, missing records, access denials, and service errors are returned as JSON by the relevant service exception handler. Clients should use the HTTP status and message rather than assuming every error has the same field set.

The gateway and each backend expose Actuator endpoints under `/actuator`, including `/actuator/health`, `/actuator/info`, and `/actuator/prometheus`. These are operational endpoints and should be protected or isolated in production.
