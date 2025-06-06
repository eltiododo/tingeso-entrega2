#!/bin/bash

# List of microservices
SERVICES=(
  "gateway-server"
  "eureka-server"
  "config-server"
  "ms1-reservation-categories"
  "ms2-quantity-discounts"
  "ms3-frequency-discounts"
  "ms4-special-tariffs"
  "ms5-reservations"
  "ms6-rack"
  "ms7-reports"
)

for SERVICE in "${SERVICES[@]}"; do
  if [ -f "$SERVICE/Dockerfile" ]; then
    echo "Updating Dockerfile for $SERVICE"
    
    # Extract JAR filename from existing Dockerfile
    JAR_NAME=$(grep -oP 'ENTRYPOINT \["java", "-jar", "./\K[^"]*' "$SERVICE/Dockerfile" || echo "$SERVICE.jar")
    
    # Create new Dockerfile content
    cat > "$SERVICE/Dockerfile" << EOF
FROM openjdk:17-jdk-slim
COPY ./target/*.jar $JAR_NAME
ENTRYPOINT ["java", "-jar", "./$JAR_NAME"]
EOF
  fi
done

echo "All Dockerfiles updated successfully"