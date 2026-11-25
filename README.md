# LeLviv Backend

[![Build Status](https://img.shields.io/github/actions/workflow/status/LeLviv/backend/build.yaml?branch=develop&logo=githubactions&logoColor=white)](https://github.com/LeLviv/backend/actions/workflows/build.yaml)
[![Quality Gate Status](https://img.shields.io/sonar/quality_gate/LeLviv_backend?server=https%3A%2F%2Fsonarcloud.io&logo=sonarqubecloud&logoColor=white)](https://sonarcloud.io/summary/new_code?id=LeLviv_backend)
[![GHCR Image](https://img.shields.io/badge/GHCR-backend%3Adevelop-2088FF?label=ghcr&logo=github&logoColor=white)](https://github.com/LeLviv/backend/pkgs/container/backend)
[![GitHub PRs](https://img.shields.io/github/issues-pr/LeLviv/backend?label=prs&logo=github&logoColor=white)](https://github.com/LeLviv/backend/pulls)

![Java](https://img.shields.io/badge/Java-Temurin_25-FF1464?logo=eclipseadoptium&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-4169E1?logo=postgresql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-Build_Tool-02303A?logo=gradle&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
[![Swagger](https://img.shields.io/badge/OpenAPI-Swagger_UI-85EA2D?logo=swagger&logoColor=black)](#endpoints)

The backend REST API service for the LeLviv project, built with **Java 25**, **Spring Boot 4**, and **PostgreSQL**.

How to run:
- [From source code](#run-from-source)
- [With Docker Compose](#run-with-docker-compose) _(recommended for mobile development and demos)_

## Navigation
- [Prerequisites](#prerequisites)
- [Run from Source (Local Development)](#run-from-source)
- [Endpoints](#endpoints)
- [Configuration](#configuration-env) ([Solve Port Conflicts](#port-conflicts))
- [Spring Boot Profiles](#spring-boot-profiles)
- [Gradle Commands](#gradle-commands)
- [Code Formatting & Static Analysis](#code-formatting--static-analysis)
- [Docker Images](#docker-images)
- [Container Management](#container-management)
- [Run with Docker Compose (Mobile / Demo)](#run-with-docker-compose)

---

## Prerequisites
1. Java 25:
   - Install [Eclipse Temurin 25](https://adoptium.net/installation).
   - This project uses Eclipse Temurin 25 from Adoptium as its Java distribution.
   - For compatibility purposes, it is highly recommended to use the same distribution in all environments (Eclipse Temurin).
   
2. <a id="docker-prerequisites"></a>Docker and Docker Compose:
   - Install [Docker Desktop](https://docs.docker.com/desktop/) (macOS/Linux/Windows) or [Docker Engine](https://docs.docker.com/engine/install/) (Linux) with the [Docker Compose](https://docs.docker.com/compose/install/) plugin v2.20+.
   - You don't need to manage containers manually; Spring Boot will automatically create, start, and stop development containers.
   - This is the only requirement for [running with Docker Compose](#run-with-docker-compose).

---

## Run from Source
1. Ensure all [prerequisites](#prerequisites) are installed.

2. Clone the repository:

    ```sh
    git clone git@github.com:LeLviv/backend.git
    cd backend
    ```

3. Copy [`.env.example`](.env.example) to `.env` ([environment variables](#configuration-env)):
   ```sh
   cp .env.example .env
   ```
   
4. Run the backend:
   - CLI: `./gradlew bootRun`
   - IDE: set `SPRING_PROFILES_ACTIVE=local` and run `ua.garage.lelviv.LeLvivApplication`

5. Check [Endpoints](#endpoints).

> [!IMPORTANT]
> Only `./gradlew bootRun` enables the `local` profile by default.  
> When launching from an IDE, you must explicitly set `SPRING_PROFILES_ACTIVE=local`.

---

## Endpoints

- Base URL: [http://127.0.0.1:8080/](http://127.0.0.1:8080/)
- Health: [http://127.0.0.1:8080/actuator/health](http://127.0.0.1:8080/actuator/health)
- Swagger: [http://127.0.0.1:8080/swagger-ui.html](http://127.0.0.1:8080/swagger-ui.html)

> [!NOTE]
> If you change the port via `SERVER_PORT`, update the URLs above accordingly.

---

## Configuration (.env)

The `.env` file can be used for configuration. This file is ignored by Git.

Don't forget to create this file: `cp .env.example .env`.

These variables are defined in [`.env.example`](.env.example) and required for a standard setup:

| Variable | Default Value | Description |
|---|---|---|
| `POSTGRES_DB` | `lelviv` | Database name |
| `POSTGRES_USER` | `lelviv` | Database username |
| `POSTGRES_PASSWORD` | `lelviv` | Database password |
| `JWT_SECRET_KEY` | `ChangeThisJwtSecretKeyToAStrongRandomValue01` | JWT secret key |

> [!IMPORTANT]
> It is highly recommended to set strong custom values instead of using defaults.  
> Especially for passwords and secret keys.

The JWT secret key must be a Base64-encoded string of at least 256 bits (32 bytes).

To generate a secure JWT secret, use:
```sh
openssl rand -base64 32
```

> [!TIP]
> Spring Boot with the `local` profile will import the `.env` file.  
> So you can put Spring Boot configuration in `.env`.
> For more information visit: [Relaxed Binding](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties.relaxed-binding).

### Advanced Configuration

General configuration:

| Variable | Default Value | Description |
|---|---|---|
| `SERVER_PORT` | `8080` | HTTP backend port |
| `POSTGRES_PORT` | `5432` | PostgreSQL port |
| `SPRING_PROFILES_ACTIVE` | `default` (IDE), `local` (Gradle) | Active profile (`local`, `prod`) |
| `SPRING_DOCKER_COMPOSE_ENABLED` | `true` (local), `false` (prod) | Enable Spring container management |

For manual container management use `SPRING_DOCKER_COMPOSE_ENABLED` in .env.

### Port Conflicts

To solve port conflicts, set the port variable in `.env`:

For example: `SERVER_PORT=<your port>`

### Host Binding (External Access)

By default, services bind to localhost.

To make services accessible from outside your machine (e.g. to connect from a real phone), use these environment variables:

| Variable | Default Value | Description |
|---|---|---|
| `SERVER_HOST` | `127.0.0.1` | IP address to bind backend on host |
| `POSTGRES_HOST` | `127.0.0.1` | IP address to bind PostgreSQL on host |

> [!CAUTION]
> Exposing services can be dangerous, as anyone on the same network can access exposed services.  
> Only use this when needed (e.g. for real phone testing).

---

## Spring Boot Profiles

Different environments (development or production) need different configurations.

To switch configuration sets, Spring Boot profiles are used.

| Profile | Environment |
| --- | --- |
| default | Common |
| local | Development |
| prod | Production |

> [!NOTE]
> Spring container management is enabled only in the `local` profile.

### Custom Spring Boot Configs & Overrides

For your custom configuration and overrides, use `config/application-custom.yaml`, which will be imported by the `local` profile.

This file is ignored by Git.

---

## Gradle Commands
- Run tests: `./gradlew test`
- Build application JAR: `./gradlew build`
- Build Docker image: `./gradlew bootBuildImage --imageName=lelviv/backend:tag`
- Run without container autostart: `./gradlew bootRun --args='--spring.docker.compose.enabled=false'`
- Check code style: `./gradlew checkstyleMain`
- Check code formatting: `./gradlew spotlessCheck`
- Apply code formatting: `./gradlew spotlessApply`

---

## Code Formatting & Static Analysis

### Code Style & Formatting

Code style and formatting are managed via Spotless and Checkstyle:
- Formatting: Spotless with
   - Palantir Java Format for Java
   - Prettier for YAML
- Code style: Checkstyle ([configuration](config/checkstyle/checkstyle.xml))

For execution commands, visit [Gradle Commands](#gradle-commands)

> [!TIP]
> You can install Palantir Java Format and Checkstyle plugins in your IDE for convenient, project-specific formatting and style warnings.

### Quality Gate (SonarQube)

SonarQube automatically analyzes pull requests.

- Dashboard: [SonarQube Project](https://sonarcloud.io/summary/new_code?id=LeLviv_backend)

> [!TIP]
> Install the SonarLint plugin in your IDE to detect bugs and code smells locally. Otherwise, you'll have to fix them after creating a PR and waiting for CI.

---

## Docker Images

[![GHCR Image](https://img.shields.io/badge/GHCR-backend%3Adevelop-2088FF?label=ghcr&logo=github&logoColor=white)](https://github.com/LeLviv/backend/pkgs/container/backend)

Backend images are published to [GitHub Container Registry](https://github.com/LeLviv/backend/pkgs/container/backend).

| Tag | Description |
|---|---|
| `develop` | Development tag, can be used for mobile or demo |
| `<commit-sha>` | Specific commit |

Pull the latest dev image:
```sh
docker pull ghcr.io/lelviv/backend:develop
```

---

## Container Management

These commands will help you work with containers.

- View running containers: `docker compose ps`
- Stop containers: `docker compose stop`
- Start containers: `docker compose start`
- Destroy containers: `docker compose down`
- Update images: `docker compose pull`
- Delete containers & volumes: `docker compose down -v`

> [!CAUTION]
> Deleting volumes causes database data loss.
> Be sure that you want to delete your data.  
> Create migrations so that they work without dropping the database.

### Local Development (Run from source)

When you run from source code, Spring Boot will automatically start and stop containers (only with the `local` profile).

> [!NOTE]
> For containers managed by Spring, `-f spring.yaml` is needed with all commands, e.g.: `docker compose -f spring.yaml ps`

---

## Run with Docker Compose

This method can be used for mobile development, demos, or a quick run.

Everything you need is Docker and Docker Compose installed.

For a quick run:
1. Install [Docker & Docker Compose](#docker-prerequisites)
2. Create a directory:
   ```sh
   mkdir lelviv-compose && cd lelviv-compose
   ```
3. Download the two Compose files:
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

> [!CAUTION]
> The Docker Compose setup cannot be used in production because it has insecure defaults.

### Configuration

The Docker Compose method works out-of-the-box with zero configuration.

If you want to customize the configuration, create a `.env` file and set [environment variables](#configuration-env).

To connect from a real phone, visit [External Access](#host-binding-external-access).

Compose-specific variables:

| Variable | Default Value | Description |
|---|---|---|
| `BACK_TAG` | `develop` | Backend image tag |
| `BACK_IMAGE` | `ghcr.io/lelviv/backend:${BACK_TAG:-develop}` | Backend image |

To update the backend image, run:
```sh
docker compose pull
```

### Compose Files

The Compose files include the backend and all necessary services.

- `spring.yaml` contains required backend services as well as useful development tools.
- `compose.yaml` includes `spring.yaml` and adds the backend container.

---

## License

LeLviv backend is licensed under the **[MIT License](LICENSE)**.
