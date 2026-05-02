package ie.stephen.pubquiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.stephen.pubquiz.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.StringJoiner;

@Service
public class TriviaFetchService {
    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TriviaFetchService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public int fetchAndStore(int amount, String difficulty) {
        try {
            String url = "https://opentdb.com/api.php?amount=" + amount
                    + "&type=multiple"
                    + (difficulty != null && !difficulty.isBlank() ? "&difficulty=" + difficulty : "");

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            int responseCode = root.get("response_code").asInt();
            if (responseCode != 0) {
                System.out.println("Open Trivia DB returned code: " + responseCode);
                return 0;
            }

            JsonNode results = root.get("results");
            int saved = 0;

            for (JsonNode result : results) {
                String category = result.get("category").asText()
                        .replace("&amp;", "&")
                        .replace("&quot;", "\"")
                        .replace("&#039;", "'");
                String diff = result.get("difficulty").asText();
                String question = result.get("question").asText();
                String correctAnswer = result.get("correct_answer").asText();

                StringJoiner joiner = new StringJoiner("||");
                for (JsonNode wrong : result.get("incorrect_answers")) {
                    joiner.add(wrong.asText());
                }

                String externalId = String.valueOf(question.hashCode());

                questionRepository.save(externalId, category, diff, question, correctAnswer, joiner.toString());
                saved++;
            }

            return saved;

        } catch (Exception e) {
            System.err.println("Failed to fetch from Open Trivia DB: " + e.getMessage());
            return 0;
        }
    }
}
