package ie.stephen.pubquiz.service;

import ie.stephen.pubquiz.model.CategoryInfo;
import ie.stephen.pubquiz.model.Question;
import ie.stephen.pubquiz.model.Score;
import ie.stephen.pubquiz.repository.QuestionRepository;
import ie.stephen.pubquiz.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuestionRepository questionRepository;
    private final ScoreRepository scoreRepository;
    private final TriviaFetchService triviaFetchService;

    public QuizService(QuestionRepository questionRepository,
                       ScoreRepository scoreRepository,
                       TriviaFetchService triviaFetchService) {
        this.questionRepository = questionRepository;
        this.scoreRepository = scoreRepository;
        this.triviaFetchService = triviaFetchService;
    }

    public List<Question> startQuiz(List<String> categories, String difficulty, int amount) {
        List<Question> questions = questionRepository.findByFilters(categories, difficulty, amount);

        if (questions.size() < amount) {
            triviaFetchService.fetchAndStore(50, difficulty);
            questions = questionRepository.findByFilters(categories, difficulty, amount);
        }

        return questions;
    }

    public void submitScore(String playerName, int score, int total, String category, String difficulty) {
        scoreRepository.save(playerName, score, total, category, difficulty);
    }

    public List<Score> getLeaderboard() {
        return scoreRepository.getLeaderboard(20);
    }

    public List<CategoryInfo> getCategories() {
        List<CategoryInfo> categories = questionRepository.findDistinctCategories();
        if (categories.isEmpty()) {
            triviaFetchService.fetchAndStore(50, null);
            categories = questionRepository.findDistinctCategories();
        }
        return categories;
    }
}