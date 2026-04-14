# Technology Stack & Development Setup

## Programming Language & Runtime
- **Language**: Java 17
- **Framework**: Spring Boot 4.0.5
- **Build Tool**: Maven 3.x (with Maven Wrapper)
- **JDK**: Java Development Kit 17+

## Core Dependencies

### Spring Boot Starters
- `spring-boot-starter-web`: REST API and web server
- `spring-boot-starter-thymeleaf`: Server-side HTML templating
- `spring-boot-devtools`: Development tools and hot reload

### Data Processing
- `jackson-databind`: JSON serialization/deserialization
- `commons-text`: Text utilities (version 1.12.0)
- `lombok`: Annotation-based code generation (optional)

### Video Processing
- `ffmpeg` (net.bramp): FFmpeg Java wrapper (version 0.8.0)

### Testing
- `spring-boot-starter-test`: JUnit, Mockito, AssertJ

## External Services & APIs

### Luma AI
- **Purpose**: AI-powered video generation from images
- **Integration**: REST API calls via RestTemplate
- **Configuration**: API key in application.properties
- **Timeout**: 3 minutes per video generation request

### Groq API
- **Purpose**: Prompt generation and optimization
- **Integration**: REST API calls
- **Configuration**: API key in application.properties
- **Usage**: Generate video prompts from product descriptions

### FFmpeg
- **Purpose**: Video processing, effects, music integration, subtitle rendering
- **Version**: Latest stable
- **Installation**: Required on system PATH or configured in application.properties
- **Key Operations**:
  - Video transformation (zoom, pan, mirror)
  - Subtitle rendering with drawtext filter
  - Audio mixing and replacement
  - Video concatenation and effects

## Configuration Files

### application.properties
```properties
# Server
server.port=8080

# FFmpeg Configuration
ffmpeg.path=C:/ffmpeg/ffmpeg.exe

# Font Configuration (Windows)
font.path=C:/Windows/Fonts/arial.ttf

# Luma AI Configuration
luma.api.key=<your_luma_api_key>

# Groq API Configuration
groq.api.key=<your_groq_api_key>

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.mode=HTML
```

### pom.xml Structure
- Parent: spring-boot-starter-parent (4.0.5)
- Encoding: UTF-8
- Source/Target: Java 17
- Plugins: spring-boot-maven-plugin, maven-compiler-plugin with Lombok annotation processing

## Development Commands

### Build
```bash
# Clean build
mvnw.cmd clean compile

# Build with tests
mvnw.cmd clean package

# Build without tests
mvnw.cmd clean package -DskipTests
```

### Run
```bash
# Using Maven
mvnw.cmd spring-boot:run

# Using Java directly
java -jar target/tiktok-tool-0.0.1-SNAPSHOT.jar

# Using IDE (IntelliJ IDEA)
Right-click TikTokAffiliateToolApplication.java → Run
```

### Test
```bash
# Run all tests
mvnw.cmd test

# Run specific test class
mvnw.cmd test -Dtest=TikTokAffiliateToolApplicationTests
```

## Project Structure for Development

### Source Code Organization
```
src/main/java/com/ninhquachhai/tiktoktool/
├── TikTokAffiliateToolApplication.java
├── config/
├── controller/
├── model/
└── service/

src/main/resources/
├── application.properties
├── templates/
│   └── [Thymeleaf HTML files]
└── static/
    ├── [CSS, JavaScript]
    └── [Music files: luxury_bgm.mp3, chill_vibes.mp3, fast_dynamic.mp3]

src/test/java/
└── com/example/tiktoktool/
    └── TikTokAffiliateToolApplicationTests.java
```

### Required Static Assets
Place in `src/main/resources/static/`:
- `luxury_bgm.mp3` (12-15 seconds) - Background music for Expert Review video
- `chill_vibes.mp3` (12-15 seconds) - Background music for Lifestyle Vibe video
- `fast_dynamic.mp3` (7-10 seconds) - Background music for Trendy Deal video

### Font Files
- Windows: `C:/Windows/Fonts/arial.ttf` (default)
- Linux: `/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf`
- macOS: `/Library/Fonts/Arial.ttf`

## System Requirements

### Minimum
- Java 17 JDK
- 4GB RAM
- 2GB free disk space
- FFmpeg installed and accessible

### Recommended
- Java 17+ JDK
- 8GB RAM
- 10GB free disk space
- FFmpeg with libx264 codec support
- Luma AI API key (for AI video generation)
- Groq API key (for prompt generation)

## IDE Setup

### IntelliJ IDEA (Recommended)
1. Open project: `File → Open → TikTokAffiliateTool`
2. Wait for Maven to import dependencies
3. Configure JDK: `File → Project Structure → Project → SDK → Java 17`
4. Run: Right-click `TikTokAffiliateToolApplication.java` → Run

### VS Code
1. Install Java Extension Pack
2. Open workspace folder
3. Maven will auto-detect pom.xml
4. Run via Maven Explorer or terminal

### Eclipse
1. Import as Maven project
2. Configure JDK 17
3. Run as Spring Boot App

## Deployment

### Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/tiktok-tool-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build Docker Image
```bash
mvnw.cmd clean package
docker build -t tiktok-affiliate-tool .
docker run -p 8080:8080 tiktok-affiliate-tool
```

## Performance Considerations

### Parallel Processing
- ExecutorService with fixed thread pool for 3 concurrent video generations
- Timeout: 240 seconds (4 minutes) total per workflow
- Individual video timeout: 180 seconds (3 minutes) for Luma AI

### Memory Management
- Video files stored in temp directory
- Cleanup on application shutdown
- Stream-based processing for large files

### Network
- Async HTTP calls to external APIs
- Timeout protection on all API calls
- Retry logic for transient failures

## Troubleshooting

### FFmpeg Issues
- Verify installation: `ffmpeg -version`
- Check PATH configuration in application.properties
- Ensure libx264 codec is available

### API Key Issues
- Verify keys in application.properties
- Check API quotas and rate limits
- Review API documentation for authentication format

### Port Conflicts
- Default port: 8080
- Change in application.properties: `server.port=8081`

### Memory Issues
- Increase JVM heap: `java -Xmx2g -jar app.jar`
- Monitor with: `jps -l` and `jstat`
