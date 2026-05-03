package ie.stephen.pubquiz.repository;

import ie.stephen.pubquiz.model.CategoryInfo;
import ie.stephen.pubquiz.model.Question;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class QuestionRepository {
    private final JdbcTemplate jdbc;

    public QuestionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(String externalId, String category, String difficulty,
                     String question, String correctAnswer, String incorrectAnswers) {
        jdbc.update("""
            INSERT OR IGNORE INTO questions
            (external_id, category, difficulty, question, correct_answer, incorrect_answers)
            VALUES (?, ?, ?, ?, ?, ?)
        """, externalId, category, difficulty, question, correctAnswer, incorrectAnswers);
    }

    public List<Question> findByFilters(List<String> categories, String difficulty, int limit) {
        StringBuilder sql = new StringBuilder(
                "SELECT id, category, difficulty, question, correct_answer, incorrect_answers FROM questions WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (categories != null && !categories.isEmpty()) {
            String placeholders = String.join(",", Collections.nCopies(categories.size(), "?"));
            sql.append(" AND category IN (").append(placeholders).append(")");
            params.addAll(categories);
        }
        if (difficulty != null && !difficulty.isBlank()) {
            sql.append(" AND difficulty = ?");
            params.add(difficulty);
        }

        sql.append(" ORDER BY RANDOM() LIMIT ?");
        params.add(limit);

        return jdbc.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            Question q = new Question();
            q.setId(rs.getLong("id"));
            q.setCategory(rs.getString("category"));
            q.setDifficulty(rs.getString("difficulty"));
            q.setQuestion(rs.getString("question"));
            q.setCorrectAnswer(rs.getString("correct_answer"));

            List<String> answers = new ArrayList<>();
            answers.add(rs.getString("correct_answer"));
            answers.addAll(Arrays.asList(rs.getString("incorrect_answers").split("\\|\\|")));
            Collections.shuffle(answers);
            q.setAllAnswers(answers);

            return q;
        });
    }

    public List<CategoryInfo> findDistinctCategories() {
        return jdbc.query(
                "SELECT category, COUNT(*) as count FROM questions GROUP BY category HAVING COUNT(*) >= 5 ORDER BY category",
                (rs, rowNum) -> new CategoryInfo(rs.getString("category"), rs.getInt("count"))
        );
    }

    public int count() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM questions", Integer.class);
        return count != null ? count : 0;
    }

}
