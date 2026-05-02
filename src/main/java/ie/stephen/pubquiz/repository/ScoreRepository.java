package ie.stephen.pubquiz.repository;

import ie.stephen.pubquiz.model.Score;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScoreRepository {
    private final JdbcTemplate jdbc;

    public ScoreRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(String playerName, int score, int total, String category, String difficulty) {
        jdbc.update("""
            INSERT INTO scores (player_name, score, total, category, difficulty)
            VALUES (?, ?, ?, ?, ?)
        """, playerName, score, total, category, difficulty);
    }

    public List<Score> getLeaderboard(int limit) {
        return jdbc.query("""
            SELECT id, player_name, score, total, category, difficulty, played_at
            FROM scores
            ORDER BY score DESC, played_at ASC
            LIMIT ?
        """, (rs, rowNum) -> {
            Score s = new Score();
            s.setId(rs.getLong("id"));
            s.setPlayerName(rs.getString("player_name"));
            s.setScore(rs.getInt("score"));
            s.setTotal(rs.getInt("total"));
            s.setCategory(rs.getString("category"));
            s.setDifficulty(rs.getString("difficulty"));
            return s;
        }, limit);
    }
}
