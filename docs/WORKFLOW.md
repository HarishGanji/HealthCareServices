# HealthCare Services Workflow Guide

## Overview
This project is a Spring Boot healthcare backend that supports authentication, user roles,
appointments, doctor availability, prescriptions, and profile management. It uses JWT-based
authentication with role-based authorization at the controller layer and JPA for persistence.

## Core Roles and Privileges
- **ADMIN**: Full access to administrative and management endpoints. Can manage departments,
  assign doctors/admins, and perform administrative profile actions.
- **DOCTOR**: Manages doctor profile, availability, and updates appointment status. Can create
  prescriptions for patients and view doctor-specific appointments.
- **PATIENT**: Manages patient profile, books/reschedules/cancels appointments, and views
  patient-specific prescriptions and appointment history.

## Authentication Flow
1. **Register**: `POST /auth/register` creates a user and returns registration metadata (no JWT).
2. **Login**: `POST /auth/login` validates credentials and returns a JWT token.
3. **Protected Requests**: Include `Authorization: Bearer <token>` for all non-auth endpoints.

## Typical Workflow
1. **Admin setup**
   - Create departments and assign a head doctor.
   - Assign doctors and administrators to departments.
2. **Doctor setup**
   - Complete doctor profile.
   - Publish availability slots.
3. **Patient onboarding**
   - Complete patient profile and address.
   - Browse doctors and availability.
4. **Appointments**
   - Patients book appointments with doctors.
   - Doctors update appointment status after visits.
5. **Prescriptions**
   - Doctors create prescriptions for patients.
   - Patients view prescriptions.

## Role-to-Feature Matrix
| Feature Area | ADMIN | DOCTOR | PATIENT |
| --- | --- | --- | --- |
| Department management | ✅ create/assign | 🔍 view | 🔍 view |
| Doctor profile | ✅ manage | ✅ manage | 🔍 view specialization |
| Patient profile | ✅ manage | 🔍 view | ✅ manage |
| Appointments | ✅ view all / update | ✅ view / update status | ✅ book/reschedule/cancel |
| Availability | ✅ manage | ✅ manage | 🔍 view |
| Prescriptions | ✅ manage | ✅ create/view | ✅ view |
| Address management | ✅ manage | ✅ manage | ✅ manage |

## API Workflow (End-to-End)
1. **Register**
   - `POST /auth/register`
   - Creates a user and role-specific profile (patient/doctor/admin).
2. **Login**
   - `POST /auth/login`
   - Returns a JWT token used for all protected endpoints.
3. **Admin configuration**
   - `POST /auth/department`
   - `POST /auth/assign-admin/{departmentId}/{adminId}`
   - `POST /auth/assigndoctor/{departmentId}/{doctorId}`
   - `POST /auth/head-doctor/{departmentId}/{doctorId}`
4. **Doctor availability**
   - `POST /availability/doctor/{doctorId}`
   - `GET /availability/doctor/{doctorId}`
5. **Patient scheduling**
   - `POST /appointment/appointment/{patientId}/{doctorId}?appointmentDateTime=...`
   - `PATCH /appointment/{appointmentId}/reschedule?appointmentDateTime=...`
   - `PATCH /appointment/{appointmentId}/cancel`
6. **Visit completion**
   - `PATCH /appointment/{appointmentId}/status?status=COMPLETED`
7. **Prescription**
   - `POST /prescriptions/doctor/{doctorId}/patient/{patientId}`
   - `GET /prescriptions/patient/{patientId}`

8. **Online prescription extension (follow-up)**
   - `POST /online-prescriptions/request/{appointmentId}?patientId=...&questions=...`
   - `PATCH /online-prescriptions/{requestId}/respond?doctorId=...`
   - `GET /online-prescriptions/{requestId}/pdf?patientId=...`

## Swagger / OpenAPI Usage
- Open Swagger UI at `/swagger-ui.html`.
- Click **Authorize** and paste: `Bearer <token>`.
- Use the JWT from `/auth/login` for protected endpoints.

## Environment Profiles
- **Default profile** uses an in-memory H2 database (`application.yml`).
- **Local profile** uses PostgreSQL (`application-local.yml`). Enable with
  `spring.profiles.active=local`.
