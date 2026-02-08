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
1. **Register**: `POST /auth/register` creates a user and returns a JWT token.
2. **Login**: `POST /auth/login` validates credentials and returns a JWT token.
3. **Protected Requests**: Include `Authorization: Bearer <token>` for all non-auth endpoints.

## Typical Workflow
1. **Admin setup**
   - Create or manage departments.
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

## Environment Profiles
- **Default profile** uses an in-memory H2 database (`application.yml`).
- **Local profile** uses PostgreSQL (`application-local.yml`). Enable with
  `spring.profiles.active=local`.
