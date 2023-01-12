FROM openjdk:11.0.2-jdk AS builder
COPY . .
COPY target/assignment-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV MONGO_HOST mongodb
ENV RABBIT_HOST rabbitmq
ENTRYPOINT ["java", "-DMONGO_HOST=mongodb", "-DRABBIT_HOST=rabbitmq", "-jar", "app.jar"]