# Stage 1: Build the application JAR
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Pre-fetch deps
COPY pom.xml .
COPY domain/pom.xml ./domain/
COPY infrastructure/pom.xml ./infrastructure/
RUN mvn -B dependency:go-offline

# Build
COPY . .
RUN mvn -B package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar
COPY --from=build /app/infrastructure/target/*.jar app.jar

# Point to the correct file (relative to WORKDIR)
ENTRYPOINT ["java", "-jar", "app.jar"]
