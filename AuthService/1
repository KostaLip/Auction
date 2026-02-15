FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8880

COPY target/auth-service-1.0.0.jar ./auth-service.jar

ENTRYPOINT ["java", "-jar", "auth-service.jar"]
