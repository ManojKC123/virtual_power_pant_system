# Virtual Power Plant System

This project implements a **Virtual Power Plant** system using **Spring Boot**. The system handles concurrent battery registrations efficiently and supports functionalities like battery management, capacity calculations, and pagination.



## Technologies Used

- **Spring Boot** - Backend framework for building and managing the system.
- **ModelMapper** - For object-to-object mapping between entities and DTOs.
- **JPA (Java Persistence API)** - For interacting with the PostgreSQL database.
- **HikariCP** - Connection pool for database interactions.
- **ExecutorService** - Manages asynchronous tasks for concurrent registration.
- **Logback / SLF4J**  - For logging application activities.
- **PostgreSQL** - Relational database used to store battery data.

---

## Project Setup

### Prerequisites

1. **Java 17+** (or any compatible version).
2. **Maven** for building and managing dependencies.
3. **Spring Boot** for application framework.
4. **PostgreSQL Database** - Used for storing battery data.

### Clone the Repository

```git clone [<repository-url>](https://github.com/ManojKC123/virtual_power_pant_system/tree/master)```

### Build and Run the Application
Navigate to the project directory:
```cd <VirtualPowerPlant>```

To build and run the application, use Maven:
```mvn clean install```
```mvn spring-boot:run```

### Configure PostgreSQL Database
```
spring.datasource.url=jdbc:postgresql://localhost:5432/virtual_power_plant_db
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.datasource.driverClassName=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.idle-timeout=30000
```

### Architecture

The Virtual Power Plant System follows a layered architecture to separate concerns and ensure scalability and maintainability. The system is divided into the following layers:

1. **Controller Layer (API Layer)**
   The controller layer handles incoming HTTP requests and forwards them to the service layer. It exposes the API endpoints for client requests.

BatteryController: Manages operations related to battery management such as saving, fetching, and searching batteries.

2. **Service Layer (Business Logic Layer)**
   This layer contains the core business logic of the system. It interacts with the repository layer to perform operations and implements the application's business rules.

BatteryImp: Implements the BatteryService interface, handling methods like saving batteries, fetching batteries by criteria, and calculating total capacity.

3. **Repository Layer (Data Access Layer)**
   The repository layer interacts directly with the database. It provides the mechanism to perform CRUD operations using Spring Data JPA.

BatteryRepository: Extends JpaRepository to enable database operations related to batteries.

4. **Concurrency Handling**
   To ensure the system can handle multiple concurrent battery registrations efficiently:

ExecutorService is used to execute tasks asynchronously, offloading battery registration to background threads. This improves throughput and prevents blocking the main application thread.

AtomicInteger is used for thread-safe registration count management.

5. **Model Layer (DTOs and Entities)**
   The model layer contains entities and DTOs used for transferring data between layers:

**Battery (Entity)**: Represents a battery in the database with fields like name, postcode, and capacity.

**BatteryDTO**: A Data Transfer Object (DTO) used to transfer battery data between controller and service layers.

6. **Database Layer**
   The system uses PostgreSQL to store battery data. It supports various queries and handles large-scale data efficiently. The database schema is created and managed using JPA and Hibernate.

### Architecture Diagram

``` 
[Client (e.g., Postman/Browser)] --> [Controller Layer] --> [Service Layer] --> [Repository Layer] --> [Database (PostgreSQL)]
                    ^                        |
                    |------------------------|
                 [Concurrency Handling (ExecutorService)]
```

### Key Features
**Battery Management:**

1. Register new batteries.

Fetch battery details by ID.

Fetch all batteries with pagination and sorting.

Search batteries by postcode and capacity range.

2. Concurrent Handling of Battery Registrations:

Using ExecutorService, the system processes battery registration tasks asynchronously, allowing multiple registrations to happen concurrently.

3. Battery Calculations:

Calculate the total capacity of all registered batteries.

Calculate the average capacity of registered batteries.

4. Pagination and Sorting:

Fetch battery data with pagination and sorting capabilities (e.g., by name or capacity).

### Concurrency Handling
To ensure the system can handle a large number of battery registrations concurrently:

ExecutorService handles asynchronous execution of tasks. Battery registrations are submitted to a thread pool for processing, which prevents blocking the main thread and allows for high throughput.

AtomicInteger ensures that the registration count is maintained in a thread-safe manner.

### Key Concurrency Concepts:
**ExecutorService**: Manages the background threads for processing battery registrations.

**Future**: Represents the result of an asynchronous computation.

**AtomicInteger**: Ensures thread-safe counting of battery registrations.


### Testing

1. **JUnit 5:**
   The test framework used is JUnit 5, which is the most current version of the popular testing framework for Java.

   The annotations used are:
   
   @Test: Marks a method as a test method.
   
   @BeforeEach: Used to define setup code that runs before each test method.
   
   @DisplayName: Provides a custom name for a test method.

2. **Mockito:**
   Mockito is used for mocking the dependencies (like BatteryService) and defining the behavior of these mocks in the test.

   **Annotations used:**
   
   **@Mock:** Creates a mock instance of BatteryService to simulate its behavior without hitting the actual database or service.
   
   **@InjectMocks:** Injects the mocked BatteryService into the VirtualPowerPlantController.
   
   **MockitoAnnotations.openMocks(this):** Initializes the mocks before each test.
   
   **when(...).thenReturn(...):** Defines what the mock should return when specific methods are called.
   
   **verify(...):** Verifies that a method was called with specific parameters.

3. **MockMvc:**
   MockMvc is used to simulate HTTP requests and test the behavior of the Spring MVC controllers without starting a full HTTP server. It allows you to test the HTTP layer of your application.

   **Methods used:**
   
   **mockMvc.perform(...):** Performs an HTTP request (like GET or POST) and defines its parameters.
   
   **andExpect(...):** Defines assertions on the result, such as verifying the HTTP status and checking the response content (e.g., jsonPath to verify JSON response).
   
4. **Spring Test Annotations:**
   @SpringBootTest or @WebMvcTest annotations are commonly used in Spring Boot for integration tests, but this test does not use them because it is a unit test for the controller with mocked services.

5. **JSONPath:**
   JSONPath is used in andExpect(jsonPath(...)) to assert the structure of the JSON response from the controller.

   Example:
   ``` .andExpect(jsonPath("$.name").value("Test Battery 1"))```

   This checks that the response JSON contains a field name with the value "Test Battery 1".

To run test ``` mvn test ```

### Logging
**SLF4J (Simple Logging Facade for Java)**

1. SLF4j is used for logging. It is a logging abstraction layer that allows different logging frameworks (such as Logback, Log4j2, etc.) to be used underneath. Spring Boot uses SLF4J for its logging needs by default.

2.   Spring Boot automatically configures Logback as the default logging implementation when you use SLF4J. This means any logging done via SLF4J in your application will be routed through Logback.
