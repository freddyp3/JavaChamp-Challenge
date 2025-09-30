package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import model.Leaderboard;

public class JsonLeaderboardTest {
    protected void checkLeaderboard(int easy, int medium, int hard, Leaderboard leaderboard) {
        assertEquals(easy, leaderboard.getHighScore("EASY"));
        assertEquals(medium, leaderboard.getHighScore("MEDIUM"));
        assertEquals(hard, leaderboard.getHighScore("HARD"));
    }
}
