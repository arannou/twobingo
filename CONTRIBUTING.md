# Database
## Ensure postgres service and pgadmin are on the same network
```
podman network ls
podman network create pg-network
```

## Start postgres
```
podman run -d   --name postgres-container   --network pg-network   -e POSTGRES_PASSWORD=1234   -p 5432:5432   postgres
```
## Start pgAdmin4 (optionnal)
```
podman run -d   --name pgadmin-container   --network pg-network   -p 8080:80   -e PGADMIN_DEFAULT_EMAIL=admin@test.com   -e PGADMIN_DEFAULT_PASSWORD=admin   dpage/pgadmin4
```

Open pgAdmin web console here: http://localhost:8080/browser/ and login with admin@test.com / 1234

# Backend
## launch app locally
```
mvn spring-boot:run
or optional -DskipTests 

#Kill previously running java sprocess with 
taskkill /F /FI "MEMUSAGE gt 0" /IM java.exe 2>/dev/null || true
```
Ensure it runs by visiting http://localhost:8081/api/twobingo/health

# Frontend
Ensure node version = 25 and npm version = 11
```
node -v && npm -v
```
## Install project deps
```
cd frontend
npm install
```
## Run locally
```
npm run dev
```
Ensure it works by visiting http://localhost:5173/

# Backlog
## Done
* create backend and basic api
* create frontend and basic ui
* create grid
* edit and delete grids


## In progress
* play grid
    - view
    - update isChecked
    - sockets

## Todo

### Priority high
* fix creator issue
* archive grid
* grid settings:
    - join with link or with approval
* add participants
* update grid in real time with sockets

### Priority medium

* handle un-registred users

### Priority low
* add aria labels, titles and other accessibility features
* add frontend toast notifications
* add tests
* add password encryption
* add user last login date
* add auto-clean up and management interface

* add about and legal and contact
* add docker file and other deployment preparation
* ensure app is secure

* feature: ordered cells or random
* feature: check a line and win
* feature: kick a player