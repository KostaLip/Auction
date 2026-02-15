FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8200

COPY target/bank-account-1.0.0.jar ./bank-account.jar

ENTRYPOINT ["java", "-jar", "bank-account.jar"]
