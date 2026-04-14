#!/bin/bash
# Production deployment script for AWS EC2 / Docker Swarm

set -e

DEPLOYMENT_ENV=${1:-prod}
DOCKER_REGISTRY=${DOCKER_REGISTRY:-docker.io}
IMAGE_TAG=${2:-latest}

echo "🚀 Deploying Twobingo - Environment: $DEPLOYMENT_ENV"
echo "=================================================="
echo ""

# Load environment variables
if [ -f ".env.$DEPLOYMENT_ENV" ]; then
    echo "📝 Loading environment from .env.$DEPLOYMENT_ENV"
    export $(cat .env.$DEPLOYMENT_ENV | grep -v '#' | xargs)
else
    echo "⚠️  .env.$DEPLOYMENT_ENV not found. Using default environment."
fi

# Pull latest images
echo "📥 Pulling latest images..."
docker pull $DOCKER_REGISTRY/twobingo-backend:$IMAGE_TAG
docker pull $DOCKER_REGISTRY/twobingo-frontend:$IMAGE_TAG
echo "✅ Images pulled successfully"
echo ""

# Verify images
echo "🔍 Verifying images..."
docker image inspect $DOCKER_REGISTRY/twobingo-backend:$IMAGE_TAG > /dev/null && echo "✅ Backend image verified"
docker image inspect $DOCKER_REGISTRY/twobingo-frontend:$IMAGE_TAG > /dev/null && echo "✅ Frontend image verified"
echo ""

# Run health checks
echo "📊 Running pre-deployment checks..."

# Check database connectivity
echo "Checking database connectivity..."
if ! docker run --rm \
    -e PGPASSWORD="$DB_PASSWORD" \
    postgres:16-alpine \
    pg_isready -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" > /dev/null 2>&1; then
    echo "❌ Cannot connect to database at $DB_HOST"
    exit 1
fi
echo "✅ Database connectivity verified"
echo ""

# Deploy with Docker Stack (if in Swarm mode)
if docker info | grep -q "Swarm: active"; then
    echo "🐳 Deploying with Docker Swarm..."
    docker stack deploy -c docker-compose.yml twobingo
    
    echo ""
    echo "⏳ Waiting for services to be ready..."
    sleep 10
    
    # Verify deployment
    RUNNING=$(docker stack ps twobingo | grep -c "Running" || true)
    TOTAL=$(docker stack ps twobingo | grep -c "twobingo" || true)
    
    if [ "$RUNNING" -eq "$TOTAL" ]; then
        echo "✅ All services are running"
    else
        echo "⚠️  Some services are not yet running. Check: docker stack ps twobingo"
    fi
else
    echo "🐳 Docker Swarm not initialized. Using docker-compose..."
    docker-compose -f docker-compose.yml up -d
    
    echo ""
    echo "⏳ Waiting for services to be ready..."
    sleep 15
fi

echo ""
echo "✨ Deployment complete!"
echo ""
echo "📊 Service Status:"
if docker info | grep -q "Swarm: active"; then
    docker stack ps twobingo
else
    docker-compose ps
fi
echo ""
echo "📌 Next steps:"
echo "   1. Monitor logs: docker service logs -f twobingo_backend"
echo "   2. Check health: curl http://localhost:8081/actuator/health"
echo "   3. Access frontend: http://localhost"
