# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-22-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies (this will be cached if the pom.xml hasn't changed)
COPY pom.xml .

# Download dependencies before adding source code to speed up builds
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY . .

# Build the application and skip the tests to save time
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:22-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that your notification service will use (adjust if needed)
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "app.jar"]
