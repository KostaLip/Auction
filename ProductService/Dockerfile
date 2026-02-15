FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8300

COPY target/product-service-1.0.0.jar ./product-service.jar

ENTRYPOINT ["java", "-jar", "product-service.jar"]
