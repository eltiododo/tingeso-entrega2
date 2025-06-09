#!/bin/bash

# Azure Container Registry details
ACR_NAME="tingeso2clusterregistry"
ACR_REGISTRY="${ACR_NAME}.azurecr.io"

# Color for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# List of microservices (add all your microservices here)
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
  "frontend"
  # Add other microservices here
)

# Function to display usage
function show_usage {
  echo "Usage: $0 [options]"
  echo "Options:"
  echo "  --all                Build and push all services"
  echo "  --service SERVICE    Build and push specific service"
  echo "  --login-only         Only login to ACR"
  echo "  --help               Show this help message"
}

# Function to build and push a service
function build_and_push {
  SERVICE=$1
  
  echo -e "${GREEN}Building Docker image for $SERVICE...${NC}"
  
  # Navigate to service directory
  cd "$SERVICE" || { echo -e "${RED}Service directory $SERVICE not found!${NC}"; return 1; }
  
  # Build Docker image
  docker build -t "${ACR_REGISTRY}/${SERVICE}:latest" .
  if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to build Docker image for $SERVICE!${NC}"
    cd ..
    return 1
  fi
  
  # Push to ACR
  echo -e "${GREEN}Pushing $SERVICE image to Azure Container Registry...${NC}"
  docker push "${ACR_REGISTRY}/${SERVICE}:latest"
  if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to push Docker image for $SERVICE!${NC}"
    cd ..
    return 1
  fi
  
  echo -e "${GREEN}Successfully built and pushed $SERVICE!${NC}"
  cd ..
  return 0
}

# Login to Azure Container Registry
function acr_login {
  echo -e "${GREEN}Logging in to Azure Container Registry...${NC}"
  az acr login --name "$ACR_NAME"
  if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to login to ACR!${NC}"
    echo "Make sure you're logged into Azure CLI with 'az login' first."
    exit 1
  fi
}

# Check if no arguments provided
if [ $# -eq 0 ]; then
  show_usage
  exit 1
fi

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --all)
      acr_login
      for SERVICE in "${SERVICES[@]}"; do
        build_and_push "$SERVICE"
      done
      ;;
    --service)
      if [ -z "$2" ]; then
        echo -e "${RED}No service specified!${NC}"
        show_usage
        exit 1
      fi
      acr_login
      build_and_push "$2"
      shift
      ;;
    --login-only)
      acr_login
      ;;
    --help)
      show_usage
      exit 0
      ;;
    *)
      echo -e "${RED}Unknown option: $1${NC}"
      show_usage
      exit 1
      ;;
  esac
  shift
done

echo -e "${GREEN}All operations completed!${NC}"