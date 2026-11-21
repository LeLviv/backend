# LeLviv Backend

[![GHCR Image](https://img.shields.io/badge/GHCR-backend%3Adevelop-2088FF?logo=github)](https://github.com/LeLviv/backend/pkgs/container/backend)


![GitHub PRs](https://img.shields.io/github/issues-pr/LeLviv/backend)
![GitHub last commit](https://img.shields.io/github/last-commit/LeLviv/backend)
![Build Status](https://img.shields.io/github/actions/workflow/status/LeLviv/backend/build.yaml?branch=develop&label=build)

![Java](https://img.shields.io/badge/Java-Temurin_25-FF1464?logo=eclipseadoptium&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.6-4169E1?logo=postgresql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-Build_Tool-02303A?logo=gradle&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger_UI-85EA2D?logo=swagger&logoColor=black)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?logo=flyway&logoColor=white)


Backend REST API service for the LeLviv project, built with **Java 25**, **Spring Boot 4**, and **PostgreSQL**.

How to run:
- [From source code](#run-from-source) _(recommended for backend development)_
- [With Docker Compose](#run-with-docker-compose) _(recommended for mobile development and demo)_


## Navigation
- [Prerequisites](#prerequisites)
- [Run from Source (Local Development)](#run-from-source)
- [Run with Docker Compose (Mobile / Demo)](#run-with-docker-compose)
- [Endpoints](#endpoints)
- [Configuration](#configuration-env)
- [Advanced Configuration](#advanced-configuration)
- [Solve port conflicts](#port-conflicts)
- [Container Management](#container-management)
- [Spring Boot Profiles](#spring-boot-profiles)
- [Docker Images](#docker-images)



## Run from source

### Prerequisites
1. Java 25:
   - This project uses Eclipse Temurin 25 from Adoptium as its Java distribution.
   - To install Temurin 25: [Eclipse Temurin](https://adoptium.net/installation).
   - For compatibility purposes, it is highly recommended to use the same distribution in all environments (Eclipse Temurin).
   
2. Docker and Docker Compose:
   - Install [Docker Desktop](https://docs.docker.com/desktop/) or Docker Engine with Docker Compose v2+.
   - You don't need to manage containers manually; Spring Boot will automatically create, start, and stop development containers.


Steps to run:

1. Clone the repository:

    ```sh
    git clone git@github.com:LeLviv/backend.git
    cd backend
    ```

2. Copy [`.env.example`](.env.example) to `.env` and set [environment variables](#configuration-env):
    ```sh
    cp .env.example .env
    ```

    > [!IMPORTANT]
    > It is highly recommended to set a strong custom password instead of using defaults.
   
3. Run the backend:
   - CLI: `./gradlew bootRun`
   - IDE: set `SPRING_PROFILES_ACTIVE=local` and run `ua.garage.lelviv.LeLvivApplication`

4. Check [Endpoints](#endpoints).


> [!IMPORTANT]
> If running from an IDE, ensure the local profile is enabled (e.g. set SPRING_PROFILES_ACTIVE=local).
> When running via ./gradlew bootRun, the local profile is active by default.


### Gradle Commands
- Run tests: `./gradlew test`
- Build application JAR: `./gradlew build`
- Build Docker image: `./gradlew bootBuildImage --imageName=lelviv/backend:tag`
- Run without container autostart: `./gradlew bootRun --args='--spring.docker.compose.enabled=false'`


## Run with Docker Compose

This method can be used for mobile development, demos, or a quick run.

The Compose file includes the backend and all necessary services.

For a quick run:
1. Install [Docker Desktop](https://docs.docker.com/desktop/) or Docker Engine with Docker Compose v2+
2. Make directory:
   ```sh
   mkdir LeLviv-compose && cd LeLviv-compose
   ```
3. Download two compose files:
   ```sh
   curl -O "https://raw.githubusercontent.com/LeLviv/backend/develop/{compose,spring}.yaml"
   ```
4. Run:
   ```sh
   docker compose up -d
   ```

> [!NOTE]
> If you already cloned the repository, simply run `docker compose up -d` in the root folder.


For container management, visit [Container Management](#container-management).

### Configuration

The Docker Compose method works out-of-the-box with zero configuration.

If you want to change something, create a `.env` file and set [environment variables](#configuration-env).

Compose-specific variables:

| Variable | Default Value | Description |
|---|---|---|
| `BACK_TAG` | `develop` | Backend image tag |
| `BACK_IMAGE` | `ghcr.io/lelviv/backend:${BACK_TAG:-develop}` | Backend image |


To update Backend image run:
```sh
docker compose pull
```


## Endpoints

- Base URL: [http://127.0.0.1:8080/](http://127.0.0.1:8080/)
- Health: [http://127.0.0.1:8080/actuator/health](http://127.0.0.1:8080/actuator/health)
- Swagger: [http://127.0.0.1:8080/swagger-ui.html](http://127.0.0.1:8080/swagger-ui.html)


## Configuration (.env)

These variables are defined in [`.env.example`](.env.example) and required for standard setup:

| Variable | Default Value | Description |
|---|---|---|
| `POSTGRES_DB` | `lelviv` | Database name |
| `POSTGRES_USER` | `lelviv` | Database username |
| `POSTGRES_PASSWORD` | `use-some-strong-password-for-access-to-db` | Database password* |



<small><i>* It is highly recommended to set a strong custom password instead of using defaults.</i></small>

> [!TIP]
> Spring Boot with the `local` profile will import the `.env` file.  
> So you can put Spring Boot configuration in `.env`.


## Advanced Configuration

Spring Boot and Docker Compose use the `.env` file for configuration.

Common configuration:

| Variable | Default Value | Description |
|---|---|---|
| `SERVER_PORT` | `8080` | HTTP backend port |
| `POSTGRES_PORT` | `5432` | PostgreSQL port |
| `SPRING_PROFILES_ACTIVE` | `default`* | Active profile (`local`, `prod`) |

<small><i>* When running via ./gradlew bootRun, the `local` profile is active by default.</i></small>


### Port Conflicts

To solve port conflicts, set the port variable in `.env`:

For example: `SERVER_PORT=<your port>`

<details>
<summary><b>Host Binding</b></summary>

By default, Spring Boot and Docker Compose bind to localhost.

If you want to make services accessible from outside your machine, use these environment variables:

| Variable | Default Value | Description |
|---|---|---|
| `SERVER_HOST` | `127.0.0.1` | IP address to bind backend on host |
| `POSTGRES_HOST` | `127.0.0.1` | IP address to bind PostgreSQL on host |

</details>


## Container Management

<details>
<summary>Click to view commands</summary>

These commands will help you work with containers.

They can be used with both running methods.

When you run from source code, Spring Boot will automatically start and stop containers.

- View running containers: `docker compose ps`
- Stop containers: `docker compose stop`
- Start containers: `docker compose start`
- Destroy containers: `docker compose down`
- Delete containers & volumes: `docker compose down -v`

> [!CAUTION]
> Deleting volumes causes database data loss.
> Be sure that you want to delete your data.  
> Create migrations so that they work without dropping the database.

</details>


## Spring Boot Profiles

Different environments (development or production) need different configurations.

To switch configuration sets, Spring Boot profiles are used.


| Profile | Environment |
| --- | --- |
| default | Common |
| local | Development |
| prod | Production |


### Custom Spring Boot Configs & Overrides

For your custom configuration and overrides, use `config/application-custom.yaml`, which will be imported by the `local` profile.

This file is ignored by Git.


## Docker Images

[![GHCR Image](https://img.shields.io/badge/GHCR-backend%3Adevelop-2088FF?logo=github)](https://github.com/LeLviv/backend/pkgs/container/backend)

Backend images are published to [GitHub Container Registry](https://github.com/LeLviv/backend/pkgs/container/backend)

| Tag | Description |
|---|---|
| `develop` | Development tag, can be used for mobile or demo |
| `<commit-sha>` | Specific commit |


Pull latest dev image:
```sh
docker pull ghcr.io/lelviv/backend:develop
```
