FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ms6-rack.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./ms6-rack.jar"]
