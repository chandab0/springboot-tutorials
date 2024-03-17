# Spring Boot Actuator Demo

This is a simple Spring Boot application that demonstrates the usage of Spring Boot Actuator to monitor and manage your application.

## Getting Started

Follow the instructions below to build and run the application locally.

### Prerequisites

- Java Development Kit (JDK) 8 or later
- Apache Maven or Gradle

### Building the Application

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/your-username/actuator-demo.git
2. Navigate to the project directory:
    ```bash
   cd actuator-demo
4. Build the application using Maven
    ```bash
    mvn clean package
### Running the Application
1. Once the build is successful, navigate to the target directory.
   ```bash
   cd target
2. Run the JAR file using the java -jar command: 
    ```bash
    java -jar actuator-demo-1.0.0.jar
3. The application should now be running locally. You can access the Actuator endpoints, including `/actuator/health`, `/actuator/metrics`, `/actuator/env` and `/actuator/info` using a web browser or tools like cURL or Postman.
   
