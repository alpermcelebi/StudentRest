# Stage 1: Build the application
# Use an OpenJDK image with Maven to build the application
FROM maven:3.9.8-eclipse-temurin-22-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .



# Download dependencies (this will be cached if the pom.xml hasn't changed)
RUN mvn dependency:go-offline

# dependecies are in the pom.xml, by using below line we save time since
# we do not need to download dependecies every time unless pom.xml changes
COPY . .

# Copy the source code into the container
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# Use a smaller image with JDK 22 to run the application
FROM eclipse-temurin:22-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application's port (adjust this as needed)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
