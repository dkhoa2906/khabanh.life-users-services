# Use OpenJDK 21 slim image as base
FROM openjdk:21-jdk-slim

# Set working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/users-services-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (Spring Boot default is 8080)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
