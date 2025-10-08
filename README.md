# IroCO2 Backend

This project is part of [IroCO2](https://github.com/ippontech/iroco2). You are not in the principal repository. For any general information about the project, please refer to the [principal repository](https://github.com/ippontech/iroco2).

This is the backend of the IroCO2 application. Developed using Java 21 and Spring Boot 3.2.4.

---

## 🚀 Getting Started

### Prerequisites

- Java 21 (tested with Eclipse Temurin 21)
- Maven 3.8+
- Docker & Docker Compose (for local development and database)
- PostgreSQL (local or via Docker)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/ippontech/iroco2-backend.git
   cd iroco2-backend
   ```
2. **Build the project:**
   ```bash
   mvn clean install
   ```

### Run in development

1. Create a `infrastructure/src/main/resources/application-local.properties` (ask an IroCO2 referent on how to document it)

2. Start the database and localstack :
   - With Docker Compose (recommended)
   ```bash
   docker compose -f docker/local.docker-compose.yml up -d
   ```
   - Without Docker 
   
   Make sure a PostgreSQL and a Localstack instance are accessible (see `docker/local.docker-compose.yml` for env vars).

3. On IntelliJ, create a configuration to launch the app with local profile :
    - in `infrastructure/src/main/java/MainApp.main`, right-click on the run button associated to`main`function
    - edit a configuration where you simply add `spring.profiles.active=local` as an environment variable
4. Run `MainApp.main` with the configuration you just created
---

## 📦 Project Structure

- `domain/` : Domain model and business rules
- `infrastructure/` : Infrastructure layer with Main Spring Boot application (external services, adapters)
- `docker/` : Dockerfile, local development and CI tools (compose, scripts)
- `docs/` : Backend documentation
- `tf/` : Terraform infrastructure as code

---

## 🧪 Testing

- Unit and integration tests via Maven:
  ```bash
  mvn test
  ```
- Cucumber BDD tests included
- Testcontainers used for integration tests (PostgreSQL, Localstack) so a Docker Daemon is needed

## 🛡️ Security & Auth

- Spring Security + JWT (see configuration in `infrastructure` module)
- OAuth2 test support via `spring-addons-oauth2-test`

---

## 🧹 Code Formatting with Prettier (Java)

We use [Prettier Java](https://github.com/jhipster/prettier-java) to ensure consistent code formatting for Java files.

### Setup (one-time)

1. Install Node modules:
   ```bash
   npm install
   npm ci
   ```
2. (Recommended) Install the File Watchers plugin in your IDE and configure it as follows:
   - **Name:** Prettier Java
   - **File Type:** Java
   - **Program:** npx
   - **Arguments:** prettier --write $FilePath$
   - **Output path to refresh:** $FilePath$
   - **Trigger the watcher on external changes:** checked
   - **Autosave edited files to trigger the watcher:** checked

### Format all Java files

```bash
npx prettier --write "**/*.java"
```

---

## 🧭 Local SonarQube Usage

We use SonarQube for static code analysis and code quality.

### Start SonarQube locally

```bash
docker compose -f docker/sonar.docker-compose.yml up
```

### Configuration
- Access SonarQube at [http://localhost:9001](http://localhost:9001)
- Login: `admin` / `admin` (change the password after first login)
- Create a new project named `iroco-backend` with default branch `develop`
- Choose "locally" analysis mode and generate a new token
- Export the token as an environment variable:
  ```bash
  export LOCAL_SONAR_TOKEN=your_token_here
  ```
- Run analysis:
  ```bash
  mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent verify sonar:sonar \
    -Dsonar.projectKey=iroco-backend \
    -Dsonar.host.url=http://localhost:9001 \
    -Dsonar.login=$LOCAL_SONAR_TOKEN
  ```


## 🚀 Deploy

After building the Java application, you will need to build and push the docker image to your repository. Use the Dockerfilek in the `docker/` directory.

Then you can deploy the application using Terraform. 

*WIP: More docs to come.*

## 📝 License

Distributed under the Apache 2.0 License. See [LICENSE](./LICENSE) for more information.

---

## 📄 Documentation
See [docs/](./docs) for technical documentation.

For ADRs and workflow documentation, see the [principal repository](https://github.com/ippontech/iroco2/blob/main/contribute/adr/ and https://github.com/ippontech/iroco2/tree/main/contribute/workflows).

## 🤝 Contributing
See [CONTRIBUTING.md](./CONTRIBUTING.md)