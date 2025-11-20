# Use supported Java 17 image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the Maven/Gradle build artifact (JAR)
COPY target/*.jar app.jar

# Expose port (Render assigns dynamically)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
