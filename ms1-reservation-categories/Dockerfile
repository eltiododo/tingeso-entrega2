FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ms1-reservation-categories.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./ms1-reservation-categories.jar"]
