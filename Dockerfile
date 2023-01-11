FROM openjdk:11.0.2-jdk AS builder
COPY . .
COPY target/assignment-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-DMONGO_HOST=mongodb", "-jar", "app.jar"]