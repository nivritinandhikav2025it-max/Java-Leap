# FilmJury – Short Film Contest Submission and Scoring System

A complete Spring Boot + MySQL + HTML/CSS/JavaScript CRUD project based on the provided use case.

## Features

- Login page with database-backed users
- Film Entry CRUD
- Judge CRUD
- Criterion CRUD
- Scorecard CRUD
- One scorecard per judge per entry business rule
- Automatic scorecard average
- Automatic final film score = average of all submitted judge scorecards
- Leaderboard
- MySQL database with JPA/Hibernate
- Validation and global error responses
- Responsive frontend
- Runs directly from IntelliJ IDEA as a Maven Spring Boot application

## Technology

- Java 17
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- Maven
- HTML5 / CSS3 / JavaScript

## IntelliJ setup

### 1. Requirements

Install:

1. JDK 17 or later
2. IntelliJ IDEA
3. MySQL 8.x

### 2. Create the database

Open MySQL Workbench or MySQL Command Line and run:

```sql
CREATE DATABASE filmjury;
```

The application is configured for:

- host: localhost
- port: 3306
- database: filmjury
- username: root
- password: root

If your MySQL root password is different, edit:

`src/main/resources/application.properties`

Change:

```properties
spring.datasource.password=root
```

to your actual MySQL password.

### 3. Open in IntelliJ

1. Extract `FilmJury.zip`.
2. Open IntelliJ IDEA.
3. Select **Open**.
4. Select the extracted `FilmJury` folder.
5. IntelliJ should detect `pom.xml` and import it as a Maven project.
6. Make sure the Project SDK is Java 17.
7. Wait for Maven dependencies to finish downloading.
8. Open `FilmJuryApplication.java`.
9. Click the green Run button.

### 4. Open the application

Go to:

`http://localhost:8080/login.html`

Demo accounts:

- Admin: `admin` / `admin123`
- Judge: `judge` / `judge123`

The first application startup automatically creates these accounts and the four default criteria.

## Default criteria

- Creativity – 10
- Originality – 10
- Technical – 10
- Presentation – 10

A scorecard total is:

`(Creativity + Originality + Technical + Presentation) / 4`

An entry final score is the average of all submitted scorecard totals for that entry.

## Main API endpoints

### Authentication

- POST `/api/auth/login`
- POST `/api/auth/logout`
- GET `/api/auth/me`

### Entries

- GET `/api/entries`
- GET `/api/entries/{id}`
- POST `/api/entries`
- PUT `/api/entries/{id}`
- DELETE `/api/entries/{id}`

### Judges

- GET `/api/judges`
- POST `/api/judges`
- PUT `/api/judges/{id}`
- DELETE `/api/judges/{id}`

### Criteria

- GET `/api/criteria`
- POST `/api/criteria`
- PUT `/api/criteria/{id}`
- DELETE `/api/criteria/{id}`

### Scorecards

- GET `/api/scorecards`
- POST `/api/scorecards`
- PUT `/api/scorecards/{id}`
- DELETE `/api/scorecards/{id}`

## Important business rule

The service layer rejects a second scorecard when the same judge tries to score the same entry again.

The database also has a unique constraint on:

`judge_id + entry_id`

This gives both application-level and database-level protection.

## If Maven or Java errors appear

Use:

- File → Project Structure → Project SDK → JDK 17
- Maven tool window → Reload All Maven Projects

Then run `FilmJuryApplication`.

