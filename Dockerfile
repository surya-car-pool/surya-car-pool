FROM eclipse-temurin:17-jdk-jammy

WORKDIR /surya-car-pool

COPY target/*.jar surya-car-pool.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","surya-car-pool.jar"]
