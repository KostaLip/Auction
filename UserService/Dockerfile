FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8770

COPY target/user-service-1.0.0.jar ./user-service.jar

ENTRYPOINT ["java", "-jar", "user-service.jar"]
