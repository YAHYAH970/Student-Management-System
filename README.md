<<<<<<< HEAD
# Student Management System (Java + MySQL)

A beginner-friendly college project with a browser UI, Java Spring Boot REST API, and a relational MySQL database.

## Features
- Admin login with a BCrypt-hashed password and server-side session
- Add, view, edit, delete and search students
- Course management
- Semester result entry and pass-status display
- Dashboard counts
- Database constraints: unique roll number/email/course code, foreign keys, duplicate-result prevention and indexes
- Input validation for email, year, marks and credits

## Technology
- Java 17
- Spring Boot 3.3.5, Spring Web, Spring Data JPA, Bean Validation
- MySQL 8+
- HTML, CSS and vanilla JavaScript
- Maven

## Setup (Windows)
1. Install **JDK 17**, **MySQL Server**, and **Maven** (or use the Maven wrapper provided by your IDE).
2. Open MySQL Workbench and run `database/schema.sql`, or let the app create the database using the connection URL.
3. Open `src/main/resources/application.properties`. Set your MySQL username/password, or use environment variables:
   - `DB_USER` (default `root`)
   - `DB_PASSWORD` (default empty)
4. Open the project folder in IntelliJ IDEA or VS Code.
5. Run `StudentManagementApplication.java`, or in a terminal run:
   ```bash
   mvn spring-boot:run
   ```
6. Open http://localhost:8080

## Demo login
- Username: `admin`
- Password: `Admin@123`

The first launch creates the demo admin if it does not exist. Passwords are stored as BCrypt hashes. **Change the demo password before using this project outside a classroom demo.** Do not deploy the demo credentials publicly.

## Suggested demo sequence
1. Sign in.
2. Add a student with a unique roll number and email.
3. Search by name or roll number; edit the record.
4. Add a course (e.g. `JAVA101`, `Java Programming`, 4 credits).
5. Record marks for a student and course.
6. Refresh the page to show that records persist in MySQL.
7. Explain the `students`, `courses`, `results` and `admins` tables, unique constraints and foreign keys.

## Database design
- `admins`: login name and password hash.
- `students`: student profile; unique roll number and email.
- `courses`: unique course code and credit count.
- `results`: connects a student to a course for a semester; foreign keys and unique `(student_id, course_id, semester)` combination prevent orphan/duplicate records.

Spring Data JPA generates/updates the tables from entity classes (`spring.jpa.hibernate.ddl-auto=update`). `database/schema.sql` documents the intended design.

## API overview
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/students?q=search-term`
- `POST /api/students`
- `PUT /api/students/{id}`
- `DELETE /api/students/{id}`
- `GET/POST /api/courses`
- `GET/POST /api/results`

All data APIs require an admin session. This is an educational project scaffold; before production use add CSRF protection, login rate limiting, stricter exception handling, audit logs, and a managed secret/password process.


## Student Portal (added)
- Open `http://localhost:8080/student.html` for student sign-in.
- Student login uses the exact email stored in the Admin Portal and the student's roll number as a **temporary demo password**.
- After sign-in, students can see only their own profile and results. Admin login remains at `http://localhost:8080/`.
- This temporary password approach is for local demonstration only. Before real deployment, replace it with individually set, hashed passwords and a password-reset/change flow.
=======
# Student-Management-System
A Student Management System built using Java, Spring Boot, MySQL, HTML, CSS, and JavaScript. Includes student records management, an admin dashboard, and a student portal for managing academic information.
>>>>>>> 234f3a3a2a634dd20c404454f5bb82957f0ca4fb
