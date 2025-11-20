# Use Java 17
FROM openjdk:17

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests

# Run the jar file
CMD ["java", "-jar", "target/gyantrix-academy-backend-0.0.1-SNAPSHOT.jar"]
