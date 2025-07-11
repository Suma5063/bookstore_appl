# Use a slim OpenJDK base image
FROM openjdk:17-jdk-slim

# Copy your JAR file into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port your app runs on
EXPOSE 8081


# Start the app
ENTRYPOINT ["java", "-jar", "/app.jar"]
