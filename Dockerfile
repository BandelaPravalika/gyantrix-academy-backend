# Use Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper & project files
COPY . .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Build the project (SKIPS TESTS)
RUN ./mvnw clean package -DskipTests

# Copy the built jar to app.jar
# (Render builds inside a temporary directory, so target folder exists now!)
RUN cp target/*.jar app.jar

# Expose dynamic port (Render sets PORT automatically)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
