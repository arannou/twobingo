# Docker Setup Guide for Twobingo

## Overview

This project is containerized for both development and production environments. It consists of:
- **Backend**: Spring Boot 3.2.3 application (Java 17)
- **Frontend**: Vue.js 3 application with Vite
- **Database**: PostgreSQL 16

## Prerequisites

- Docker 20.10+
- Docker Compose 2.0+

## Quick Start - Development

### 1. Clone and Setup

```bash
cd /path/to/twobingo
cp .env.example .env
```

### 2. Update `.env` file (optional)
The `.env.example` includes default dev values. If needed, update:
```env
DB_PASSWORD=your_secure_password
SPRING_PROFILE=dev
```

### 3. Start All Services

```bash
docker-compose up -d
```

This will:
- Start PostgreSQL on port 5432
- Build and start backend on port 8081
- Build and start frontend on port 80

### 4. Verify Services

```bash
# Check services status
docker-compose ps

# View logs
docker-compose logs -f

# Check backend health
curl http://localhost:8081/actuator/health

# Access frontend
open http://localhost
```

## Production Deployment

### 1. Build Images

```bash
# Build backend
docker build -t twobingo-backend:latest .

# Build frontend
docker build -f Dockerfile.frontend -t twobingo-frontend:latest .

# Build database image (optional - use managed PostgreSQL in production)
```

### 2. Set Environment Variables

```bash
export DB_HOST=your-production-db-host
export DB_PORT=5432
export DB_NAME=twobingo
export DB_USER=postgres
export DB_PASSWORD=your_very_secure_password
export SPRING_PROFILE=prod
export ADMIN_PASSWORD=your_admin_password
```

### 3. Run with Docker Compose (Production)

Create a `docker-compose.prod.yml`:
```bash
# Remove the db service and update backend/frontend services to use production databases
```

Or deploy to Kubernetes/Docker Swarm with:
```bash
docker stack deploy -c docker-compose.yml twobingo
```

### 4. Reverse Proxy Setup (Nginx/HAProxy)

In production, use an external reverse proxy (not the container's nginx) pointing to:
- Backend: `http://backend:8081`
- Frontend: `http://frontend:80`

## Configuration

### Backend Configuration Files

- **Development**: `src/main/resources/application-dev.properties`
- **Production**: `src/main/resources/application-prod.properties`
- **Default**: `src/main/resources/application.properties`

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | PostgreSQL host |
| `DB_PORT` | 5432 | PostgreSQL port |
| `DB_NAME` | twobingo | Database name |
| `DB_USER` | postgres | Database user |
| `DB_PASSWORD` | postgres | Database password |
| `SPRING_PROFILE` | dev | Spring active profile (dev/prod) |
| `JAVA_OPTS` | -Xmx512m -Xms256m | JVM options |
| `ADMIN_PASSWORD` | - | Admin user password (prod only) |

## Common Commands

```bash
# Start services in background
docker-compose up -d

# Stop all services
docker-compose down

# Remove all data (volumes included)
docker-compose down -v

# View logs for specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f db

# Rebuild specific service
docker-compose up -d --build backend

# Execute command in running container
docker-compose exec backend sh
docker-compose exec frontend sh
docker-compose exec db psql -U postgres -d twobingo

# Stop and remove specific service
docker-compose rm -s backend
```

## Troubleshooting

### Backend fails to start
```bash
# Check logs
docker-compose logs backend

# Common issues:
# 1. Database not ready - wait for healthcheck
# 2. Wrong DATABASE URL - verify DB_HOST, DB_PORT, DB_NAME
# 3. Port 8081 already in use - change in docker-compose.yml
```

### Frontend shows blank page
```bash
# Check nginx logs
docker-compose logs frontend

# Verify API URL in nginx.conf points to backend
# Check browser console for CORS issues
```

### Database connection issues
```bash
# Check PostgreSQL is running
docker-compose exec db pg_isready -U postgres

# Test connection
docker-compose exec db psql -U postgres -c "SELECT version();"
```

### Reset everything
```bash
# Stop and remove all containers, volumes, and networks
docker-compose down -v
docker system prune -a

# Start fresh
docker-compose up -d
```

## Performance Optimization

### For Development
- Mount source code as volumes for hot-reload (optional in docker-compose.yml)
- Reduce Xmx setting for faster startup

### For Production
- Use managed PostgreSQL (AWS RDS, Azure Database, etc.)
- Deploy frontend to CDN
- Use container orchestration (Kubernetes, Docker Swarm)
- Enable caching headers (configured in nginx.conf)
- Monitor with Prometheus/Grafana (expose /actuator/metrics endpoint)

## Security Considerations

### Current Implementation
- ✅ Non-root user in containers
- ✅ Health checks configured
- ✅ Environment-based secrets (use `.env`)
- ✅ Nginx configured to deny dot-files

### Before Production
- ⚠️ Use secrets management (AWS Secrets Manager, HashiCorp Vault, etc.)
- ⚠️ Enable SSL/TLS termination at reverse proxy
- ⚠️ Configure CORS properly
- ⚠️ Set `spring.jpa.hibernate.ddl-auto=validate` (already done in prod config)
- ⚠️ Implement rate limiting
- ⚠️ Set up log aggregation
- ⚠️ Use private Docker registry for images

## Deployment Examples

### AWS (ECS + RDS)
1. Push images to ECR
2. Create ECS task definitions
3. Use RDS for PostgreSQL
4. Use ALB for load balancing

### Kubernetes
```bash
# Would require k8s manifests (Deployment, Service, ConfigMap, Secret)
# See kubernetes/ directory for examples
```

### Docker Swarm
```bash
docker swarm init
docker stack deploy -c docker-compose.yml twobingo
docker stack ps twobingo
```

## Monitoring

The application exposes Spring Boot Actuator endpoints:
- Health: `http://localhost:8081/actuator/health`
- Metrics: `http://localhost:8081/actuator/metrics`
- Info: `http://localhost:8081/actuator/info`

## Next Steps

1. **Update SecurityConfig.java** to set proper CORS origins
2. **Add environment-specific logging** (e.g., send to CloudWatch/ELK)
3. **Implement database schema versioning** with Flyway/Liquibase
4. **Set up CI/CD** to build and push Docker images on commits
5. **Configure auto-scaling** for production
6. **Add rate limiting and DDoS protection**
7. **Implement observability** (Prometheus, Grafana, Jaeger)
