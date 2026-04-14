# Development Guidelines & Code Patterns

## Code Quality Standards

### Naming Conventions
- **Classes**: PascalCase (e.g., `VideoGenerationService`, `AppController`)
- **Methods**: camelCase (e.g., `generateScripts()`, `processReupWithOptions()`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `FFMPEG_PATH`, `OUTPUT_DIR`, `TOTAL_TIMEOUT_SECONDS`)
- **Variables**: camelCase (e.g., `videoIndex`, `reupOpts`, `finalPrompts`)
- **Package names**: lowercase with dots (e.g., `com.ninhquachhai.tiktoktool.service`)

### Code Organization
- **Single Responsibility**: Each service handles one domain (VideoGeneration, VideoRender, LumaVideo, PromptGenerator)
- **Logical Grouping**: Related methods grouped with comment headers using box drawing characters
- **Method Ordering**: Public API methods first, then private implementation methods, helpers at end
- **Comment Headers**: Use box drawing characters for section separation:
  ```java
  // ─────────────────────────────────────────────
  //  SECTION NAME
  // ─────────────────────────────────────────────
  ```

### Documentation Standards
- **Minimal Comments**: Code is self-documenting through clear naming
- **Section Headers**: Use for logical grouping of related functionality
- **Vietnamese Comments**: Used for domain-specific explanations (e.g., "Phụ đề mặc định", "Lật gương")
- **No Javadoc**: Prefer clear method names over verbose documentation

### Formatting & Style
- **Indentation**: 4 spaces (not tabs)
- **Line Length**: Reasonable length, break long lines for readability
- **Braces**: Opening brace on same line (Java convention)
- **Imports**: Organized, no wildcard imports
- **Blank Lines**: Used to separate logical sections within methods

## Structural Conventions

### Service Layer Patterns

#### 1. Dependency Injection
- Use constructor injection for all dependencies
- Mark services with `@Service` annotation
- Example:
  ```java
  @Service
  public class VideoGenerationService {
      private final VideoRenderService videoRenderService;
      private final LumaVideoService lumaVideoService;
      
      public VideoGenerationService(VideoRenderService videoRenderService, 
                                    LumaVideoService lumaVideoService) {
          this.videoRenderService = videoRenderService;
          this.lumaVideoService = lumaVideoService;
      }
  }
  ```

#### 2. Configuration Management
- Store paths and constants as static final fields at class level
- Use `application.properties` for environment-specific values
- Example:
  ```java
  private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";
  private static final String OUTPUT_DIR = "src/main/resources/static/videos/";
  private static final String FONT_PATH = "C\\:/Windows/Fonts/arial.ttf";
  ```

#### 3. Resource Management
- Use try-with-resources for file operations
- Implement `@PreDestroy` for cleanup (e.g., ExecutorService shutdown)
- Example:
  ```java
  @PreDestroy
  public void cleanup() {
      videoRenderExecutor.shutdown();
      try {
          if (!videoRenderExecutor.awaitTermination(60, TimeUnit.SECONDS))
              videoRenderExecutor.shutdownNow();
      } catch (InterruptedException e) {
          videoRenderExecutor.shutdownNow();
      }
  }
  ```

### Controller Layer Patterns

#### 1. Request Handling
- Use `@GetMapping` and `@PostMapping` for HTTP methods
- Accept `MultipartFile` for file uploads
- Use `@RequestParam` for query/form parameters with sensible defaults
- Example:
  ```java
  @PostMapping("/generate")
  public String generate(@RequestParam(value = "image", required = false) MultipartFile image,
                         @RequestParam(value = "speed", required = false, defaultValue = "1.1") double speed,
                         Model model) {
      // Implementation
  }
  ```

#### 2. Error Handling
- Use try-catch blocks with finally for resource cleanup
- Log errors with descriptive messages
- Return user-friendly error messages via Model
- Implement global `@ExceptionHandler` for uncaught exceptions
- Example:
  ```java
  @ExceptionHandler(Exception.class)
  public String handleError(Exception e, Model model) {
      System.err.println("❌ Global error handler: " + e.getMessage());
      model.addAttribute("error", "Lỗi hệ thống: " + e.getMessage());
      return "index_simple";
  }
  ```

#### 3. Model Attributes
- Pass DTOs and results via `Model.addAttribute()`
- Always provide default values for form objects
- Example:
  ```java
  ProductRequest defaultRequest = new ProductRequest("", "", 3, List.of());
  model.addAttribute("productRequest", defaultRequest);
  ```

### Model Layer (DTOs)

#### 1. Record Usage
- Use Java records for immutable data transfer objects
- Example:
  ```java
  public record ReupOptions(
      boolean isMirror,
      boolean isCrop,
      double speed,
      boolean isSubtitle,
      String subtitle1,
      String subtitle2,
      String subtitle3,
      boolean isTTS,
      String voiceType,
      boolean isRemoveOldAudio
  ) {
      public static ReupOptions defaults() {
          return new ReupOptions(false, true, 1.1, false, "", "", "", false, "female", false);
      }
  }
  ```

#### 2. Getter/Setter Pattern
- Use Lombok `@Data` or manual getters/setters for mutable DTOs
- Provide sensible defaults in constructors
- Example:
  ```java
  public class ProductRequest {
      private String productName;
      private String description;
      private int videoCount;
      private List<String> videoPrompts;
      
      public ProductRequest(String productName, String description, 
                           int videoCount, List<String> videoPrompts) {
          this.productName = productName;
          this.description = description;
          this.videoCount = videoCount;
          this.videoPrompts = videoPrompts;
      }
  }
  ```

## Semantic Patterns & Implementation Approaches

### 1. Parallel Processing Pattern
**Frequency**: Used in VideoGenerationService for concurrent video generation
**Implementation**:
- Use `ExecutorService` with fixed thread pool
- Use `CompletableFuture` for async task management
- Apply timeout protection with `orTimeout()`
- Collect results with `allOf()` and `join()`

**Code Example**:
```java
ExecutorService executor = Executors.newFixedThreadPool(3);
List<CompletableFuture<VideoScript>> futures = new ArrayList<>();

for (int i = 0; i < 3; i++) {
    CompletableFuture<VideoScript> future = CompletableFuture
        .supplyAsync(() -> produceVideo(...), executor)
        .orTimeout(TOTAL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .exceptionally(ex -> createEmergencyFallback(...));
    futures.add(future);
}

CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
```

### 2. Fallback/Graceful Degradation Pattern
**Frequency**: Used throughout for error recovery
**Implementation**:
- Primary method with timeout
- Secondary fallback method
- Tertiary emergency fallback
- Each level logs status and errors

**Code Example**:
```java
try {
    // Primary: Luma AI (3-minute timeout)
    finalVideoPath = lumaVideoService.submitVideoJob(...);
} catch (Exception e) {
    try {
        // Secondary: FFmpeg-based rendering
        finalVideoPath = videoRenderService.processReupWithOptions(...);
    } catch (Exception fe) {
        // Tertiary: Static video with effects
        finalVideoPath = videoRenderService.createStaticVideo(...);
    }
}
```

### 3. Strategy Pattern
**Frequency**: Used in VideoRenderService for different video variants
**Implementation**:
- Define variant-specific rendering methods
- Use switch statement to dispatch to correct strategy
- Each strategy applies different effects and parameters

**Code Example**:
```java
private String renderVariant(Path src, VideoScenario sc, int variant, ReupOptions opts) throws Exception {
    return switch (variant) {
        case 1  -> renderV1_MirrorZoom(src, sc, opts);
        case 2  -> renderV2_RotateColor(src, sc, opts);
        default -> renderV3_ContrastNoise(src, sc, opts);
    };
}
```

### 4. Builder Pattern
**Frequency**: Used for complex FFmpeg command construction
**Implementation**:
- Build command as `List<String>` incrementally
- Add flags and parameters conditionally
- Join into single command for execution

**Code Example**:
```java
List<String> cmd = new ArrayList<>();
cmd.add(FFMPEG_PATH);
cmd.add("-i"); cmd.add(src.toAbsolutePath().toString());

if (music != null && !opts.isRemoveOldAudio()) {
    cmd.add("-i"); cmd.add(music);
    cmd.add("-filter_complex"); cmd.add(filterComplex);
} else {
    cmd.add("-vf"); cmd.add(videoFilter);
}

cmd.add("-c:v"); cmd.add("libx264");
// ... more parameters
```

### 5. Template Method Pattern
**Frequency**: Used in video production workflow
**Implementation**:
- Define overall workflow structure
- Allow customization at specific points
- Reuse across different video types

**Code Example**:
```java
private VideoScript produceVideo(Path imagePath, VideoScenario scenario, 
                                 int videoIndex, AtomicInteger completedCount,
                                 VideoRenderService.ReupOptions reupOpts) {
    updateStatus(scenario.name, "Đang khởi động...");
    try {
        // Customize variant-specific options
        VideoRenderService.ReupOptions variantOpts = new VideoRenderService.ReupOptions(...);
        
        // Execute common workflow
        finalVideoPath = videoRenderService.processReupWithOptions(imagePath, videoIndex, scenario, variantOpts);
        method = "V" + videoIndex + "-Reup";
    } catch (Exception e) {
        // Fallback handling
    }
    updateStatus(scenario.name, "Hoàn tất!");
    return new VideoScript(...);
}
```

## Frequently Used Annotations

### Spring Framework
- `@SpringBootApplication`: Main application class
- `@Service`: Service layer components
- `@Controller`: Web request handlers
- `@Configuration`: Configuration classes
- `@GetMapping`, `@PostMapping`: HTTP method mappings
- `@RequestParam`: Extract query/form parameters
- `@ModelAttribute`: Bind form data to objects
- `@ResponseBody`: Return JSON responses
- `@ExceptionHandler`: Global error handling
- `@PreDestroy`: Cleanup on bean destruction

### Lombok (Optional)
- `@Data`: Generate getters, setters, equals, hashCode, toString
- `@Slf4j`: Inject SLF4J logger

## Common Code Idioms

### 1. String Formatting
- Use `String.format(Locale.US, pattern, args)` for locale-independent formatting
- Example: `fmt("%.5f*PTS", 1.0 / speed)`

### 2. Path Handling
- Use `java.nio.file.Path` and `Paths.get()` for file operations
- Use `Files` utility class for file operations
- Example:
  ```java
  Path out = Paths.get(OUTPUT_DIR + fileName);
  Files.createDirectories(out.getParent());
  Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
  ```

### 3. List Operations
- Use stream API for filtering and mapping
- Example:
  ```java
  List<String> prompts = List.of(prompt1, prompt2, prompt3)
      .stream()
      .filter(p -> !p.isBlank())
      .toList();
  ```

### 4. Exception Handling
- Log errors with descriptive context
- Wrap checked exceptions in RuntimeException when appropriate
- Example:
  ```java
  try {
      // Operation
  } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Operation interrupted", e);
  }
  ```

### 5. Random Value Generation
- Use `Random` instance for deterministic seeding
- Example:
  ```java
  private static final Random RNG = new Random();
  String fps = FPS_POOL[RNG.nextInt(FPS_POOL.length)];
  ```

### 6. Null Safety
- Use `isBlank()` for string null/empty checks
- Use ternary operators for simple null coalescing
- Example:
  ```java
  String s = subtitle != null ? subtitle.trim() : "";
  if (!s.isBlank()) { /* process */ }
  ```

## Performance Considerations

### 1. Async Processing
- Use `CompletableFuture` for non-blocking operations
- Implement timeout protection on all async operations
- Example: `.orTimeout(TOTAL_TIMEOUT_SECONDS, TimeUnit.SECONDS)`

### 2. Resource Management
- Close streams and connections properly
- Use try-with-resources for automatic cleanup
- Implement `@PreDestroy` for ExecutorService shutdown

### 3. Caching & Reuse
- Reuse `ObjectMapper` and `RestTemplate` instances
- Cache static resources (fonts, music files)
- Example:
  ```java
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();
  ```

### 4. Logging
- Use `java.util.logging.Logger` for logging
- Log at appropriate levels (INFO for progress, WARNING for issues, SEVERE for errors)
- Include context in log messages
- Example: `LOG.info("✅ [" + sc.name + "][" + label + "] → /videos/" + fileName);`

## Testing Patterns

### Unit Testing
- Use `@SpringBootTest` for integration tests
- Minimal test setup (basic context load test)
- Example:
  ```java
  @SpringBootTest
  class TikTokAffiliateToolApplicationTests {
      @Test
      void contextLoads() {
      }
  }
  ```

## Best Practices Summary

1. **Clarity Over Cleverness**: Write readable code that's easy to maintain
2. **Fail Fast**: Validate inputs early and throw descriptive exceptions
3. **Defensive Programming**: Handle edge cases and null values
4. **Separation of Concerns**: Keep business logic separate from infrastructure
5. **DRY Principle**: Extract common patterns into reusable methods
6. **Error Context**: Include relevant information in error messages and logs
7. **Resource Cleanup**: Always clean up resources (files, threads, connections)
8. **Timeout Protection**: Apply timeouts to all external API calls and long-running operations
9. **Graceful Degradation**: Provide fallback mechanisms for critical operations
10. **Consistent Naming**: Use consistent naming conventions throughout the codebase
