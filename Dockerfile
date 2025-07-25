# Build stage
FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /app

# Copy pom and source
COPY pom.xml .
COPY src/ src/

# Build the project
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/vestralok-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
