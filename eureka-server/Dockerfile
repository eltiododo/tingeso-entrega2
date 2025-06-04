FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar eureka-server.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./eureka-server.jar"]
