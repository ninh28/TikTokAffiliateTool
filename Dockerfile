# 🚀 TikTok Affiliate Tool - Production Dockerfile
FROM eclipse-temurin:17-jdk

# Install FFmpeg
RUN apt-get update && apt-get install -y \
    ffmpeg \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy source code
COPY src src

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