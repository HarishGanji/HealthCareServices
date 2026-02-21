# Notification Microservice Plan (Feign Integration)

## Objective
Create a separate `notification-service` microservice and integrate it with `HealthCareServices` using Feign client for event-based user notifications.

## Why a separate service
- Isolates notification channels (email/SMS/push) from core healthcare logic.
- Scales independently for high notification volume.
- Allows retries/queueing without blocking healthcare workflows.

## Proposed Architecture
- **Healthcare Service (current project)**
  - Emits notification requests through `NotificationServiceClient` (Feign).
  - Uses `NotificationGateway` wrapper for business-friendly calls.
- **Notification Service (new microservice)**
  - Receives notification requests via REST API.
  - Persists notification logs and delivery status.
  - Sends notifications via configured providers.

## Feign Contract (already prepared in healthcare service)
- Endpoint called by Feign:
  - `POST /notifications/send`
- Request payload:
  - `recipientEmail`, `subject`, `message`, `channel`
- Response payload:
  - `accepted`, `trackingId`, `status`

## Events to trigger notifications
1. **Online prescription request raised**
   - Notify doctor that patient requested follow-up online prescription.
2. **Online prescription answered**
   - Notify patient that prescription is ready to download.

## Notification Service API (to implement)
### `POST /notifications/send`
- Validates payload
- Creates notification log row
- Sends notification using channel provider
- Returns tracking status

### `GET /notifications/{trackingId}`
- Returns status for observability and troubleshooting

## Suggested Notification Service Data Model
- `notification_id` (UUID)
- `recipient`
- `subject`
- `message`
- `channel` (EMAIL/SMS/PUSH)
- `status` (QUEUED/SENT/FAILED)
- `failure_reason`
- `created_at`
- `sent_at`

## Configuration
In healthcare service:
- `services.notification.url` (default `http://localhost:9091`)

In notification-service:
- SMTP/SMS provider credentials
- Retry policy and delivery timeout settings

## Rollout Plan
1. Build notification-service skeleton with above API.
2. Add persistence + delivery adapters.
3. Run both services locally; point healthcare `services.notification.url` to notification-service.
4. Validate online prescription flow triggers notifications for doctor and patient.
5. Add resilience (retry/circuit breaker) and monitoring dashboards.
