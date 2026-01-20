# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only what we need for dependency caching
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Now copy source
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Create a non-root user (good container practice)
RUN useradd -r -u 1001 appuser
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# JVM flags are kept modest; adjust later if needed
ENTRYPOINT ["java","-jar","/app/app.jar"]
