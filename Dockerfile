# Use official OpenJDK replacement
FROM eclipse-temurin:17-jdk-jammy

# Create app directory
WORKDIR /surya-car-pool

# Copy jar file
COPY target/*.jar surya-car-pool.jar

# Expose Spring Boot port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "surya-car-pool.jar"]
