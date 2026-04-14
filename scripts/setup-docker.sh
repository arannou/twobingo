#!/bin/bash
# Setup script for local Docker development

set -e

echo "🚀 Setting up Twobingo with Docker Compose"
echo "=========================================="
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker Desktop."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose."
    exit 1
fi

echo "✅ Docker and Docker Compose are installed"
echo ""

# Create .env if it doesn't exist
if [ ! -f .env ]; then
    echo "📝 Creating .env file from .env.example..."
    cp .env.example .env
    echo "✅ .env created. Update it if needed."
else
    echo "✅ .env file already exists"
fi
echo ""

# Build images
echo "🔨 Building Docker images..."
docker-compose build --no-cache
echo ""

# Start services
echo "🟢 Starting services..."
docker-compose up -d
echo ""

# Wait for services to be ready
echo "⏳ Waiting for services to be healthy..."
sleep 10

# Run health check
echo ""
echo "📊 Running health checks..."
bash scripts/health-check.sh

echo ""
echo "✨ Setup complete!"
echo ""
echo "📌 Next steps:"
echo "   1. Update .env with your database password if needed"
echo "   2. Access frontend at: http://localhost"
echo "   3. Backend API at: http://localhost:8081"
echo "   4. View logs: docker-compose logs -f"
echo "   5. Stop services: docker-compose down"
