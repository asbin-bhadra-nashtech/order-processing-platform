# Architecture

## Overview

The Order Processing Platform is a microservices-based application consisting of three Spring Boot services:

1. **Order API** — exposes the external REST API for creating and retrieving orders.
2. **Order Processor** — processes orders received from the Order API and communicates with the Notification Service.
3. **Notification Service** — handles notification requests generated during order processing.

## Service Communication

The request flow is:

Client → Order API → Order Processor → Notification Service

### Service Ports

| Service | Port | Kubernetes Service |
|---|---:|---|
| Order API | 8081 | NodePort |
| Order Processor | 8082 | ClusterIP |
| Notification Service | 8083 | ClusterIP |

## Local Deployment

Docker Compose can be used to run all three services locally.

The services communicate through the Docker Compose network using service names rather than localhost.

## Kubernetes Deployment

The application can also be deployed to Kubernetes using the manifests under the `k8s/` directory.

The Order API is exposed externally using a NodePort:

```text
Order API
8081 → NodePort 30081