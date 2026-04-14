# Kubernetes Deployment Files for Twobingo

This directory contains Kubernetes manifests for deploying Twobingo to a Kubernetes cluster.

## Files

- `namespace.yaml` - Namespace for Twobingo resources
- `configmap.yaml` - ConfigMap for application configuration
- `secret.yaml` - Secrets for sensitive data (generate from .env)
- `postgres-statefulset.yaml` - PostgreSQL StatefulSet
- `backend-deployment.yaml` - Backend Deployment
- `frontend-deployment.yaml` - Frontend Deployment
- `services.yaml` - Kubernetes Services
- `ingress.yaml` - Ingress for external access
- `hpa.yaml` - Horizontal Pod Autoscaler

## Prerequisites

```bash
- Kubernetes 1.22+
- kubectl configured
- Docker images pushed to a container registry
```

## Deployment

### 1. Create Secrets from .env

```bash
# Generate secret from .env file
kubectl create secret generic twobingo-secret \
  --from-env-file=.env \
  --namespace=twobingo \
  --dry-run=client \
  -o yaml > kubernetes/secret.yaml
```

### 2. Apply Manifests

```bash
# Create namespace
kubectl apply -f kubernetes/namespace.yaml

# Create configuration
kubectl apply -f kubernetes/configmap.yaml

# Apply secrets
kubectl apply -f kubernetes/secret.yaml

# Deploy database
kubectl apply -f kubernetes/postgres-statefulset.yaml
kubectl apply -f kubernetes/postgres-service.yaml

# Wait for database
kubectl wait --for=condition=ready pod -l app=postgres -n twobingo --timeout=300s

# Deploy backend
kubectl apply -f kubernetes/backend-deployment.yaml
kubectl apply -f kubernetes/backend-service.yaml

# Deploy frontend
kubectl apply -f kubernetes/frontend-deployment.yaml
kubectl apply -f kubernetes/frontend-service.yaml

# Create Ingress
kubectl apply -f kubernetes/ingress.yaml

# Setup autoscaling
kubectl apply -f kubernetes/hpa.yaml
```

### 3. Verify Deployment

```bash
# Check all resources
kubectl get all -n twobingo

# Check pods
kubectl get pods -n twobingo -w

# Check logs
kubectl logs -f deployment/twobingo-backend -n twobingo
kubectl logs -f deployment/twobingo-frontend -n twobingo

# Port forward for testing
kubectl port-forward -n twobingo svc/twobingo-backend 8081:8081
kubectl port-forward -n twobingo svc/twobingo-frontend 80:80
```

## See kubernetes/ directory for the actual manifest files
