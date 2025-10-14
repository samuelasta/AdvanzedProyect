🏠 Lodging Management API — Advanced Programming Project

University of Quindío — Systems and Computing Engineering Program

🎯 Project Overview

This project is a backend application built with Spring Boot for managing lodgings, reservations, and user interactions between guests and hosts.
It is designed with enterprise-grade development practices and a layered architecture to ensure scalability, maintainability, and clean separation of concerns.

⚙️ Tech Stack

Backend

Java 21

Spring Boot 3.x

Spring Web

Spring Data JPA

Spring Security (JWT)

Validation API

Lombok

Java Mail Sender

MariaDB

Gradle

Tools & Others

IntelliJ IDEA / VS Code

Postman / .http files for API testing

Swagger (optional API documentation)

🧩 Architecture

This project follows a layered architecture pattern, commonly used in enterprise systems.

controller/ → Manages REST endpoints (API layer)

service/ → Contains business logic

repository/ → Handles persistence using JPA

model/ → Defines entities and enums

dto/ → Data Transfer Objects (request/response)

config/ → Handles security, JWT, and CORS configuration

utils/ → General helper classes and reusable utilities

🧑‍💻 Main Features
👤 User

Register, log in, and authenticate via JWT.

Search available lodgings by city, price, date, and amenities.

Create, view, and cancel reservations.

Leave comments and ratings after completed stays.

Manage favorite lodgings.

Update personal profile information.

🏡 Host

CRUD operations for owned lodgings.

View booking metrics (number of reservations, average ratings).

Reply to user comments.

Soft delete for lodgings (status-based instead of hard deletion).

🔐 Security

JWT-based authentication for stateless API sessions.

Role-based authorization (ROLE_USER, ROLE_HOST).

Each request is validated by a custom JwtFilter.

When a user becomes inactive, their token is automatically invalidated.

📬 Notifications

Automatic email notifications for booking confirmations, cancellations, and password recovery.

Recovery codes expire after 15 minutes.
