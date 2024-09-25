## **Congestion Tax Calculator Solution Note**

### **Solution Overview**
This is a Spring Boot 3 application for calculating congestion taxes based on city-specific rules. The project applies key design principles for scalability and flexibility.

### **Design Patterns & Principles**:
- **Strategy Pattern**: For city-specific congestion tax calculation.
- **Chain of Responsibility**: For handling request validation, decoupled validation.
- **SOLID Principles**:
    - **Single Responsibility Principle**: Each class has its own clear responsibility (validation, calculation, etc.).
    - **Open/Closed Principle**: The application is designed to be easily extendable (e.g., adding new cities) without modifying existing code.
    - **Dependency Inversion**: High level modules depend on abstractions, not on low-level implementations.

### **Configuration**
- All configuration is externalized using **YAML** for easy maintainability, including cities, fee slots, exempt vehicles, and dates.

### **Project Specs**
* **Java 17**
* **Spring Boot 3**
* **JUnit & Mockito**
* **Maven**
* **SpringBootTest** for integration testing
* **Micrometer** 

## **Running the Application**
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-repository-url.git
   ```

2. **Run the Application:**
   ```bash
   mvn spring-boot:run
   ```

## **API Details**

**URL:** `http://localhost:8080/v1/congestion-fee/calculate`

**Sample POST Request:**
```json
{
    "city": "gothenburg",
    "vehicleType": "car",
    "timeStamps": [
        "2013-09-23T06:23:00",
        "2013-09-23T07:00:00"
    ]
}
```

**Success Response:**
```json
{
    "totalFee": 18
}
```

**Failure Response:**
```json
{
    "message": "Invalid city"
}
```

## **Testing**
- **Unit Testing**: JUnit and Mockito are used to test the business logic.
- **Integration Testing**: Controller and API endpoints are tested using SpringBootTest.

## **Future Enhancements**
- **Support for More Cities**: Add additional strategies for different cities.
- **Security using JWT**: Implement token-based authentication.
- **Docker Integration**: Containerize the application using Docker.
- **Caching**: Introduce caching for fee calculation performance improvements.
- **Enhanced Logging**: Add logging using **Slf4j** for better traceability.
- **Database Support**: Consider adding or **PostgreSQL** for persistent data storage.
- **Implement Rate Limiting**
- **Monitoring & Alerts**

