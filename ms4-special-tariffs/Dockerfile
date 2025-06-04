FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ms4-special-tariffs.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./ms4-special-tariffs.jar"]
