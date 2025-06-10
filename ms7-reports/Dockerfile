FROM openjdk:17-jdk-slim
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    libfreetype6 \
    fontconfig && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
COPY ./target/*.jar ms7-reports.jar
ENTRYPOINT ["java", "-jar", "./ms7-reports.jar"]
