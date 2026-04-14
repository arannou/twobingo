# Production Deployment Checklist

## Pre-Deployment Security

- [ ] Update `DB_PASSWORD` in production environment
- [ ] Set `ADMIN_PASSWORD` in production environment
- [ ] Update `SPRING_PROFILE=prod` in environment
- [ ] Disable `spring.h2.console.enabled`
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (not update)
- [ ] Configure SSL/TLS certificates
- [ ] Set up secrets management (AWS Secrets Manager, HashiCorp Vault, etc.)
- [ ] Configure firewall/security groups
- [ ] Review and update CORS configuration in `SecurityConfig.java`
- [ ] Enable HTTPS only
- [ ] Set secure headers (HSTS, CSP, X-Frame-Options, etc.)
- [ ] Configure rate limiting
- [ ] Set up Web Application Firewall (WAF)

## Database

- [ ] Use managed PostgreSQL (AWS RDS, Azure Database, Google Cloud SQL)
- [ ] Enable automated backups
- [ ] Set up backup retention policy
- [ ] Configure multi-AZ/replica for high availability
- [ ] Enable encryption at rest
- [ ] Enable encryption in transit (SSL)
- [ ] Create database user with minimal required permissions
- [ ] Run database migrations/schema setup
- [ ] Test backup and restore procedures

## Docker

- [ ] Build images with `--no-cache` flag
- [ ] Scan images for vulnerabilities (Trivy, CVE scanning)
- [ ] Push images to private Docker registry
- [ ] Tag images with version numbers (not just `latest`)
- [ ] Implement image signing (Docker Content Trust)
- [ ] Remove debug tools and unnecessary packages from base images
- [ ] Run containers as non-root user
- [ ] Set resource limits (memory, CPU)

## Infrastructure

- [ ] Set up reverse proxy (Nginx, HAProxy, AWS ALB)
- [ ] Configure load balancing across multiple backend instances
- [ ] Set up auto-scaling policies
- [ ] Configure health checks
- [ ] Set up monitoring and alerting
- [ ] Configure centralized logging (ELK, CloudWatch, Datadog)
- [ ] Set up distributed tracing (Jaeger, Zipkin)
- [ ] Configure metrics collection (Prometheus)

## Application

- [ ] Build and deploy to staging environment first
- [ ] Run full test suite
- [ ] Perform load testing
- [ ] Test database failover
- [ ] Verify API rate limiting
- [ ] Test authentication/authorization
- [ ] Verify CORS configuration
- [ ] Test file upload limits
- [ ] Test session management
- [ ] Test password reset functionality

## API Security

- [ ] Implement API key validation
- [ ] Set rate limiting per IP/API key
- [ ] Implement request validation
- [ ] Add request size limits
- [ ] Configure CORS properly
- [ ] Implement CSRF protection
- [ ] Set Content Security Policy headers
- [ ] Implement proper error handling (no sensitive info in errors)

## Monitoring & Logging

- [ ] Enable application logging (JSON format recommended)
- [ ] Set up log aggregation
- [ ] Monitor database performance
- [ ] Monitor CPU, memory, disk usage
- [ ] Monitor network I/O
- [ ] Set up alerts for critical errors
- [ ] Set up alerts for high resource usage
- [ ] Monitor API response times
- [ ] Monitor error rates
- [ ] Implement security event logging

## Deployment

- [ ] Create deployment runbook
- [ ] Plan rollback procedure
- [ ] Test deployment process
- [ ] Set up CI/CD pipeline
- [ ] Document deployment steps
- [ ] Set up blue-green or canary deployment strategy
- [ ] Test failover procedures
- [ ] Document disaster recovery plan

## Post-Deployment

- [ ] Verify all services are running
- [ ] Verify health check endpoints return 200
- [ ] Verify backend-frontend connectivity
- [ ] Verify database connectivity
- [ ] Test user login functionality
- [ ] Test core functionality
- [ ] Verify SSL certificate validity
- [ ] Monitor logs for errors
- [ ] Verify backups are working
- [ ] Document deployment completion

## Performance Tuning

- [ ] Configure JVM heap size appropriately (based on available RAM)
- [ ] Enable HTTP/2 in Nginx
- [ ] Enable gzip compression
- [ ] Configure Nginx caching for static assets
- [ ] Optimize database queries
- [ ] Configure connection pooling
- [ ] Set up CDN for static assets
- [ ] Enable browser caching headers

## Compliance & Standards

- [ ] Implement data privacy measures (GDPR, CCPA, etc.)
- [ ] Set up audit logging
- [ ] Document security policies
- [ ] Document data retention policies
- [ ] Document disaster recovery procedures
- [ ] Perform security audit
- [ ] Run penetration testing
- [ ] Document third-party dependencies
- [ ] Implement update policy for dependencies

## Maintenance

- [ ] Set up automated security patching
- [ ] Plan regular database maintenance windows
- [ ] Document known issues and workarounds
- [ ] Create incident response plan
- [ ] Schedule regular security reviews
- [ ] Monitor CVE databases for dependency vulnerabilities
- [ ] Document runbooks for common operations
