FROM openjdk:17-jdk-slim
COPY ./target/*.jar config-server.jar
ENTRYPOINT ["java", "-jar", "./config-server.jar"]
