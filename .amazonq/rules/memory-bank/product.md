# TikTok Affiliate Tool - Product Overview

## Project Purpose
Automated video generation system for TikTok affiliate marketing. Converts product images into multiple high-quality affiliate videos with AI-powered enhancements, music, and dynamic text overlays.

## Value Proposition
- **Zero-Input Workflow**: Users upload a single product image and the system automatically generates 3 complete affiliate videos
- **Multi-Strategy Approach**: Creates videos with different angles (Expert Review, Lifestyle Vibe, Trendy Deal) to maximize engagement
- **Intelligent Fallback**: Seamlessly transitions from AI video generation (Luma AI) to FFmpeg-based video creation if AI processing times out
- **Complete Post-Production**: Automatically adds background music, dynamic text overlays, and video effects
- **Time-Efficient**: Delivers 3 finished videos within 4 minutes maximum

## Key Features

### Video Generation Strategies
1. **Expert Review Video**
   - Macro close-up focus on product materials
   - Luxury background music
   - Text: "Hàng cao cấp soi kỹ chi tiết" (Premium product detailed inspection)
   - Duration: 12 seconds

2. **Lifestyle Vibe Video**
   - Product in luxurious context/setting
   - Chill ambient music
   - Text: "Style cực xinh cho bạn" (Stylish for you)
   - Duration: 12 seconds

3. **Trendy Deal Video**
   - Fast-paced movement with dynamic lighting
   - Upbeat dynamic music
   - Text: "GIẢM 50% - MUA NGAY" (50% OFF - BUY NOW) with blinking effect
   - Duration: 7 seconds

### AI Integration
- **Luma AI Support**: Optional AI video generation with 3-minute timeout
- **Auto-Fallback Mechanism**: Automatically switches to FFmpeg-based video creation if AI processing exceeds timeout
- **Groq API Integration**: For prompt generation and optimization

### Video Enhancement
- FFmpeg-based video processing with zoom, pan, and mirror effects
- Dynamic text overlay with customizable positioning and styling
- Background music integration with automatic synchronization
- Subtitle support with multiple text layers
- Audio processing: TTS support, audio removal, speed adjustment

### Rendering Options
- Mirror/flip effects
- Crop functionality
- Speed adjustment
- Subtitle customization
- Voice-over (TTS) support
- Audio removal and replacement

## Target Users
- **E-commerce Sellers**: Creating affiliate content for product promotion
- **Content Creators**: Generating multiple video variations for TikTok/social media
- **Marketing Agencies**: Batch processing product images into affiliate content
- **Dropshippers**: Quick video content generation for product listings

## Use Cases
1. **Rapid Content Creation**: Generate 3 unique video angles from a single product image
2. **A/B Testing**: Test different video strategies (expert, lifestyle, trendy) to find best engagement
3. **Batch Processing**: Process multiple product images to build content library
4. **Affiliate Marketing**: Create ready-to-post TikTok content with calls-to-action
5. **Social Media Marketing**: Generate platform-specific video content with optimized durations

## Technical Capabilities
- Parallel video processing (3 videos generated simultaneously)
- Asynchronous task handling with timeout protection
- Graceful degradation with fallback mechanisms
- RESTful API for video generation requests
- Web UI for user-friendly image upload and video preview
- Comprehensive error handling and status reporting
