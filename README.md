# Student Management System

A comprehensive Spring Boot application for managing students, teachers, courses, and departments with role-based access control.

## 🚀 Features

- **User Authentication & Authorization**
  - Role-based access (Student & Teacher)
  - Secure login with Spring Security
- **Student Management**
  - Student profiles with personal information
  - Course enrollment tracking
  - Department assignment

- **Teacher Management**
  - Teacher profiles with specialization
  - Course assignments
  - Department association

- **Course Management**
  - Create and manage courses
  - Assign teachers to courses
  - Track course details (code, name, description, credits)

- **Department Management**
  - Organize students and teachers by department
  - Department information management

## 🛠 Technologies Used

- **Backend**: Spring Boot 4.0.2
- **Security**: Spring Security 7
- **Database**: PostgreSQL 16
- **ORM**: Spring Data JPA / Hibernate
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Testing**: JUnit 5, Mockito, Spring Boot Test

## 📋 Prerequisites

- Java 21
- Docker Desktop
- Maven 3.9+ (optional if using Docker)
- Git

## 🚀 Quick Start

### Using Docker (Recommended)

1. **Clone the repository**

   ```bash
   git clone https://github.com/niloy2107028/soft_lab_sms.git
   cd soft_lab_sms
   ```

2. **Start the application**

   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - Open browser: http://localhost:8081
   - Use demo credentials below

### Using Maven (Local)

1. **Configure PostgreSQL**
   - Install PostgreSQL
   - Create database: `student_management`
2. **Update application.properties**

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/student_management
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## 👤 Demo Credentials

### Teacher Account

- Username: `teacher1`
- Password: `password123`

### Student Account

- Username: `student1`
- Password: `password123`

## 🧪 Running Tests

### Run all tests

```bash
mvn test
```

### Run specific test

```bash
# Unit tests
mvn test -Dtest=UserServiceTest

# Integration tests
mvn test -Dtest=AuthControllerIntegrationTest
```

### Test Coverage

See [BEGINNER_TEST_GUIDE.md](docs/testing/BEGINNER_TEST_GUIDE.md) for detailed testing documentation and [RUN_TESTS.txt](docs/testing/RUN_TESTS.txt) for quick commands.

## 🏗 Project Structure

```
niloy/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/student_management_system/niloy/
│   │   │       ├── config/          # Security & Data initialization
│   │   │       ├── controller/      # REST & Web controllers
│   │   │       ├── model/           # Entity classes
│   │   │       ├── repository/      # JPA repositories
│   │   │       └── service/         # Business logic
│   │   └── resources/
│   │       ├── templates/           # Thymeleaf HTML templates
│   │       ├── static/              # CSS, JS files
│   │       └── application.properties
│   └── test/
│       └── java/                    # Unit & Integration tests
├── docker-compose.yml               # Docker configuration
├── Dockerfile                       # Application container
└── pom.xml                          # Maven dependencies
```

## 📝 API Endpoints

### Authentication

- `GET /login` - Login page
- `POST /login` - Process login
- `GET /logout` - Logout

### Student Routes

- `GET /student/dashboard` - Student dashboard
- `GET /student/profile` - View profile
- `PUT /student/profile` - Update profile

### Teacher Routes

- `GET /teacher/dashboard` - Teacher dashboard
- `GET /teacher/profile` - View profile
- `PUT /teacher/profile` - Update profile

### Admin Routes (Teacher)

- `GET /manage-students` - Manage students
- `GET /manage-teachers` - Manage teachers
- `GET /manage-courses` - Manage courses
- `GET /manage-departments` - Manage departments

## 🐳 Docker Configuration

The application uses Docker Compose with two services:

1. **PostgreSQL Database**
   - Port: 5432
   - Database: student_management
   - Credentials: postgres/postgres

2. **Spring Boot Application**
   - Port: 8081 (host) → 8080 (container)
   - Depends on PostgreSQL
   - Auto-restarts on failure

## 🔐 Security Features

- Password encryption using BCrypt
- Role-based access control (RBAC)
- CSRF protection (disabled for development)
- Secure session management
- Protected endpoints based on user roles

## 🚀 CI/CD

GitHub Actions workflow automatically:

- Runs tests on every push/PR
- Builds the application
- Reports test results

See `.github/workflows/ci.yml` for details.

## 📚 Additional Documentation

### Testing

- [Beginner's Test Guide](docs/testing/BEGINNER_TEST_GUIDE.md) - Complete guide to understanding and running tests
- [Quick Test Commands](docs/testing/RUN_TESTS.txt) - Command reference for running tests

### GitHub & CI/CD

- [GitHub Actions Explained](docs/github/GITHUB_ACTIONS_EXPLAINED.md) - How cloud testing works
- [Branch Protection Guide](docs/github/BRANCH_PROTECTION_GUIDE.md) - Setting up branch protection rules

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Niloy**

- GitHub: [@niloy2107028](https://github.com/niloy2107028)

## 🙏 Acknowledgments

- Spring Boot Documentation
- Stack Overflow Community
- GitHub Community
