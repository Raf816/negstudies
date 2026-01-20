# Device Reservation Service

Spring Boot microservice for managing devices and their reservations. The service is intentionally simple but production-minded: it includes validation, error handling, and Docker support to help you demonstrate software-engineering and cloud-deployment practices.

## Features
- Create, update, list, and delete devices.
- Reserve devices for date ranges with conflict detection.
- Cancel reservations.
- Standardised error responses and validation.

## Tech Stack
- Java 21 + Spring Boot 3
- Spring Web, Spring Validation, Spring Data JPA
- H2 (in-memory) for local development
- PostgreSQL support for containerised deployments

## Running Locally (H2)
```bash
./mvnw spring-boot:run
```
The API will be available at `http://localhost:8080`.

## Running with Docker Compose (PostgreSQL)
```bash
docker compose up --build
```
The application will start on `http://localhost:8080` and connect to PostgreSQL.

## API Endpoints
### Devices
- `POST /api/devices` - create a device
- `GET /api/devices` - list devices
- `GET /api/devices/{id}` - get device details
- `PUT /api/devices/{id}` - update device
- `DELETE /api/devices/{id}` - delete device

Example payload:
```json
{
  "name": "MacBook Pro 14",
  "serialNumber": "MBP-2024-001",
  "model": "Apple M3 Pro",
  "status": "AVAILABLE"
}
```

### Reservations
- `POST /api/devices/{id}/reservations` - create a reservation for a device
- `GET /api/reservations` - list all reservations
- `GET /api/reservations?deviceId={id}` - list reservations for a device
- `GET /api/reservations/{id}` - get reservation details
- `PATCH /api/reservations/{id}/cancel` - cancel a reservation

Example payload:
```json
{
  "reservedBy": "Ava Reynolds",
  "startDate": "2025-02-10",
  "endDate": "2025-02-12",
  "purpose": "Mobile testing sprint"
}
```

## Notes for AWS Deployment Evidence
- Use Docker images built from the provided `Dockerfile`.
- Replace H2 with RDS PostgreSQL in AWS.
- Capture evidence from AWS (ECS/ECR/RDS, VPC, security groups) and CI/CD pipeline runs.
- Document environment variables used for deployment and the production database connection details (without secrets).