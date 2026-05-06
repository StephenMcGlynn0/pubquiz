package ie.stephen.pubquiz.controller;

import ie.stephen.pubquiz.model.Question;
import ie.stephen.pubquiz.service.QuizService;
import ie.stephen.pubquiz.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final QuizService quizService;
    private final ValidationService validationService;

    public AdminController(QuizService quizService, ValidationService validationService) {
        this.quizService = quizService;
        this.validationService = validationService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(quizService.getAllQuestions());
    }

    @PostMapping("/questions")
    public ResponseEntity<Map<String, String>> addQuestion(@RequestBody Map<String, Object> body) {
        String category = (String) body.get("category");
        String difficulty = (String) body.get("difficulty");
        String question = (String) body.get("question");
        String correctAnswer = (String) body.get("correctAnswer");
        List<String> incorrectAnswers = (List<String>) body.get("incorrectAnswers");

        if (category == null || difficulty == null || question == null ||
                correctAnswer == null || incorrectAnswers == null || incorrectAnswers.size() != 3) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields required, must have exactly 3 incorrect answers"));
        }

        ValidationService.ValidationResult validation = validationService.validate(question, correctAnswer, incorrectAnswers);
        if (!validation.valid()) {
            return ResponseEntity.badRequest().body(Map.of("error", "AI validation failed: " + validation.reason()));
        }

        quizService.addQuestion(category, difficulty, question, correctAnswer, incorrectAnswers);
        return ResponseEntity.ok(Map.of("message", "Question added!"));
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<Map<String, String>> updateQuestion(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        String category = (String) body.get("category");
        String difficulty = (String) body.get("difficulty");
        String question = (String) body.get("question");
        String correctAnswer = (String) body.get("correctAnswer");
        List<String> incorrectAnswers = (List<String>) body.get("incorrectAnswers");

        if (category == null || difficulty == null || question == null ||
                correctAnswer == null || incorrectAnswers == null || incorrectAnswers.size() != 3) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields required, must have exactly 3 incorrect answers"));
        }

        quizService.updateQuestion(id, category, difficulty, question, correctAnswer, incorrectAnswers);
        return ResponseEntity.ok(Map.of("message", "Question updated!"));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable Long id) {
        quizService.deleteQuestion(id);
        return ResponseEntity.ok(Map.of("message", "Question deleted!"));
    }
}