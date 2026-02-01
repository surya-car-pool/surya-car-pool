# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build

# Copy Maven config first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /surya-car-pool

# Copy the built JAR from build stage
COPY --from=build /build/target/surya-car-pool-*.jar surya-car-pool.jar

EXPOSE 8080

# Correct JVM option order + prod profile
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","surya-car-pool.jar"]
