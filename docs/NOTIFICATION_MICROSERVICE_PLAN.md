# Notification Service Microservice Plan

## Goal
Decouple notification concerns from the healthcare core service by introducing a dedicated `notification-service` microservice and connecting it through Feign.

## Architecture
- **healthcare-service** (this project)
  - Owns business workflows (appointments, online prescriptions, etc.)
  - Publishes notification events via Feign client.
- **notification-service** (new microservice)
  - Receives event requests and sends notifications using email/SMS/push providers.
  - Stores delivery audit records.

## Integration Pattern
1. Healthcare flow completes a domain action (e.g., online prescription request created).
2. Healthcare service calls notification-service via Feign `POST /notifications/send`.
3. Notification-service sends configured channel notifications and responds success/failure.
4. Healthcare service continues business flow with best-effort notifications (non-blocking on failures).

## Notification Event Contract
`POST /notifications/send`

Request body:
```json
{
  "eventType": "ONLINE_PRESCRIPTION_REQUEST_CREATED",
  "patientId": "uuid",
  "doctorId": "uuid",
  "message": "A patient has requested online prescription follow-up."
}
```

## Events to Start With
- `ONLINE_PRESCRIPTION_REQUEST_CREATED`
- `ONLINE_PRESCRIPTION_ISSUED`
- `APPOINTMENT_BOOKED`
- `APPOINTMENT_CANCELLED`
- `APPOINTMENT_RESCHEDULED`

## Suggested notification-service APIs
- `POST /notifications/send`
- `GET /notifications/history/{entityId}`
- `GET /notifications/health`

## Resilience Recommendations
- Add retries + circuit breaker in healthcare service Feign client.
- Keep notification calls best-effort so business transaction does not fail if notification-service is down.
- Add dead-letter queue if moving to async messaging later.

## Future Upgrade (Async)
After Feign-based MVP, move to event streaming (Kafka/RabbitMQ):
- Healthcare publishes domain events.
- Notification-service consumes events asynchronously.
- Better reliability and scalability for high throughput.
