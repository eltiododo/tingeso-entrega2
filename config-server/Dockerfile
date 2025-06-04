FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar config-server.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./config-server.jar"]
