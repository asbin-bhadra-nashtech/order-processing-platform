# Troubleshooting

## 1. Gradle Wrapper Permission Denied in GitHub Actions

### Problem

The GitHub Actions CI/CD workflow failed with:

```text
Run ./gradlew clean test build
/home/runner/work/_temp/...sh: line 1: ./gradlew: Permission denied

Error: Process completed with exit code 126.
```

### Cause

The `gradlew` files did not have executable permission in Git.

### Solution

On Linux/macOS or Git Bash:

```bash
chmod +x services/orderapi/gradlew
chmod +x services/order-processor/gradlew
chmod +x services/notification-service/gradlew
```

Then commit the permission changes:

```bash
git add services/*/gradlew
git commit -m "Fix Gradle wrapper permissions"
git push
```

Alternatively, the GitHub Actions workflow can explicitly make the wrappers executable before running them:

```yaml
- name: Make Gradle wrappers executable
  run: chmod +x services/*/gradlew
```

---

## 2. Docker Build Could Not Find JAR

### Problem

Docker Compose failed with:

```text
COPY build/libs/*.jar app.jar

failed to solve:
lstat /build/libs: no such file or directory
```

### Cause

The Dockerfile expected the Gradle JAR to already exist under:

```text
build/libs/
```

but the project had not been built before Docker attempted to copy the JAR.

### Solution

Build each service before building the Docker images:

```powershell
cd services/orderapi
.\gradlew.bat clean build

cd ../order-processor
.\gradlew.bat clean build

cd ../notification-service
.\gradlew.bat clean build
```

Then return to the project root and build the containers:

```powershell
cd ../..
docker compose build
```

---

## 3. Docker Compose Order Processor Returned HTTP 500

### Problem

Calling:

```powershell
Invoke-RestMethod `
  -Method POST `
  -Uri "http://localhost:8082/orders/process" `
  -ContentType "application/json" `
  -Body '{"id":1,"customerName":"Alex","product":"Laptop","quantity":1}'
```

initially returned:

```text
500 Internal Server Error
```

### Cause

The order processor communicates with the notification service. The dependent service/configuration needed to be available and correctly configured.

### Troubleshooting

Check running containers:

```powershell
docker compose ps
```

Check order processor logs:

```powershell
docker compose logs order-processor
```

Check notification service logs:

```powershell
docker compose logs notification-service
```

Once the dependent services were running correctly, the request worked successfully.

---

## 4. Order Processor Unit Test Compilation Error

### Problem

The test failed with:

```text
incompatible types: NotificationClient cannot be converted to Builder
```

The test contained:

```java
new OrderProcessorService(notificationClient);
```

### Cause

The constructor of `OrderProcessorService` expected a different argument type.

### Solution

The test setup was updated to match the current constructor/dependency structure of `OrderProcessorService`.

After the correction:

```powershell
.\gradlew.bat clean test
```

completed successfully.

---

## 5. Order API Unit Test Compilation Error

### Problem

The test initially used:

```java
orderService = new OrderService();
```

but compilation reported:

```text
constructor OrderService in class OrderService cannot be applied to given types;

required: OrderProcessorClient
found:    no arguments
```

### Cause

`OrderService` had been changed to require an `OrderProcessorClient` dependency.

### Solution

The test was updated to provide/mock the required dependency.

After the correction:

```powershell
.\gradlew.bat clean test
```

and the build completed successfully.

---

## 6. Java Public Class / Filename Mismatch

### Problem

Compilation failed with:

```text
class OrderApiApplication is public,
should be declared in a file named OrderApiApplication.java
```

### Cause

The Java file name and public class name did not match.

For example:

```text
OrderapiApplication.java
```

contained:

```java
public class OrderApiApplication
```

Java requires a public class to have the same name as its source file.

### Solution

Rename the file so that it matches the public class:

```text
OrderApiApplication.java
```

Then rebuild:

```powershell
.\gradlew.bat clean test build
```

---

## 7. Kubernetes Pod Initially Showed NotReady

### Problem

After starting Minikube:

```powershell
kubectl get nodes
```

initially showed:

```text
minikube   NotReady
```

### Cause

Minikube was still initializing the Kubernetes control plane and networking components.

### Solution

Wait a few seconds and check again:

```powershell
kubectl get nodes
```

The node eventually changed to:

```text
minikube   Ready   control-plane
```

---

## 8. Kubernetes Service Returned HTTP 500

### Problem

The Order API was accessible through the Minikube NodePort, but creating an order initially returned:

```text
500 Internal Server Error
```

### Troubleshooting

Check the services:

```powershell
kubectl get services
```

Check the pods:

```powershell
kubectl get pods
```

Check service endpoints:

```powershell
kubectl get endpoints
```

Check application logs:

```powershell
kubectl logs deployment/order-api
kubectl logs deployment/order-processor
kubectl logs deployment/notification-service
```

The important dependency chain is:

```text
Order API
    ↓
Order Processor
    ↓
Notification Service
```

All services must be running and reachable using their Kubernetes service names.

After correcting the service configuration, the request succeeded.

---

## 9. Accessing a Kubernetes NodePort with Minikube

### Problem

Running:

```powershell
minikube service order-api --url
```

returned an address such as:

```text
http://127.0.0.1:62201
```

Minikube also displayed a message that the terminal needed to remain open when using the Docker driver on Windows.

### Cause

With the Docker driver on Windows, Minikube uses a local forwarding mechanism for accessing NodePort services.

### Solution

Keep the terminal running:

```powershell
minikube service order-api --url
```

Then use the displayed URL from another terminal.

Example:

```powershell
Invoke-RestMethod `
  -Method POST `
  -Uri "http://127.0.0.1:62201/orders" `
  -ContentType "application/json" `
  -Body '{"customerName":"Alex","product":"Laptop","quantity":1}'
```

The request successfully returned the created order.

---

## 10. Helm Release Already Exists

### Problem

Running:

```powershell
helm install order-processing-platform .\helm
```

returned:

```text
cannot reuse a name that is still in use
```

### Cause

The Helm release had already been installed.

### Solution

Check the release:

```powershell
helm status order-processing-platform
```

If it is already deployed, use:

```powershell
helm upgrade order-processing-platform .\helm
```

instead of `helm install`.

---

## 11. Terraform Import Identifier Error

### Problem

Running:

```powershell
terraform import helm_release.order_processing_platform order-processing-platform
```

failed with:

```text
Unable to parse identifier order-processing-platform:
Unexpected ID format, expected namespace/name
```

### Cause

The Helm provider requires the release import ID in:

```text
namespace/name
```

format.

### Solution

Use:

```powershell
terraform import helm_release.order_processing_platform default/order-processing-platform
```

After importing, verify:

```powershell
terraform plan
```

---

## 12. Terraform Detected an Existing Helm Release

### Problem

After importing the Helm release, Terraform showed:

```text
Plan: 0 to add, 1 to change, 0 to destroy.
```

The change was mainly:

```text
chart = "order-processing-platform" -> "../helm"
```

### Cause

The Helm release already existed, but Terraform's configuration referenced the local Helm chart:

```text
../helm
```

Terraform therefore needed to reconcile the existing release with the configuration.

### Solution

Apply the Terraform configuration:

```powershell
terraform apply
```

After applying, verify:

```powershell
terraform plan
```

Expected result:

```text
No changes.
Your infrastructure matches the configuration.
```

---

## 13. Terraform Configuration Directory Renamed

### Problem

The infrastructure directory was renamed from:

```text
terraform/
```

to:

```text
infra/
```

### Verification

Run:

```powershell
cd infra
terraform validate
terraform plan
```

Expected result:

```text
Success! The configuration is valid.
```

and:

```text
No changes.
Your infrastructure matches the configuration.
```

The Terraform configuration continued to work after the directory rename.

---

## 14. Checking for Secrets Before Git Commit

Before committing the project, staged files were checked for common secret patterns.

Use:

```powershell
git grep --cached -n -i -E "password|secret|token|api[_-]?key"
```

Only expected `.gitignore` entries were returned.

No application credentials or API keys were found in the staged files.

---

## 15. Useful Kubernetes Debugging Commands

When troubleshooting Kubernetes deployments, these commands are useful:

### Check nodes

```powershell
kubectl get nodes
```

### Check pods

```powershell
kubectl get pods
```

### Check services

```powershell
kubectl get services
```

### Check deployments

```powershell
kubectl get deployments
```

### Check endpoints

```powershell
kubectl get endpoints
```

### Check logs

```powershell
kubectl logs deployment/order-api
kubectl logs deployment/order-processor
kubectl logs deployment/notification-service
```

### Describe a pod

```powershell
kubectl describe pod <pod-name>
```

### Check all resources

```powershell
kubectl get all
```

---

## 16. Useful Helm Debugging Commands

Check the installed release:

```powershell
helm status order-processing-platform
```

List releases:

```powershell
helm list
```

Validate the chart:

```powershell
helm lint .\helm
```

Render templates locally:

```powershell
helm template order-processing-platform .\helm
```

Upgrade the release:

```powershell
helm upgrade order-processing-platform .\helm
```

Check release history:

```powershell
helm history order-processing-platform
```

---

## 17. Useful Terraform Debugging Commands

Initialize Terraform:

```powershell
terraform init
```

Validate configuration:

```powershell
terraform validate
```

Preview changes:

```powershell
terraform plan
```

Apply changes:

```powershell
terraform apply
```

Check Terraform state:

```powershell
terraform state list
```

Verify that no further changes are required:

```powershell
terraform plan
```

Expected result:

```text
No changes.
Your infrastructure matches the configuration.
```
