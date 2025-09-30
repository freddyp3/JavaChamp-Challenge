package model;

/**
 * Represents a score entry for a specific difficulty level, used for leaderboard sorting.
 */
public class ScorePair implements Comparable<ScorePair> {

    String difficulty;
    int score;
    
    // EFFECTS: Creates a ScorePair with the specified difficulty and score
    ScorePair(String difficulty, int score) {
        this.difficulty = difficulty;
        this.score = score;
    }
    
    // EFFECTS: Compares this score pair with another based on score value
    //          in descending order (higher scores come first)
    @Override
    public int compareTo(ScorePair other) {
        return Integer.compare(other.score, this.score);
    }
    
}
