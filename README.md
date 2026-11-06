# Surya Car Pool
This is the Surya Car Pool! First Application founded by Mr.Shanthappa

# Surya Car Pool

A simple Spring Boot + Thymeleaf application for car-pooling. This repository provides a minimal UI and backend for managing users, rides, and bookings. It includes a simple session-based login (username/password: `shan` / `shan`) and CRUD pages for the basic models.

---

## Table of contents

* [Project Overview](#project-overview)
* [Features](#features)
* [Tech stack / Requirements](#tech-stack--requirements)
* [Project Structure](#project-structure)
* [Quick Start — Run locally](#quick-start--run-locally)
* [Default credentials](#default-credentials)
* [Database & configuration](#database--configuration)
* [Templates & static files](#templates--static-files)
* [REST endpoints / controllers (summary)](#rest-endpoints--controllers-summary)
* [Model summary](#model-summary)
* [How to extend or change login](#how-to-extend-or-change-login)
* [Development tips](#development-tips)
* [Testing](#testing)
* [Contributing](#contributing)
* [License](#license)

---

## Project Overview

This project is intended as a small sample application to demonstrate:

* Spring Boot web application structure
* Thymeleaf templates + static assets
* Simple session-based authentication (demo purpose only)
* Basic persistence using Spring Data repositories
* CRUD pages for `User`, `Ride`, and `Booking` models

It is **not** production hardened. Use it as a learning/demo starter or scaffold.

---

## Features

* Login page (session-based) with a simple hard-coded credential for demo
* Pages to view and manage users, rides and bookings
* Controllers for web pages and REST-style handlers
* Simple, clean UI built with Bootstrap (via CDN)

---

## Tech stack / Requirements

* Java 26 (required)
* Maven 3.6+ (or mvnw wrapper included if present)
* Spring Boot (version specified in `pom.xml`)
* Thymeleaf templating

Make sure `JAVA_HOME` is set and `mvn` is available in your PATH, or use the included Maven wrapper if present.

---

## Project Structure (important files)

```
surya-car-pool/
├─ pom.xml
├─ src/main/java/com/surya/carpool/
│  ├─ SuryaCarPoolApplication.java
│  ├─ controller/
│  │  ├─ WebController.java
│  │  ├─ UserController.java
│  │  ├─ RideController.java
│  │  └─ BookingController.java
│  ├─ model/
│  │  ├─ User.java
│  │  ├─ Ride.java
│  │  └─ Booking.java
│  └─ repository/
│     ├─ UserRepository.java
│     ├─ RideRepository.java
│     └─ BookingRepository.java
├─ src/main/resources/
│  ├─ application.properties
│  ├─ templates/
│  │  ├─ index.html
│  │  ├─ login.html
│  │  ├─ users.html
│  │  ├─ rides.html
│  │  └─ bookings.html
│  └─ static/styles.css
└─ README.md (this file)
```

---

## Quick Start — Run locally

1. Open a terminal in the project root (`surya-car-pool/`).

2. Build the project with Maven:

```bash
mvn clean package
```

3. Run the application (one of the options below):

* Using Maven:

```bash
mvn spring-boot:run
```

* Using the packaged jar (after `mvn package`):

```bash
java -jar target/surya-car-pool-*.jar
```

4. Open a browser and go to:

```
http://localhost:8080/login
```

Login with the default credentials (see below).

---

## Default credentials

This demo app uses a simple hard-coded login check for quick testing. Use:

* **Username:** `shan`
* **Password:** `shan`

After logging in you will be redirected to the home page (`/`).

> **Security note:** This is for demo/development only. Do not use hard-coded credentials or this method in production. Replace with Spring Security and password hashing for any real application.

---

## Database & configuration

* Database configuration is in `src/main/resources/application.properties`.
* The project ships with a simple in-memory default or H2 configuration (check `application.properties`).
* If you want persistence to disk, configure a real DB (Postgres/MySQL) and update `application.properties`.

### Typical properties to check / change

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

If you switch to a different database, also add the appropriate JDBC driver dependency in `pom.xml`.

---

## Templates & static files

The Thymeleaf templates are in `src/main/resources/templates/`. The important pages are:

* `login.html` — login form
* `index.html` — home page (requires login)
* `users.html` — user listing / management
* `rides.html` — rides listing / management
* `bookings.html` — bookings listing / management

Static CSS is located at `src/main/resources/static/styles.css`.

---

## REST endpoints / controllers (summary)

The project contains controllers that map the main routes. The important ones include (non-exhaustive):

* `GET /login` — show login page
* `POST /doLogin` — handle login (session creation)
* `GET /logout` — invalidate session and redirect to login
* `GET /` — home page
* `GET /users`, `POST /users` — list/create users (see `UserController`)
* `GET /rides`, `POST /rides` — list/create rides (see `RideController`)
* `GET /bookings`, `POST /bookings` — list/create bookings (see `BookingController`)

Open the controller files to see method names and more specific routes.

---

## Model summary

* **User** — stores user profile details
* **Ride** — represents a ride offered by a user (origin, destination, seats, date/time)
* **Booking** — a user booking for a ride

See the `model` package classes for exact fields and validations.

---

## How to extend or change login

This project uses a simple demonstration login. To replace it with a more secure approach:

1. Add Spring Security dependency to `pom.xml`.
2. Create a `UserDetailsService` backed by `UserRepository`.
3. Store hashed passwords (BCrypt) for users instead of hard-coded values.
4. Configure form login, CSRF, route protection via `WebSecurityConfigurerAdapter` (or the newer `SecurityFilterChain` bean pattern for Spring Security 5.7+).

If you prefer to keep the simple approach for local demo, you can still move the `username`/`password` check to a properties file or environment variables for easier changes.

---

## Development tips

* When you change template files, Thymeleaf will usually reflect changes on refresh in `spring-boot:run`.
* If you add a new dependency in `pom.xml`, run `mvn clean package` before running.
* Use the browser dev tools to inspect network / HTML.

---

## Testing

No automated tests are included by default. You can add:

* Unit tests for services and controllers (JUnit 5)
* Integration tests using `@SpringBootTest`

Example:

```bash
mvn test
```

---

## Contributing

1. Create an issue describing the change you'd like to make.
2. Fork the repository and create a feature branch.
3. Open a pull request when your changes are ready.

Please keep commits small and documentation updated.

---

## License

This project has no specified license file. Add a LICENSE (e.g., MIT) if you plan to make it public.

---

## Contact / Author

Surya Car Pool — sample project

If you want specific help (e.g., migrate to Spring Security, add registration, add API doc), say what you want and I will update the project.
