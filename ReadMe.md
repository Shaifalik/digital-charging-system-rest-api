#Charge Detail Record Application

Charge Detail Records Application is manage APIs responsible for managing Charge Detail Records (CDR) in real time
to a network of Charge Point Operators (CPO). It exposes multiple APIs for creating,searching and getting 
Charge Detail Records.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Test](#test)
- [Design Patterns](#designpatterns)

## Installation

### Prerequisites

- Java Development Kit (JDK) 17 (recommended)
- Gradle 6.0 or higher (recommended)

To install and set up the project, follow these steps:

1. **Unzip the file:**
    ```
    Open it one of the IDE.
    ```
   
2. **Build the project :**
    ```sh
    ./gradlew build
    ```
   
   In case of permission denied please run: 
    ```sh
    chmod +x gradlew
    ```
   
3. **Run the application:**
    ```sh
    ./gradlew bootRun
    ```

## Usage

To use the project, you can perform the following:

- **Access the application in your browser:**
    ```
    http://localhost:8080
    ```

Please refer to to Swagger API documentation for all APIs: http://localhost:8080/swagger-ui/index.html

##Testing

This project includes a several test strategies to ensure the code quality and functionality. 
These tests are implemented using JUnit and Spring Boot Test, our project includes

1. Unit Tests: Test individual components and services.
2. Repository Tests: Test the data access layer to ensure that database interactions work as expected.

To run the tests, you can use the following command:
  ``` 
    ./gradlew test
  ```

The test coverage reports can be generated with JaCoCo using below command.
  ```
./gradlew jacocoTestReport
  ```

This will generate the report. - **Open index.html file in your browser:**

##DesignPattern

We have included few design patterns to address to the code readability and maintainability.
Some design patterns used includes:

Controller-Service -Repository - (CSR): The project is designed using the CSR pattern to separate concerns 
between different parts.

Repository Pattern: Used in the data access layer to abstract the database operations 
and make the data layer more flexible and testable.

Builder Pattern: Used in ChargeDetailRecordDTOs (Data Transfer Objects) to simplify object creation 
and make the code more readable.