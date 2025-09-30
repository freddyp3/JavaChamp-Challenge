package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.json.JSONObject;

import persistence.Writable;

/**
 * Represents a leaderboard that tracks high scores for each difficulty level.
 */
public class Leaderboard implements Writable {
    private int easyHighScore;
    private int mediumHighScore;
    private int hardHighScore; 

    // EFFECTS: Initializes leaderboard with default high scores of 0
    public Leaderboard() {
        easyHighScore = 0;
        mediumHighScore = 0;
        hardHighScore = 0;
        EventLog.getInstance().logEvent(new Event("Leaderboard created"));
    }

    // EFFECTS: Initializes leaderboard with specified scores
    public Leaderboard(int easy, int medium, int hard) {
        this.easyHighScore = easy;
        this.mediumHighScore = medium;
        this.hardHighScore = hard;
        EventLog.getInstance().logEvent(new Event("Leaderboard created"));
    }

    // MODIFIES: this
    // EFFECTS: Updates the high score for the given difficulty if the new score is higher
    public void updateHighScore(String difficulty, int score) {
        if (difficulty.equals("EASY") && score > easyHighScore) {
            easyHighScore = score;
            EventLog.getInstance().logEvent(new Event("Easy score updated"));
        } else if (difficulty.equals("MEDIUM") && score > mediumHighScore) {
            mediumHighScore = score;
            EventLog.getInstance().logEvent(new Event("Medium score updated"));
        } else if (difficulty.equals("HARD") && score > hardHighScore) {
            hardHighScore = score;
            EventLog.getInstance().logEvent(new Event("Hard score updated"));
        }
        return;
    }

    // EFFECTS: Returns the high score for the given difficulty
    public int getHighScore(String difficulty) {
        if (difficulty.equals("EASY")) {
            return easyHighScore;
        } else if (difficulty.equals("MEDIUM")) {
            return mediumHighScore;
        } else if (difficulty.equals("HARD")) {
            return hardHighScore;
        } else {
            return 0; 
        }
    }

    // EFFECTS: Returns all high scores in a readable format
    public String displayHighScores() {
        return "EASY: " + easyHighScore + "\n" + "MEDIUM: " + mediumHighScore + "\n" + "HARD: " + hardHighScore;
    }

    // EFFECTS: Resests the specific highscore
    // MODIFIES: this
    public void resetScoreForDifficulty(String difficulty) {
        if (difficulty.toUpperCase().equals("EASY")) {
            easyHighScore = 0;
            EventLog.getInstance().logEvent(new Event("Easy score reset"));
        }
        if (difficulty.toUpperCase().equals("MEDIUM")) {
            mediumHighScore = 0;
            EventLog.getInstance().logEvent(new Event("Medium score reset"));
        }
        if (difficulty.toUpperCase().equals("HARD")) {
            hardHighScore = 0;
            EventLog.getInstance().logEvent(new Event("Hard score reset"));
        }
    }

    // EFFECTS: Returns scores in a sorted list {lowest, medium, highest}
    public ArrayList<String> sortedList() {
        ArrayList<String> scoreNames = new ArrayList<>();
        
        ArrayList<ScorePair> pairs = new ArrayList<>();
        pairs.add(new ScorePair("EASY", easyHighScore));
        pairs.add(new ScorePair("MEDIUM", mediumHighScore));
        pairs.add(new ScorePair("HARD", hardHighScore));
        
        Collections.sort(pairs);
        
        for (ScorePair pair : pairs) {
            scoreNames.add(pair.difficulty);
        }

        EventLog.getInstance().logEvent(new Event("Scores sorted for leaderboard visual"));
        return scoreNames;
    }

    public int getMediumHighScore() {
        return this.mediumHighScore;
    }

    public int getHardHighScore() {
        return this.hardHighScore;
    }

    public int getEasyHighScore() {
        return this.easyHighScore;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("easyHighScore", easyHighScore);
        json.put("mediumHighScore", mediumHighScore);
        json.put("hardHighScore", hardHighScore);
        return json;
    }

}
