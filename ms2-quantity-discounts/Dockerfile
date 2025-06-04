FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ms2-quantity-discounts.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./ms2-quantity-discounts.jar"]
