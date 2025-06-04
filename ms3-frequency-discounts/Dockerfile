FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ms3-frequency-discounts.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./ms3-frequency-discounts.jar"]
