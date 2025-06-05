FROM openjdk:17-jdk-slim
COPY ./target/*.jar gateway-server.jar
ENTRYPOINT ["java", "-jar", "./gateway-server.jar"]
