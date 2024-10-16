# Use an official Java 17 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file to the container
COPY ./build/libs/payment-question-0.0.1-SNAPSHOT.jar /app/application.jar

# Expose port 8080 for the application to listen on
EXPOSE 8080

# Start the Java application
CMD ["java", "-jar" ,"/app/application.jar"]