# ==========================================
# Stage 1: Build Stage
# ==========================================
FROM gradle:8.7-jdk21 AS builder

WORKDIR /app

# Copy gradle files first to leverage Docker layer caching for dependencies
COPY build.gradle.kts settings.gradle.kts gradle/ ./
COPY gradlew ./

# Download dependencies (cached unless build files change)
RUN gradle dependencies --no-daemon || true

# Copy source code and build the executable jar
COPY src ./src
RUN gradle bootJar --no-daemon

# ==========================================
# Stage 2: Runtime Stage
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root user for security best practices
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

# Copy only the compiled jar from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port 8080 for Spring Boot
EXPOSE 8080

# Environment variables
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Launch the microservice
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]