# Quarkus Secure Department API with Kafka, PostgreSQL, and JWT  

This project is a backend API developed with **Quarkus**, integrating **PostgreSQL**, **Kafka**, and **JWT** to demonstrate a secure, event-driven microservice architecture. The use case centers around managing `Department` entities with role-based access control and **real-time security event monitoring** using **Kafka**.  

## ✨ Features  

- 🔐 **JWT Authentication with Role-Based Access Control**  
  - `ROLE_USER`: Allowed to access `GET` endpoints.  
  - `ROLE_ADMIN`: Allowed to access all endpoints (`GET`, `POST`, `PUT`, `PATCH`).  
  
- 🏢 **Department API**  
  - Full CRUD operations on department resources.  
  - Secure endpoints requiring JWT authentication.  

- 📦 **Apache Kafka Integration**  
  - Acts as a **Security Event Monitor**.  
  - Failed login attempts trigger a Kafka event to the `security-events` topic.  

- 📄 **PostgreSQL Integration**  
  - Used for persisting departments and security events.  
  - Consumer service stores security events into the `security_event` table.  

- 🔑 **JWT with RSA Private/Public Key**  
  - Uses OpenSSL to generate RSA keys for signing JWTs (not symmetric `secret-key`).  
  - Ensures stronger security and public key verification.  


### 🔐 Authentication Flow  
- **Login Endpoint** — Users authenticate via `/auth/login` using `userName` and `password`.  

- **On Success** — A **JWT token** is returned  
    - This token **must be attached** as an `Authorization: Bearer <token>` header for all secured API requests.  

- **On Failure** (due to reasons such as **invalid credentials**, **disabled account**, **expired account**, or **deleted status**):  
    - A **Kafka message** is published to the `security-events` topic.  
    - A **Kafka consumer** listens to this topic and persists the event into the `security_event` table in the database.  

### 📄 `security_event` Table  

Each failed authentication attempt is stored with the following details:  

| Column Name   | Description                                                                                       |
|---------------|---------------------------------------------------------------------------------------------------|
| `event_type`  | Type of the failure event (`BAD_REQUEST`, `UNAUTHORIZED`, `NOT_ALLOWED`, `INTERNAL_SERVER_ERROR`) |
| `username`    | The username involved in the failed attempt                                                       |
| `ip_address`  | IP address of the client making the request                                                       |
| `user_agent`  | The client’s user agent (browser, client type, etc.)                                              |
| `http_method` | HTTP method used (e.g., `POST`)                                                                   |
| `path`        | Endpoint path accessed (e.g., `/auth/login`)                                                      |
| `fail_message`| Detailed error message describing the reason for failure                                          |
| `timestamp`   | The time the event was recorded                                                                   |

This provides an **audit trail** and facilitates **monitoring suspicious activity** or **troubleshooting authentication issues**.  

---


## 🤖 Tech Stack  

This project leverages a modern and modular technology stack designed for **building secure**, **event-driven RESTful APIs** with ease and performance.  

| Technology                        | Role / Purpose                                                                 |
|-----------------------------------|----------------------------------------------------------------------------------|
| **Quarkus**                       | A **Kubernetes-native Java framework** used to build fast and lightweight Java applications. Provides **dependency injection**, **lifecycle management**, and **extension-based modularity**. |
| **JWT (quarkus-smallrye-jwt)**    | Provides **JSON Web Token** support for stateless, token-based authentication and authorization. Enables fine-grained access control via roles and scopes. |
| **quarkus-messaging-kafka**       | Integrates **Apache Kafka** into the Quarkus application. Used to implement Kafka `producers` and `consumers` for **event-driven messaging**. |
| **quarkus-jdbc-postgresql**       | Provides PostgreSQL database connectivity using **JDBC**. Enables interaction with relational data storage. |
| **quarkus-hibernate-orm-panache** | Simplifies Hibernate ORM with **Panache**, providing an active record-like pattern for writing database operations with minimal boilerplate. |
| **quarkus-rest-jackson**          | Enables REST endpoints with JSON serialization/deserialization using **Jackson**. Powers the API layer to produce and consume JSON payloads. |
| **Lombok**                        | **Reduces boilerplate** code in Java classes by generating **getters**, **setters**, **constructors**, **builders**, etc., through annotations. |
| **quarkus-junit5**                | Supports **writing unit and integration tests** using **JUnit 5** within the Quarkus ecosystem. Used to validate API and service behavior. |

---

## 🧱 Architecture Overview  

The project follows a modular architecture to ensure **separation of concerns**, **testability**, and **maintainability**. Here's a breakdown of each module's responsibility:  

```bash
📁 quarkus-kafka-postgresql/
│
├── 📂src/main/java/com/yoanesber/quarkus_kafka_postgresql/
│   ├── 📂config/serializer/                    # Custom JSON serializers (e.g., for LocalDateTime, Instant)
│   ├── 📂context/                              # Holds RequestContext for capturing user info (e.g., username)
│   ├── 📂dto/                                  # Data Transfer Objects used for API requests and responses
│   ├── 📂entity/                               # Entity models representing database tables
│   ├── 📂exception/                            # Custom exceptions (e.g., DepartmentNotFoundException)
│   ├── 📂handler/                              # Exception mappers to convert exceptions into HTTP responses
│   ├── 📂mapping/                              # Mappers to convert between DTOs and entities
│   ├── 📂repository/                           # Data access layer using Panache Repository
│   ├── 📂resources/                            # REST API endpoints (e.g., DepartmentResource, AuthResource)
│   ├── 📂service/                              # Business logic layer
│   │   ├── 📂kafka/
│   │   │   ├── SecurityEventConsumerService    # Kafka consumer for 'security-events' topic
│   │   │   └── SecurityEventProducerService    # Kafka producer that emits security events
│   │   ├── DepartmentService                   # Manages department CRUD operations
│   │   ├── PasswordService                     # Handles password hashing/verification
│   │   ├── SecurityEventService                # Persists security-related events to DB
│   │   └── TokenService                        # Issues and validates JWT tokens
│
├── 📂src/main/resources/
│   ├── application.properties                  # Quarkus config file (e.g., datasource, JWT, Kafka)
│   ├── generate-jwt-keys.sh                    # Script to generate RSA key pair (private/public) for JWT
│   ├── import.sql                              # SQL script to pre-populate database (e.g., users, roles)
│   ├── privateKey.pem                          # RSA private key for signing JWTs
│   └── publicKey.pem                           # RSA public key for verifying JWTs
│
├── 📂src/test/java/                            # Unit and integration tests
│   └── 📂com/yoanesber/
│       ├── AuthResourceIT.java                 # @QuarkusIntegrationTest for AuthResource
│       ├── AuthResourceTest.java               # @QuarkusTest for AuthResource
│       ├── DepartmentResourceIT.java           # @QuarkusIntegrationTest for DepartmentResource
│       └── DepartmentResourceTest.java         # @QuarkusTest for DepartmentResource
│    
├── Makefile                                    # Makefile for simplifying tasks like building, testing, etc.
├── pom.xml                                     # Maven build configuration (Java 17, dependencies, plugins)
├── README.md                                   # Project overview, setup, and usage instructions
└── .gitignore                                  # Files/folders to ignore in version control
```  

This clean separation allows the application to **scale well**, supports **test-driven development**, and adheres to best practices in **enterprise application design**.  

---


## 🛠️ Installation & Setup  

Follow these steps to set up and run the project locally:  

### ☕ 1. Install Java 17  

1. Ensure **Java 17** is installed on your system. You can verify this with:  

```bash
java --version
```  

2. If Java is not installed, follow one of the methods below based on your operating system:  

#### 🐧 Linux  

**Using apt (Ubuntu/Debian-based)**:  

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```  

#### 🪟 Windows  
1. Use [https://adoptium.net](https://adoptium.net) to download and install **Java 17 (Temurin distribution recommended)**.  

2. After installation, ensure `JAVA_HOME` is set correctly and added to the `PATH`.  

3. You can check this with:  

```bash
echo $JAVA_HOME
```  

### 🐘 2. Install PostgreSQL  
1. Install PostgreSQL if it’s not already available on your machine:  
    - Use [https://www.postgresql.org/download/](https://www.postgresql.org/download/) to download PostgreSQL.  

2. Once installed, create the following databases:  
```sql
CREATE DATABASE quarkus_kafka_postgresql_dev;  
CREATE DATABASE quarkus_kafka_postgresql_test;  
```  

These databases are used for development and automated testing, respectively.  

### 📡 3. Install Apache Kafka  
Follow the official [Apache Kafka Quickstart](https://kafka.apache.org/quickstart) for local installation.  
Here’s a simplified summary:  

1. **Download & Extract Kafka**:  
```bash
sudo wget https://downloads.apache.org/kafka/4.0.0/kafka_2.13-4.0.0.tgz
tar -xzf kafka_2.13-4.0.0.tgz
mv kafka_2.13-4.0.0 kafka-
cd kafka
```  

2.  **Start the Kafka environment**  
**NOTE**: Your local environment must have Java 17+ installed.  

- Generate a Cluster UUID:  
```bash
KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
```  

- Format Log Directories:  
```bash
bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties
```  

- Start the Kafka Server:  
```bash
bin/kafka-server-start.sh config/server.properties
```  
Once the Kafka server has successfully launched, you will have a basic Kafka environment running and ready to use.  


### 🧰 4. Install make (Optional but Recommended)  
This project uses a `Makefile` to streamline common tasks.  

Install `make` if not already available:  

#### 🐧 Linux  

Install `make` using **APT**  

```bash
sudo apt install makesudo apt update
sudo apt install make
```  

You can verify installation with:   
```bash
make --version
```  

#### 🪟 Windows  

If you're using **PowerShell**:  

- Install [Chocolatey](https://chocolatey.org/install) (if not installed):  
```bash
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```  

- Verify Chocolatey installation:  
```bash
choco --version
```  

- Install make via Chocolatey:  
```bash
choco install make
```  

After installation, **restart your terminal** or ensure make is available in your `PATH`.  

### 🔁 5. Clone the Project  

Clone the repository:  

```bash
git clone https://github.com/yoanesber/Quarkus-kafka-Postgresql-JWT.git
cd Quarkus-kafka-Postgresql-JWT
```  

### 🔐 6. Generate JWT RSA Key Pair  

Generate a private and public key pair to **sign** and **verify** JWT tokens:  

**Using `make`:**  

```bash
make generate-jwt-keys
```  

**Or manually:**  

```bash
bash generate-jwt-keys.sh
```  

This will generate `privateKey.pem` and `publicKey.pem` in the `src/main/resources/` directory.  

**⚠️ Security Note:**  
The `privateKey.pem` file is included in `.gitignore` to **prevent accidental commits to the repository**, especially since this project will be made **public**.  
Never expose your private key in version control to protect your JWT signing mechanism.

### ⚙️ 7. Configure Application Properties  

Set up your **database**, **Kafka**, and **JWT configuration** in:  

```properties
# Quarkus configuration file
# Configure the application to use the default HTTP port
quarkus.http.port=8081
quarkus.http.cors=true

# PostgreSQL database configuration (Dev)
%dev.quarkus.datasource.db-kind=postgresql
%dev.quarkus.datasource.username=postgres
%dev.quarkus.datasource.password=<password>
%dev.quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/quarkus_kafka_postgresql_dev
%dev.quarkus.hibernate-orm.database.generation=drop-and-create
%dev.quarkus.hibernate-orm.log.sql=true
%dev.quarkus.hibernate-orm.sql-load-script=import.sql

# PostgreSQL database configuration (Test)
%test.quarkus.datasource.db-kind=postgresql
%test.quarkus.datasource.username=postgres
%test.quarkus.datasource.password=<password>
%test.quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/quarkus_kafka_postgresql_test
%test.quarkus.hibernate-orm.database.generation=drop-and-create
%test.quarkus.hibernate-orm.log.sql=true
%test.quarkus.hibernate-orm.sql-load-script=import.sql

# JWT configuration
mp.jwt.verify.publickey.location=publicKey.pem
mp.jwt.verify.issuer=http://yoanesber.com/issuer
mp.jwt.verify.token.age=60
quarkus.http.auth.permission.authenticated.paths=/api/*
quarkus.http.auth.permission.authenticated.policy=authenticated
smallrye.jwt.sign.key.location=privateKey.pem
# quarkus.native.resources.includes=./publicKey.pem


# Kafka configuration
kafka.bootstrap.servers=localhost:9092

# Channel for sending Security Event Monitoring
mp.messaging.outgoing.security-events-outgoing.connector=smallrye-kafka
mp.messaging.outgoing.security-events-outgoing.topic=security-events
mp.messaging.outgoing.security-events-outgoing.value.serializer=org.apache.kafka.common.serialization.StringSerializer

# Channel for receiving Security Event Monitoring
mp.messaging.incoming.security-events-incoming.connector=smallrye-kafka
mp.messaging.incoming.security-events-incoming.topic=security-events
mp.messaging.incoming.security-events-incoming.value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
```  

- Ensure that:  
  - Database URLs, username, and password are correct.  
  - Kafka is running at `localhost:9092`.  
  - JWT keys (path to `.pem` files) are set correctly.


### ✅ 8. Run Unit Tests  

Make sure your `quarkus_kafka_postgresql_test` database exists, then run:  

```bash
make test
```  

This will run the integration and unit tests.  

### 🚀 9. Run the Application  

Once everything is configured and tests pass, you can start the application in development mode:  

```bash
make run
```  

---

## 🧪 Testing Scenarios  

### 1. 🔐Login API Testing Scenarios

Endpoint:  

```bash
POST http://localhost:8081/auth/login
Content-Type: application/json
```  

#### Scenario 1: Successful Login

**Request Body:**  

```json
{
  "userName": "admintest",
  "password": "P@ssw0rd"
}
```  

**Expected Response (`200 OK`):**  

```json
{
  "message": "Login successful",
  "error": null,
  "path": "/auth/login",
  "status": 200,
  "data": "<JWT_TOKEN>",
  "timestamp": "<timestamp>"
}
```  

- **Validation**:  
    - Status code is `200`  
    - Message is `Login successful`  
    - JWT token is present in the `data` field  

#### Scenario 2: Invalid Credentials  

**Request Body:**  

```json
{
  "userName": "invalid_user",
  "password": "P@ssw0rd"
}
```  

**Expected Response (`401 Unauthorized`):**  

```json
{
  "message": "Invalid username or password",
  "error": "Unauthorized",
  "path": "/auth/login",
  "status": 401,
  "data": null,
  "timestamp": "<timestamp>"
}
```  

- **Validation**:  
    - Status code is `401`  
    - Proper error message is returned  
    - `data` field is `null`  


#### Scenario 3: Disabled User  

**Precondition:**  

```sql
UPDATE users SET is_enabled = false WHERE id = 5;
```  

**Request Body:**  

```json
{
  "userName": "admintest",
  "password": "P@ssw0rd"
}
```  

**Expected Response (`401 Unauthorized`):**  

```json
{
  "message": "User has been disabled",
  "error": "Unauthorized",
  "path": "/auth/login",
  "status": 401,
  "data": null,
  "timestamp": "<timestamp>"
}
```  

- **Validation**:  
    - Status code is `401`  
    - Message explicitly indicates disabled user  
    - No token is returned  

#### Scenario 4: Expired User Account  

**Precondition:**  

```sql
UPDATE users SET is_account_non_expired = false WHERE id = 5;
```  

**Request Body:**  

```json
{
  "userName": "admintest",
  "password": "P@ssw0rd"
}
```  

**Expected Response (`401 Unauthorized`):**  

```json
{
  "message": "User account has been expired",
  "error": "Unauthorized",
  "path": "/auth/login",
  "status": 401,
  "data": null,
  "timestamp": "<timestamp>"
}
```  

- **Validation**:  
    - Status code is `401`  
    - Message indicates account expiration  
    - No token is returned  


### 2. Department API Testing Scenarios  

**Endpoint:**  

```bash
GET http://localhost:8081/api/v1/departments?page=0&size=2
Authorization: Bearer <JWT_TOKEN>
```  

### Scenario 1: Valid Token - Successful Retrieval  

- **Precondition**:  
    - Login berhasil dan mendapatkan token JWT valid  
    - Token disisipkan dalam header Authorization: Bearer <JWT_TOKEN>  

**Expected Response (`200 OK`):**  

```json
{
    "message": "All departments retrieved successfully",
    "error": null,
    "path": "/api/v1/departments",
    "status": 200,
    "data": [
        {
            "id": "d001",
            "deptName": "Marketing",
            "active": true
        },
        {
            "id": "d002",
            "deptName": "Finance",
            "active": true
        }
    ],
    "timestamp": "2025-05-09T20:10:07.602740Z"
}
```  

- **Validation**:  
    - Status code is `200`  
    - Data array berisi daftar department  
    - Message menyatakan data berhasil diambil  


### Scenario 2: Invalid Token - Unauthorized Access

- **Precondition**:  
    - Gunakan token yang sudah dimanipulasi atau tidak valid (expired/salah format)  


**Header:**  

```bash
Authorization: Bearer invalid.token.here
```  

**Expected Response (`401 Unauthorized`):**  

```json
{
  "message": "Authentication failed",
  "error": null,
  "path": "/api/v1/departments",
  "status": 401,
  "data": null,
  "timestamp": "2025-05-09T20:03:43.792190900Z"
}
```  

- **Validation**:  
    - Status code is `401`  
    - Message menjelaskan bahwa autentikasi gagal  
    - Tidak ada data yang dikembalikan  


### 3. Kafka Security Event Monitoring Scenario  

**Scenario:** Failed Login triggers **Kafka Producer & Consumer**  

**Endpoint:**  
```bash
POST http://localhost:8081/auth/login
```  

**Invalid Request Body:**  

```json
{
  "userName": "invalid_user",
  "password": "P@ssw0rd"
}
```  

**Expected HTTP Response (401 Unauthorized):**  

```json
{
    "message": "Invalid username or password",
    "error": "Unauthorized",
    "path": "/auth/login",
    "status": 401,
    "data": null,
    "timestamp": "2025-05-09T21:10:57.750492Z"
}
```  

**Kafka Producer:**  

- Listens to `security-events` topic  
- Persists each received event into the `security_event` table in PostgreSQL  

```bash
2025-05-10 04:19:21,866 INFO  [com.yoa.qua.ser.kaf.SecurityEventProducerService] (executor-thread-1) Sending security event: {"id":null,"eventType":"UNAUTHORIZED","username":"invalid_user","ipAddress":"127.0.0.1","userAgent":"PostmanRuntime/7.43.4","httpMethod":"POST","path":"/auth/login","failMessage":"Invalid username or password","timestamp":"2025-05-10T04:19:21.8665771"}
```  

**Kafka Consumer:**  

- Listens to `security-events` topic  
- Persists each received event into the `security_event` table in PostgreSQL  

```bash
2025-05-10 04:19:21,866 INFO  [com.yoa.qua.ser.kaf.SecurityEventConsumerService] (vert.x-worker-thread-1) Received message: {"id":null,"eventType":"UNAUTHORIZED","username":"invalid_user","ipAddress":"127.0.0.1","userAgent":"PostmanRuntime/7.43.4","httpMethod":"POST","path":"/auth/login","failMessage":"Invalid username or password","timestamp":"2025-05-10T04:19:21.8665771"}
[Hibernate]
    insert
    into
        security_event
        (event_type, fail_message, http_method, ip_address, path, timestamp, user_agent, username, id)
    values
        (?, ?, ?, ?, ?, ?, ?, ?, ?)

2025-05-10 04:19:21,866 INFO  [com.yoa.qua.ser.kaf.SecurityEventConsumerService] (vert.x-worker-thread-1) Security event persisted: SecurityEvent(id=e523d0ea-32fd-4e27-a129-f4600ff753eb, eventType=UNAUTHORIZED, username=invalid_user, ipAddress=127.0.0.1, userAgent=PostmanRuntime/7.43.4, httpMethod=POST, path=/auth/login, failMessage=Invalid username or password, timestamp=2025-05-10T04:19:21.866577100)
```  

**Verification:**  

After triggering the invalid login, run the following query in your PostgreSQL DB:  

```sql
SELECT * FROM security_event ORDER BY timestamp DESC;
```  

You should see an entry like:  

![Image](https://github.com/user-attachments/assets/83aa3b55-6326-4541-a27b-38edcf893764)  



For full coverage, refer to the included **Postman Collection**:  
[Quarkus.postman_collection.json](https://github.com/user-attachments/files/20128551/Quarkus.postman_collection.json)