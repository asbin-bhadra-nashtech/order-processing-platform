# Order Processing Platform

A containerized microservices-based order processing platform built with Spring Boot, Docker, Kubernetes, Helm, and Terraform.

## Architecture

The platform consists of three services:

* **Order API** — exposes REST APIs for creating and retrieving orders.
* **Order Processor** — processes orders received from Order API and communicates with the notification service.
* **Notification Service** — handles order notification processing.

### Service Ports

| Service              | Port | Kubernetes Service |
| -------------------- | ---: | ------------------ |
| Order API            | 8081 | NodePort `30081`   |
| Order Processor      | 8082 | ClusterIP          |
| Notification Service | 8083 | ClusterIP          |

## Project Structure

```text
order-processing-platform/
├── services/
│   ├── orderapi/
│   ├── order-processor/
│   └── notification-service/
├── k8s/
├── helm/
├── infra/
├── docker-compose.yml
└── .github/
    └── workflows/
        └── ci-cd.yml
```

## Prerequisites

Install the following:

* Java 21
* Docker Desktop
* Kubernetes / Minikube
* kubectl
* Helm
* Terraform

## Build and Test

Each service is an independent Spring Boot application.

Run tests from the individual service directories:

```powershell
cd services/orderapi
.\gradlew clean test build
```

```powershell
cd services/order-processor
.\gradlew clean test build
```

```powershell
cd services/notification-service
.\gradlew clean test build
```

## Docker

Build the service JARs first:

```powershell
cd services/orderapi
.\gradlew clean build

cd ..\order-processor
.\gradlew clean build

cd ..\notification-service
.\gradlew clean build
```

Build the Docker images:

```powershell
docker build -t order-api:1.0 .\services\orderapi
docker build -t order-processor:1.0 .\services\order-processor
docker build -t notification-service:1.0 .\services\notification-service
```

For Minikube, load the images into the cluster:

```powershell
minikube image load order-api:1.0
minikube image load order-processor:1.0
minikube image load notification-service:1.0
```

Verify:

```powershell
minikube image ls
```

## Docker Compose

Start the platform using:

```powershell
docker compose up --build
```

Stop the services:

```powershell
docker compose down
```

## Kubernetes

Start Minikube:

```powershell
minikube start --driver=docker
```

Verify the cluster:

```powershell
kubectl get nodes
```

Deploy the Kubernetes manifests:

```powershell
kubectl apply -f k8s/
```

Verify deployments:

```powershell
kubectl get deployments
```

Verify pods:

```powershell
kubectl get pods
```

Verify services:

```powershell
kubectl get services
```

Check service logs:

```powershell
kubectl logs deployment/order-api
kubectl logs deployment/order-processor
kubectl logs deployment/notification-service
```

## Test the Order API

The Order API is exposed through a Kubernetes NodePort.

Get the service URL:

```powershell
minikube service order-api --url
```

Use the returned URL to create an order:

```powershell
Invoke-RestMethod `
  -Method POST `
  -Uri "http://127.0.0.1:<PORT>/orders" `
  -ContentType "application/json" `
  -Body '{"customerName":"Alex","product":"Laptop","quantity":1}'
```

Get all orders:

```powershell
Invoke-RestMethod "http://127.0.0.1:<PORT>/orders"
```

## Helm

Validate the Helm chart:

```powershell
helm lint .\helm
```

Render the Kubernetes manifests locally:

```powershell
helm template order-processing-platform .\helm
```

Install the chart:

```powershell
helm install order-processing-platform .\helm
```

Check the release:

```powershell
helm status order-processing-platform
```

Verify the Kubernetes resources:

```powershell
kubectl get pods
kubectl get deployments
kubectl get services
```

If the Helm release already exists, upgrade it instead:

```powershell
helm upgrade order-processing-platform .\helm
```

## Terraform

Terraform configuration is located in the `infra/` directory.

Initialize Terraform:

```powershell
cd infra
terraform init
```

Validate the configuration:

```powershell
terraform validate
```

Review the execution plan:

```powershell
terraform plan
```

Apply the configuration:

```powershell
terraform apply
```

Verify that Terraform has no pending changes:

```powershell
terraform plan
```

Expected result:

```text
No changes. Your infrastructure matches the configuration.
```

## CI/CD

The GitHub Actions workflow is located at:

```text
.github/workflows/ci-cd.yml
```

The pipeline is intended to automate the application's build and test process.

> The CI/CD workflow is currently being finalized.

## Troubleshooting

Common troubleshooting information is documented in:

```text
troubleshooting.md
```

This includes issues encountered during local Docker, Kubernetes, Helm, Terraform, and CI/CD setup.

## Infrastructure

The project includes:

* Docker containerization
* Kubernetes deployments and services
* Helm packaging
* Terraform-based Helm deployment
* GitHub Actions CI/CD workflow

The Azure deployment portion is intentionally not included in the current implementation.
