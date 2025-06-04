FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ms5-reservations.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./ms5-reservations.jar"]
