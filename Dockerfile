FROM ubuntu:latest
LABEL authors="dimleon95"
FROM maven:3.9.5-eclipse-temurin-17 AS build
COPY . .

RUN mvn -f pom.xml clean package
FROM eclipse-temurin:17
WORKDIR /
COPY --from=build ./target/redis_*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]