# Employee Data Processing & Notification System

## 📌 Project Overview

The **Employee Data Processing & Notification System** is a Spring Boot-based backend application designed to process employee data from Excel files using **Spring Batch**.

The system allows users to upload an Excel file containing employee records. Spring Batch processes the records in chunks, validates and transforms the data, stores valid employees in the database, and publishes employee events through RabbitMQ.

The project follows a **microservices-based architecture** consisting of three independent services:

* Employee Service
* Notification Service
* Audit Service

The project demonstrates real-world backend concepts such as:

* Spring Boot
* Spring Batch
* Spring Data JPA
* Hibernate
* REST APIs
* Excel Processing
* Apache POI
* MySQL
* RabbitMQ
* Asynchronous Communication
* Chunk Processing
* Retry Mechanism
* Dead Letter Queue (DLQ)
* Swagger API Documentation
* Logging and Exception Handling

---

# 🏗️ System Architecture

```text
                    CLIENT / POSTMAN
                           │
                           │ REST API
                           ▼
                  ┌───────────────────┐
                  │ Employee Service  │
                  └───────────────────┘
                           │
                           │ Upload Excel
                           ▼
                  ┌───────────────────┐
                  │   Spring Batch    │
                  │                   │
                  │ JobLauncher       │
                  │       ↓           │
                  │ Job               │
                  │       ↓           │
                  │ Step              │
                  │       ↓           │
                  │ Reader            │
                  │       ↓           │
                  │ Processor         │
                  │       ↓           │
                  │ Writer            │
                  └───────────────────┘
                           │
                           ▼
                        MySQL
                           │
                           │ EmployeeCreated Event
                           ▼
                  ┌───────────────────┐
                  │     RabbitMQ      │
                  │     Exchange      │
                  └───────────────────┘
                       │         │
                       │         │
                       ▼         ▼
            ┌──────────────┐ ┌──────────────┐
            │ Notification │ │ Audit Service│
            │   Service    │ │              │
            └──────────────┘ └──────────────┘
```

---

# 🎯 Project Objectives

The main objective of this project is to understand a complete enterprise-level data processing pipeline.

This project demonstrates:

* Uploading employee data through Excel files.
* Processing large amounts of data using Spring Batch.
* Chunk-based processing.
* Data validation and transformation.
* Database persistence.
* Error handling and failed record tracking.
* Asynchronous communication using RabbitMQ.
* Event-driven microservices architecture.
* Retry and Dead Letter Queue handling.
* API documentation using Swagger.

---

# 🛠️ Technology Stack

| Technology        | Purpose                 |
| ----------------- | ----------------------- |
| Java 17+          | Programming Language    |
| Spring Boot       | Backend Framework       |
| Spring Data JPA   | Database Persistence    |
| Hibernate         | ORM Framework           |
| Spring Batch      | Batch Processing        |
| Apache POI        | Excel File Processing   |
| MySQL             | Database                |
| RabbitMQ          | Message Broker          |
| Spring AMQP       | RabbitMQ Integration    |
| Maven             | Dependency Management   |
| Lombok            | Reduce Boilerplate Code |
| Swagger / OpenAPI | API Documentation       |
| Postman           | API Testing             |
| Git & GitHub      | Version Control         |

---

# 📦 Microservices

## 1️⃣ Employee Service

The Employee Service is the main service responsible for:

* Employee CRUD operations.
* Excel file upload.
* File validation.
* Spring Batch job execution.
* Employee data processing.
* Database persistence.
* Publishing employee events to RabbitMQ.
* Import job tracking.

### Main APIs

```text
POST   /api/employees
GET    /api/employees
GET    /api/employees/{id}
PUT    /api/employees/{id}
DELETE /api/employees/{id}
```

### Excel Import API

```text
POST /api/employees/import
```

Content-Type:

```text
multipart/form-data
```

Parameter:

```text
file
```

---

## 2️⃣ Notification Service

The Notification Service consumes employee events from RabbitMQ.

Responsibilities:

* Listen for employee-created events.
* Process notification messages.
* Create notification records.
* Simulate email or notification delivery.
* Acknowledge successfully processed messages.

Queue:

```text
employee.notification.queue
```

---

## 3️⃣ Audit Service

The Audit Service consumes employee events independently.

Responsibilities:

* Listen for employee events.
* Store audit information.
* Track employee activity.
* Record timestamps and event types.

Queue:

```text
employee.audit.queue
```

---

# 📊 Excel File Format

The uploaded Excel file should follow this structure:

| employeeId | name  | email                                     | department | salary |
| ---------- | ----- | ----------------------------------------- | ---------- | ------ |
| 101        | Rahul | [rahul@gmail.com](mailto:rahul@gmail.com) | IT         | 50000  |
| 102        | Priya | [priya@gmail.com](mailto:priya@gmail.com) | HR         | 45000  |
| 103        | Arun  | [arun@gmail.com](mailto:arun@gmail.com)   | Finance    | 60000  |

### Validation Rules

| Field      | Validation                |
| ---------- | ------------------------- |
| employeeId | Unique and Required       |
| name       | Cannot be Empty           |
| email      | Valid Email Format        |
| department | Valid Department          |
| salary     | Must be Greater than Zero |

---

# 🔄 Spring Batch Processing Flow

The application processes employee records using Spring Batch.

```text
Excel File
     │
     ▼
ItemReader
     │
     ▼
Employee Object
     │
     ▼
ItemProcessor
     │
     ├── Validate Data
     ├── Transform Data
     └── Normalize Values
     │
     ▼
ItemWriter
     │
     ▼
MySQL Database
```

---

# 📦 Chunk Processing

Spring Batch processes large files in chunks.

Example:

```text
READ 100 Records
        ↓
PROCESS 100 Records
        ↓
WRITE 100 Records
        ↓
COMMIT TRANSACTION
        ↓
REPEAT
```

This improves:

* Performance
* Memory Management
* Transaction Handling
* Error Recovery

---

# ⚠️ Error Handling

The system supports fault-tolerant batch processing.

### Features

* Skip invalid records.
* Retry temporary failures.
* Rollback failed transactions.
* Restart failed jobs.
* Track failed records.

Failed records can store:

```text
Row Number
Employee Data
Failure Reason
Timestamp
```

---

# 📈 Import Job Tracking

Users can track the status of an Excel import job.

Example fields:

```text
jobId
fileName
status
totalRecords
processedRecords
failedRecords
startTime
endTime
```

### APIs

```text
GET /api/import/{jobId}

GET /api/import/{jobId}/errors
```

### Possible Job Status

```text
STARTED
PROCESSING
COMPLETED
FAILED
```

---

# 🐇 RabbitMQ Architecture

The project uses RabbitMQ for asynchronous communication between services.

```text
Employee Saved
      │
      ▼
EmployeeCreatedEvent
      │
      ▼
RabbitTemplate
      │
      ▼
employee.exchange
      │
      ├───────────────────────────┐
      │                           │
      ▼                           ▼
employee.notification.queue    employee.audit.queue
      │                           │
      ▼                           ▼
Notification Service          Audit Service
```

---

# 🔑 RabbitMQ Configuration

### Exchange

```text
employee.exchange
```

### Routing Key

```text
employee.created
```

### Queues

```text
employee.notification.queue

employee.audit.queue

employee.error.queue
```

### Recommended Exchange Type

```text
Topic Exchange
```

---

# 🔁 Message Processing Flow

```text
RabbitMQ
    │
    ▼
Consumer
    │
    ├── Success → ACK
    │
    ├── Failure → Retry
    │
    └── Repeated Failure → DLQ
```

The system avoids infinite retry loops by using bounded retries.

---

# 🗄️ Database Tables

## Employee

```text
employee
```

Fields:

```text
id
employee_id
name
email
department
salary
created_at
updated_at
```

---

## Import Job

```text
import_job
```

Fields:

```text
id
file_name
status
total_records
processed_records
failed_records
start_time
end_time
```

---

## Import Error

```text
import_error
```

Fields:

```text
id
job_id
row_number
employee_data
error_message
created_at
```

---

## Notification

```text
notification
```

Fields:

```text
id
employee_id
email
message
status
created_at
```

---

## Audit

```text
audit
```

Fields:

```text
id
event_type
employee_id
message
created_at
```

---

# 📂 Project Structure

```text
Employee-Data-Processing-System
│
├── employee-service
│   │
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   │
│   ├── batch
│   │   ├── BatchConfig
│   │   ├── EmployeeReader
│   │   ├── EmployeeProcessor
│   │   ├── EmployeeWriter
│   │   └── JobCompletionListener
│   │
│   └── rabbitmq
│       ├── RabbitMQConfig
│       └── EmployeeEventPublisher
│
├── notification-service
│   │
│   └── rabbitmq
│       └── EmployeeEventConsumer
│
├── audit-service
│   │
│   └── rabbitmq
│       └── EmployeeEventConsumer
│
└── README.md
```

---

# 🚀 Getting Started

## Prerequisites

Make sure the following software is installed:

* Java 17 or higher
* Maven
* MySQL
* RabbitMQ
* Postman
* Git

---

# ▶️ Running the Application

### Step 1: Clone the Repository

```bash
git clone <repository-url>
```

### Step 2: Navigate to the Project

```bash
cd Employee-Data-Processing-System
```

### Step 3: Configure Database

Create the MySQL database:

```sql
CREATE DATABASE employee_db;
```

Configure your database credentials in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

# 🐇 RabbitMQ Setup

Start RabbitMQ before running the microservices.

Default RabbitMQ ports:

```text
AMQP Port: 5672

Management UI: 15672
```

Default Management URL:

```text
http://localhost:15672
```

---

# 🧪 Testing

The application can be tested using Postman.

### Test Cases

| Test                  | Expected Result                              |
| --------------------- | -------------------------------------------- |
| Valid Excel File      | Records processed successfully               |
| Invalid Email         | Record rejected                              |
| Duplicate Employee ID | Validation error                             |
| Empty Required Field  | Validation error                             |
| Large Excel File      | Processed in chunks                          |
| Batch Failure         | Job failure tracked                          |
| RabbitMQ Success      | Consumer ACK                                 |
| Consumer Failure      | Retry occurs                                 |
| Repeated Failure      | Message moves to DLQ                         |
| Multiple Consumers    | Notification and Audit process independently |

---

# 📖 API Documentation

Swagger/OpenAPI will document:

* Employee CRUD APIs
* Excel Import API
* Import Job Status API
* Failed Record API
* Request and Response Examples
* Validation Errors

---

# 🔮 Future Enhancements

Possible improvements include:

* Email notification integration.
* Real-time WebSocket notifications.
* Docker containerization.
* Kubernetes deployment.
* Distributed tracing.
* Monitoring with Prometheus and Grafana.
* CI/CD pipeline.
* Cloud deployment.
* Role-based security.
* API Gateway integration.

---

# 🎓 Learning Outcomes

After completing this project, developers will understand:

* Spring Batch architecture.
* Job and Step configuration.
* JobLauncher and JobRepository.
* ItemReader, ItemProcessor and ItemWriter.
* Chunk processing.
* Transaction management.
* Skip and Retry mechanisms.
* Batch restart concepts.
* RabbitMQ Producer and Consumer architecture.
* Exchange, Queue and Routing Key concepts.
* Dead Letter Queue handling.
* Asynchronous microservice communication.
* Integration of Spring Batch, MySQL and RabbitMQ.

---

# 🔄 Complete End-to-End Flow

```text
1. User uploads employees.xlsx
              │
              ▼
2. Employee Service receives the file
              │
              ▼
3. Spring Batch Job starts
              │
              ▼
4. ItemReader reads Excel rows
              │
              ▼
5. ItemProcessor validates and transforms data
              │
              ▼
6. ItemWriter stores valid employees
              │
              ▼
7. Failed records are tracked
              │
              ▼
8. EmployeeCreatedEvent is generated
              │
              ▼
9. RabbitMQ publishes the event
              │
              ▼
10. Notification Service consumes the event
              │
              ▼
11. Audit Service consumes the event
              │
              ▼
12. Import status can be queried
```

---

# 👨‍💻 Author

**Saravanan**

---

# 📄 License

This project is created for learning and educational purposes.

---

## ⭐ Project Status

🚧 **Under Development**

This project is being developed step-by-step to demonstrate a complete real-world Spring Boot data processing and event-driven architecture.
