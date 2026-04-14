# Project Structure & Architecture

## Directory Organization

```
TikTokAffiliateTool/
├── src/
│   ├── main/
│   │   ├── java/com/ninhquachhai/tiktoktool/
│   │   │   ├── TikTokAffiliateToolApplication.java    # Spring Boot entry point
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java                     # Web configuration
│   │   │   ├── controller/
│   │   │   │   └── AppController.java                 # HTTP request handlers
│   │   │   ├── model/
│   │   │   │   ├── ProductRequest.java                # Input DTO
│   │   │   │   ├── ProductResponse.java               # Output DTO
│   │   │   │   ├── VideoScript.java                   # Video metadata
│   │   │   │   ├── VideoScenario.java                 # Video scenario definition
│   │   │   │   └── AiVideoJob.java                    # Luma AI job tracking
│   │   │   └── service/
│   │   │       ├── VideoGenerationService.java        # Main orchestration service
│   │   │       ├── VideoRenderService.java            # FFmpeg video processing
│   │   │       ├── LumaVideoService.java              # Luma AI integration
│   │   │       └── PromptGeneratorService.java        # Prompt generation
│   │   └── resources/
│   │       ├── application.properties                 # Spring configuration
│   │       ├── templates/                             # Thymeleaf HTML templates
│   │       └── static/                                # Static assets (music, fonts)
│   └── test/
│       └── java/com/example/tiktoktool/
│           └── TikTokAffiliateToolApplicationTests.java
├── pom.xml                                            # Maven configuration
├── Dockerfile                                         # Container configuration
└── [Documentation files]
    ├── ZERO_INPUT_GUIDE.md
    ├── RUN_GUIDE.md
    ├── AUTO_FALLBACK_GUIDE.md
    └── [Other guides]
```

## Core Components

### 1. Controller Layer
**AppController.java**
- Handles HTTP requests for video generation
- Manages file uploads and image processing
- Returns video generation status and results
- Provides web UI endpoints

### 2. Service Layer

#### VideoGenerationService
- **Responsibility**: Main orchestration of video generation workflow
- **Key Methods**:
  - `generateScripts()`: Entry point for zero-input workflow
  - `runHybridWorkflow()`: Parallel execution of 3 video generation tasks
  - `produceVideo()`: Individual video production with fallback handling
  - `createScenariosFromPrompts()`: Converts prompts to video scenarios
  - `getAutoAffiliatePrompts()`: Generates default prompts if none provided
- **Features**:
  - Parallel processing with ExecutorService
  - Timeout handling (4 minutes total)
  - Automatic fallback from Luma AI to FFmpeg
  - Status tracking and progress reporting

#### VideoRenderService
- **Responsibility**: FFmpeg-based video processing and rendering
- **Key Methods**:
  - `processReupWithOptions()`: Main video processing with customizable options
  - `createStaticVideo()`: Fallback static video creation
  - `processLumaVideo()`: Post-processing of Luma AI generated videos
  - `addSubtitles()`: Dynamic text overlay addition
  - `addBackgroundMusic()`: Music integration
- **Features**:
  - Mirror/flip effects
  - Zoom and pan transformations
  - Crop functionality
  - Speed adjustment
  - Subtitle positioning and styling
  - Audio processing (TTS, removal, replacement)

#### LumaVideoService
- **Responsibility**: Integration with Luma AI video generation API
- **Key Methods**:
  - `submitVideoJob()`: Submit video generation request to Luma AI
  - `checkJobStatus()`: Poll job status
  - `isApiKeyConfigured()`: Validate API key availability
- **Features**:
  - API key management
  - Job submission and tracking
  - Status polling with retry logic
  - Error handling for API failures

#### PromptGeneratorService
- **Responsibility**: Generate and optimize video prompts
- **Features**:
  - Groq API integration for prompt generation
  - Prompt optimization for AI video generation
  - Fallback prompt generation

### 3. Model Layer (DTOs)

**ProductRequest**
- Input data structure for video generation
- Fields: productName, videoPrompts, videoCount

**ProductResponse**
- Output data structure containing generated videos
- Fields: productName, videoScripts (list of VideoScript)

**VideoScript**
- Represents a single generated video
- Fields: name, hook, method, cta, videoPath

**VideoScenario**
- Defines video generation parameters
- Fields: name, prompt, hook, cta

**AiVideoJob**
- Tracks Luma AI job status
- Fields: jobId, status, videoUrl, errorMessage

### 4. Configuration Layer

**WebConfig.java**
- Spring Web configuration
- CORS settings
- Resource handling

**application.properties**
- FFmpeg path configuration
- Font path configuration
- Luma AI API key
- Groq API key
- Port configuration

## Architectural Patterns

### 1. Parallel Processing Pattern
- Uses ExecutorService for concurrent video generation
- 3 videos processed simultaneously
- CompletableFuture for async task management
- Timeout protection with orTimeout()

### 2. Fallback/Graceful Degradation Pattern
- Primary: Luma AI video generation (3-minute timeout)
- Secondary: FFmpeg-based video creation
- Tertiary: Static video with effects
- Ensures delivery of output even if primary method fails

### 3. Service Orchestration Pattern
- VideoGenerationService coordinates multiple services
- Manages workflow state and transitions
- Handles error recovery and status reporting

### 4. Strategy Pattern
- Different video rendering strategies (Mirror, Zoom, Rotate)
- Configurable via ReupOptions
- Allows flexible video effect combinations

### 5. Template Method Pattern
- Video production follows consistent workflow
- Customizable at specific points (effects, music, subtitles)
- Reusable across different video types

## Data Flow

```
User Upload (Image)
    ↓
AppController.generateVideos()
    ↓
VideoGenerationService.generateScripts()
    ↓
Create 3 VideoScenarios
    ↓
runHybridWorkflow() - Parallel Execution
    ├─→ Video 1: Expert Review
    │   ├─→ LumaVideoService (3min timeout)
    │   └─→ VideoRenderService (Fallback)
    ├─→ Video 2: Lifestyle Vibe
    │   ├─→ LumaVideoService (3min timeout)
    │   └─→ VideoRenderService (Fallback)
    └─→ Video 3: Trendy Deal
        ├─→ LumaVideoService (3min timeout)
        └─→ VideoRenderService (Fallback)
    ↓
Post-Processing (Music, Subtitles)
    ↓
ProductResponse (3 VideoScripts)
    ↓
Return to User
```

## Technology Stack Integration

- **Spring Boot 4.0.5**: Application framework
- **Java 17**: Programming language
- **Maven**: Build and dependency management
- **FFmpeg**: Video processing engine
- **Luma AI API**: AI video generation
- **Groq API**: Prompt generation
- **Thymeleaf**: Web UI templating
- **Jackson**: JSON serialization

## Key Design Decisions

1. **Parallel Processing**: 3 videos generated simultaneously to minimize total execution time
2. **Timeout-Based Fallback**: Automatic switch to FFmpeg after 3 minutes ensures predictable delivery
3. **Zero-Input Design**: Automatic prompt and scenario generation reduces user friction
4. **Modular Services**: Each service has single responsibility for maintainability
5. **Async/Await Pattern**: Non-blocking operations for better resource utilization
