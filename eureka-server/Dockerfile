FROM openjdk:17-jdk-slim
COPY ./target/*.jar eureka-server.jar
ENTRYPOINT ["java", "-jar", "./eureka-server.jar"]
