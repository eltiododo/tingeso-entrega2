FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ms7-reports.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./ms7-reports.jar"]
