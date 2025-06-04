FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar gateway-server.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./gateway-server.jar"]
