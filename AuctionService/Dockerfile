FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8400

COPY target/auction-service-1.0.0.jar ./auction-service.jar

ENTRYPOINT ["java", "-jar", "auction-service.jar"]
