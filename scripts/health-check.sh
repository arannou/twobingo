#!/bin/bash
# Health check verification script for local development

echo "🏥 Twobingo Health Check"
echo "========================"
echo ""

# Check Docker Compose services
echo "📦 Service Status:"
docker-compose ps
echo ""

# Check Backend Health
echo "🔵 Backend Health:"
BACKEND_HEALTH=$(curl -s http://localhost:8081/actuator/health)
if [ $? -eq 0 ]; then
    echo "✅ Backend: $BACKEND_HEALTH"
else
    echo "❌ Backend: Unreachable (waiting to start...)"
fi
echo ""

# Check Database Health
echo "🟢 Database Health:"
DB_STATUS=$(docker-compose exec -T db pg_isready -U postgres 2>/dev/null)
if [ $? -eq 0 ]; then
    echo "✅ Database: Connected"
else
    echo "❌ Database: Not ready"
fi
echo ""

# Check Frontend Health
echo "🟡 Frontend Health:"
FRONTEND_HEALTH=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/)
if [ "$FRONTEND_HEALTH" -eq 200 ]; then
    echo "✅ Frontend: Running (HTTP $FRONTEND_HEALTH)"
else
    echo "❌ Frontend: Not responding (HTTP $FRONTEND_HEALTH)"
fi
echo ""

echo "📊 Endpoints:"
echo "  Frontend:  http://localhost"
echo "  Backend:   http://localhost:8081"
echo "  Health:    http://localhost:8081/actuator/health"
echo "  Metrics:   http://localhost:8081/actuator/metrics"
echo ""

echo "💡 Tip: Run 'docker-compose logs -f' to view real-time logs"
