# 🐳 Quick Start Guide - Twobingo Docker

Get your Twobingo project running in Docker in 2 minutes!

## Prerequisites

- Docker Desktop installed ([Download](https://www.docker.com/products/docker-desktop))
- Git
- macOS, Linux, or Windows with WSL2

## ⚡ Quick Start

### 1️⃣ Clone and Navigate

```bash
cd /path/to/twobingo
```

### 2️⃣ Start Docker Setup Script

```bash
bash scripts/setup-docker.sh
```

This will:
- ✅ Check Docker installation
- ✅ Create `.env` file
- ✅ Build all images
- ✅ Start all services
- ✅ Run health checks

### 3️⃣ Access Your Application

```
🌐 Frontend:  http://localhost
🔧 Backend:   http://localhost:8081
🏥 Health:    http://localhost:8081/actuator/health
```

### 4️⃣ Stop Services

```bash
docker-compose down
```

---

## 📋 What Gets Started?

| Service | Port | Access |
|---------|------|--------|
| PostgreSQL | 5432 | Internal only |
| Backend (Spring Boot) | 8081 | http://localhost:8081 |
| Frontend (Nginx) | 80 | http://localhost |

---

## 🛠 Common Commands

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f db
```

### Rebuild Services

```bash
# Rebuild and restart backend
docker-compose up -d --build backend

# Rebuild everything
docker-compose up -d --build
```

### Access Database

```bash
# Connect to PostgreSQL
docker-compose exec db psql -U postgres -d twobingo

# Useful queries:
# \dt - list tables
# \q - quit
```

### Execute Commands in Container

```bash
# Backend shell
docker-compose exec backend sh

# Frontend shell
docker-compose exec frontend sh
```

### Health Status

```bash
bash scripts/health-check.sh
```

---

## 🔧 Configuration

Edit `.env` file to customize:

```env
DB_NAME=twobingo          # Database name
DB_USER=postgres          # Database user
DB_PASSWORD=postgres      # Database password
SPRING_PROFILE=dev        # Profile (dev/prod)
```

After changing `.env`:
```bash
docker-compose down
docker-compose up -d
```

---

## 🐛 Troubleshooting

### Backend won't start
```bash
docker-compose logs backend
# Check: Database connection, PORT 8081 availability
```

### Frontend shows blank page
```bash
docker-compose logs frontend
# Check: Browser console for errors
# Verify: Nginx proxy configuration
```

### Port already in use
Change ports in `docker-compose.yml`:
```yaml
ports:
  - "8082:8081"  # Backend on 8082
```

### Reset everything
```bash
docker-compose down -v              # Remove volumes
docker system prune -a              # Clean up unused resources
docker-compose up -d                # Start fresh
```

---

## 📚 Learn More

- **Full Docker Setup Guide**: See [DOCKER_SETUP.md](DOCKER_SETUP.md)
- **Production Deployment**: See [DOCKER_SETUP.md#production-deployment](DOCKER_SETUP.md#production-deployment)
- **Production Checklist**: See [PRODUCTION_CHECKLIST.md](PRODUCTION_CHECKLIST.md)

---

## 🧪 Testing

### Backend API

```bash
# Health check
curl http://localhost:8081/actuator/health

# Get metrics
curl http://localhost:8081/actuator/metrics
```

### Frontend

```bash
# Test frontend is serving
curl -I http://localhost
```

---

## 🚀 Next Steps (After Testing)

1. ✅ Test the application locally
2. 🔐 Update database password before production
3. 📝 Review [PRODUCTION_CHECKLIST.md](PRODUCTION_CHECKLIST.md)
4. 🏗️ Set up CI/CD pipeline (.github/workflows/)
5. 📦 Push Docker images to registry
6. 🌍 Deploy to production infrastructure

---

## 📞 Support

### Check Service Status
```bash
docker-compose ps
```

### View Docker Info
```bash
docker info
docker version
```

### Get Help
```bash
docker-compose --help
docker ps --help
```

---

## 📝 Notes

- Database data persists in `postgres_data` volume
- Remove volume with `docker-compose down -v` to reset database
- Frontend hot-reload available with `docker-compose.dev.yml`
- Use `docker-compose.prod.yml` pattern for production (customize as needed)

---

**Happy coding! 🎉**

For detailed information, see [DOCKER_SETUP.md](DOCKER_SETUP.md)
