# 🚀 TikTok Affiliate Tool - Production Dockerfile
FROM eclipse-temurin:17-jdk

# Install FFmpeg and other necessary tools
RUN apt-get update && apt-get install -y \
    ffmpeg \
    wget \
    fontconfig \
    fonts-dejavu \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Create directories for static content and temporary uploads outside the JAR
# These directories will be used by Spring Boot to serve static files and store generated videos
RUN mkdir -p /app/static/videos /app/static/music /app/temp_uploads

# Copy Maven files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy source code
COPY src src

# Copy music files to the external static directory
COPY src/main/resources/static/music/ /app/static/music/

# Make mvnw executable
RUN chmod +x mvnw

# Build application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8080

# Run application
CMD ["java", "-jar", "target/tiktok-tool-0.0.1-SNAPSHOT.jar"]