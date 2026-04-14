package com.ninhquachhai.tiktoktool.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class PromptGeneratorService {

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL    = "llama-3.3-70b-versatile";

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper  = new ObjectMapper();

    /**
     * Sinh 3 prompt tiếng Anh cho Luma AI dựa trên tên + mô tả sản phẩm.
     * Trả về mảng đúng 3 phần tử: [reviewPrompt, lifestylePrompt, trendyPrompt]
     */
    public List<String> generatePrompts(String productName, String description) {
        // Thử Groq trước
        try {
            return generateWithGroq(productName, description);
        } catch (Exception e) {
            System.err.println("[PromptGenerator] Groq lỗi: " + e.getMessage() + " — dùng template mặc định");
        }
        // Fallback template
        return buildTemplatePrompts(productName, description);
    }

    private List<String> generateWithGroq(String productName, String description) throws Exception {
        String desc = (description != null && !description.isBlank()) ? description : "no description";
        String systemMsg = """
            You are a creative director for TikTok product videos.
            Generate exactly 3 short English video prompts (1-2 sentences each) for Luma AI Dream Machine.
            Each prompt must describe ONLY the visual scene, camera movement, and lighting — no text overlay.
            Return ONLY a valid JSON array with exactly 3 strings. No markdown, no explanation.
            """;
        String userMsg = """
            Product: %s
            Description: %s

            Generate prompts for these 3 styles:
            1. REVIEW style: Close-up of product material/texture, smooth slow pan left to right, luxury studio lighting, shallow depth of field.
            2. LIFESTYLE style: Product in a real-life setting (park bench or cafe table), natural golden-hour sunlight, relaxed atmosphere, medium shot.
            3. TRENDY style: Neon-lit environment, slight camera shake, fast dynamic movement, vibrant saturated colors, energetic feel.

            Return JSON array: ["prompt1","prompt2","prompt3"]
            """.formatted(productName, desc);

        Map<String, Object> body = Map.of(
            "model",    MODEL,
            "messages", List.of(
                Map.of("role", "system", "content", systemMsg),
                Map.of("role", "user",   "content", userMsg)
            ),
            "max_tokens",   400,
            "temperature",  0.8
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + groqApiKey);

        String raw = restTemplate.postForObject(
            GROQ_URL, new HttpEntity<>(objectMapper.writeValueAsString(body), headers), String.class);

        String content = objectMapper.readTree(raw)
            .path("choices").get(0).path("message").path("content").asText()
            .replaceAll("(?s)```json\\s*", "").replaceAll("```", "").trim();

        JsonNode arr = objectMapper.readTree(content);
        if (!arr.isArray() || arr.size() < 3)
            throw new RuntimeException("Groq trả về ít hơn 3 prompts");

        return List.of(arr.get(0).asText(), arr.get(1).asText(), arr.get(2).asText());
    }

    /** Template cứng khi Groq không khả dụng */
    private List<String> buildTemplatePrompts(String productName, String description) {
        String name = productName != null ? productName : "product";
        return List.of(
            // Video 1 - Review
            "Extreme close-up of " + name + " surface texture, slow cinematic pan left to right, "
            + "soft studio lighting with bokeh background, luxury product feel.",

            // Video 2 - Lifestyle
            name + " placed on a wooden cafe table near a window, warm golden-hour sunlight streaming in, "
            + "shallow depth of field, relaxed lifestyle atmosphere, medium shot.",

            // Video 3 - Trendy
            name + " in a neon-lit urban environment, slight handheld camera shake, "
            + "fast dynamic zoom-in, vibrant purple and cyan neon reflections, energetic commercial style."
        );
    }
}
