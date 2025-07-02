# Build stage
FROM eclipse-temurin:17-jdk-jammy as builder

# 1. Create and set working directory
WORKDIR /app

# 2. First copy only the files needed for Maven to work
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# 3. Make mvnw executable
RUN chmod +x mvnw

# 4. Download dependencies (this layer gets cached)
RUN ./mvnw dependency:go-offline

# 5. Copy source code
COPY src/ src/

# 6. Build the application
RUN ./mvnw clean package

# Runtime stage
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# 7. Copy the built JAR from builder stage
COPY --from=builder /app/target/vestralok-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
