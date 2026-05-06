package ie.stephen.pubquiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ValidationService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ValidationResult validate(String question, String correctAnswer, List<String> incorrectAnswers) {
        try {
            String prompt = String.format("""
                You are a quiz fact-checker. Evaluate this quiz question:
                
                Question: %s
                Correct Answer: %s
                Incorrect Answers: %s
                
                Check:
                1. Is the question clear and unambiguous?
                2. Is the correct answer definitely correct?
                3. Are the incorrect answers definitely wrong?
                
                Respond with ONLY a JSON object, no markdown, no backticks, no explanation:
                {"valid": true, "reason": "Brief explanation"}
                or
                {"valid": false, "reason": "Brief explanation of the problem"}
                """,
                    question, correctAnswer, String.join(", ", incorrectAnswers)
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");

            Map<String, Object> body = Map.of(
                    "model", "claude-haiku-4-5-20251001",
                    "max_tokens", 256,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    )
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.anthropic.com/v1/messages", request, String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.path("content").get(0).path("text").asText();

            text = text.replaceAll("```json", "").replaceAll("```", "").trim();

            JsonNode result = objectMapper.readTree(text);
            boolean valid = result.get("valid").asBoolean();
            String reason = result.get("reason").asText();

            return new ValidationResult(valid, reason);

        } catch (Exception e) {
            System.err.println("Validation failed: " + e.getMessage());
            return new ValidationResult(true, "Validation unavailable");
        }
    }

    public record ValidationResult(boolean valid, String reason) {}
}