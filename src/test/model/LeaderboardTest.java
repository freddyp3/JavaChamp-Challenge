package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Tests for the Leaderboard class.
 */
public class LeaderboardTest {
    private Leaderboard leaderboard;

    @BeforeEach
    void runBefore() {
        leaderboard = new Leaderboard();
    }

    @Test
    void testLeaderboardConstructor() {
        assertEquals(0, leaderboard.getHighScore("EASY"));
        assertEquals(0, leaderboard.getHighScore("MEDIUM"));
        assertEquals(0, leaderboard.getHighScore("HARD"));
    }

    @Test
    void testUpdateHighScore() {
        leaderboard.updateHighScore("EASY", 50);
        assertEquals(50, leaderboard.getHighScore("EASY"));
        leaderboard.updateHighScore("MEDIUM", 100);
        assertEquals(100, leaderboard.getHighScore("MEDIUM"));
        leaderboard.updateHighScore("HARD", 200);
        assertEquals(200, leaderboard.getHighScore("HARD"));

        leaderboard.updateHighScore("DEMON MODE", 199);
        assertEquals(200, leaderboard.getHighScore("HARD"));
        assertEquals(100, leaderboard.getHighScore("MEDIUM"));
        assertEquals(50, leaderboard.getHighScore("EASY")); 

        leaderboard.updateHighScore("EASY", 40);
        leaderboard.updateHighScore("MEDIUM", 20);
        leaderboard.updateHighScore("HARD", 1);
        assertEquals(200, leaderboard.getHighScore("HARD"));
        assertEquals(100, leaderboard.getHighScore("MEDIUM"));
        assertEquals(50, leaderboard.getHighScore("EASY")); 
    }

    @Test
    void testGetHighScoreInvalidDifficulty() {
        assertEquals(0, leaderboard.getHighScore("INVALID"));
    }

    @Test
    void testDisplayHighScores() {
        leaderboard.updateHighScore("EASY", 75);
        leaderboard.updateHighScore("MEDIUM", 150);
        leaderboard.updateHighScore("HARD", 225);

        String expected = "EASY: 75\nMEDIUM: 150\nHARD: 225";
        assertEquals(expected, leaderboard.displayHighScores());
    }

    @Test
    void testResetEasyScore() {
        leaderboard.updateHighScore("EASY", 50);
        leaderboard.resetScoreForDifficulty("EASY");
        assertEquals(0, leaderboard.getHighScore("EASY"));
    }

    @Test
    void testResetMediumScore() {
        leaderboard.updateHighScore("MEDIUM", 100);
        leaderboard.resetScoreForDifficulty("MEDIUM");
        assertEquals(0, leaderboard.getHighScore("MEDIUM"));
    }

    @Test
    void testResetHardScore() {
        leaderboard.updateHighScore("HARD", 150);
        leaderboard.resetScoreForDifficulty("HARD");
        assertEquals(0, leaderboard.getHighScore("HARD"));
    }

    @Test
    void testResetWithLowerCaseInput() {
        leaderboard.updateHighScore("EASY", 60);
        leaderboard.updateHighScore("MEDIUM", 80);
        leaderboard.updateHighScore("HARD", 120);

        leaderboard.resetScoreForDifficulty("easy");
        leaderboard.resetScoreForDifficulty("medium");
        leaderboard.resetScoreForDifficulty("hard");

        assertEquals(0, leaderboard.getHighScore("EASY"));
        assertEquals(0, leaderboard.getHighScore("MEDIUM"));
        assertEquals(0, leaderboard.getHighScore("HARD"));
    }

    @Test
    void testResetWithMixedCaseInput() {
        leaderboard.updateHighScore("EASY", 90);
        leaderboard.updateHighScore("MEDIUM", 110);
        leaderboard.updateHighScore("HARD", 130);

        leaderboard.resetScoreForDifficulty("EaSy");
        leaderboard.resetScoreForDifficulty("MeDiUm");
        leaderboard.resetScoreForDifficulty("HaRd");

        assertEquals(0, leaderboard.getHighScore("EASY"));
        assertEquals(0, leaderboard.getHighScore("MEDIUM"));
        assertEquals(0, leaderboard.getHighScore("HARD"));
    }

    @Test
    void testInvalidDifficultyDoesNotChangeScores() {
        leaderboard.updateHighScore("EASY", 55);
        leaderboard.updateHighScore("MEDIUM", 77);
        leaderboard.updateHighScore("HARD", 99);

        leaderboard.resetScoreForDifficulty("INVALID"); 

        assertEquals(55, leaderboard.getHighScore("EASY"));
        assertEquals(77, leaderboard.getHighScore("MEDIUM"));
        assertEquals(99, leaderboard.getHighScore("HARD"));
    }

    @Test
    void testSortedListAllZero() {
        ArrayList<String> sorted = leaderboard.sortedList();
        assertEquals(3, sorted.size());
        assertEquals("EASY", sorted.get(0));
        assertEquals("MEDIUM", sorted.get(1));
        assertEquals("HARD", sorted.get(2));
    }

    @Test
    void testSortedListWithScores() {
        leaderboard.updateHighScore("EASY", 3);
        leaderboard.updateHighScore("MEDIUM", 5);
        leaderboard.updateHighScore("HARD", 1);
        
        ArrayList<String> sorted = leaderboard.sortedList();
        assertEquals("MEDIUM", sorted.get(0)); // Highest score (5)
        assertEquals("EASY", sorted.get(1));   // Second highest (3)
        assertEquals("HARD", sorted.get(2));   // Lowest score (1)
    }

    @Test
    void testSortedListWithTies() {
        leaderboard.updateHighScore("EASY", 5);
        leaderboard.updateHighScore("MEDIUM", 5);
        leaderboard.updateHighScore("HARD", 5);
        
        ArrayList<String> sorted = leaderboard.sortedList();
        assertEquals(3, sorted.size());
        // Order should be maintained for equal scores
        assertTrue(sorted.contains("EASY"));
        assertTrue(sorted.contains("MEDIUM"));
        assertTrue(sorted.contains("HARD"));
    }

    @Test
    void testUpdateHighScoreHigher() {
        leaderboard.updateHighScore("EASY", 5);
        assertEquals(5, leaderboard.getEasyHighScore());
        leaderboard.updateHighScore("EASY", 8);
        assertEquals(8, leaderboard.getEasyHighScore());
    }

    @Test
    void testUpdateHighScoreLower() {
        leaderboard.updateHighScore("MEDIUM", 10);
        assertEquals(10, leaderboard.getMediumHighScore());
        leaderboard.updateHighScore("MEDIUM", 7);
        assertEquals(10, leaderboard.getMediumHighScore());
    }

    @Test
    void testUpdateHighScoreInvalidDifficulty() {
        leaderboard.updateHighScore("INVALID", 5);
        assertEquals(0, leaderboard.getEasyHighScore());
        assertEquals(0, leaderboard.getMediumHighScore());
        assertEquals(0, leaderboard.getHardHighScore());
    }

    @Test
    void testGetters() {
        assertEquals(0, leaderboard.getEasyHighScore());
        assertEquals(0, leaderboard.getMediumHighScore());
        assertEquals(0, leaderboard.getHardHighScore());
        
        leaderboard.updateHighScore("EASY", 3);
        leaderboard.updateHighScore("MEDIUM", 5);
        leaderboard.updateHighScore("HARD", 7);
        
        assertEquals(3, leaderboard.getEasyHighScore());
        assertEquals(5, leaderboard.getMediumHighScore());
        assertEquals(7, leaderboard.getHardHighScore());
    }

}