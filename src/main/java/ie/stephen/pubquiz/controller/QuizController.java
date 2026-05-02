package ie.stephen.pubquiz.controller;

import ie.stephen.pubquiz.model.Question;
import ie.stephen.pubquiz.model.Score;
import ie.stephen.pubquiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/quiz/start")
    public ResponseEntity<List<Question>> startQuiz(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(defaultValue = "10") int amount) {

        if (amount < 1 || amount > 20) {
            return ResponseEntity.badRequest().build();
        }

        List<Question> questions = quizService.startQuiz(category, difficulty, amount);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/quiz/score")
    public ResponseEntity<Map<String, String>> submitScore(@RequestBody Map<String, Object> body) {
        String playerName = (String) body.get("playerName");
        int score = (int) body.get("score");
        int total = (int) body.get("total");
        String category = (String) body.getOrDefault("category", "Any");
        String difficulty = (String) body.getOrDefault("difficulty", "Any");

        if (playerName == null || playerName.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Player name required"));
        }

        quizService.submitScore(playerName.trim(), score, total, category, difficulty);
        return ResponseEntity.ok(Map.of("message", "Score saved!"));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboard() {
        return ResponseEntity.ok(quizService.getLeaderboard());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(quizService.getCategories());
    }
}