package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

/**
 * Represents a quiz session with a difficulty level and a set of questions.
 */
public class Quiz implements Writable {
    
    private String difficultyLevel;
    private ArrayList<Question> questions;
    private int currentScore;
    private int index;
    private int timeLimit;
    private ArrayList<String> answers;

    // EFFECTS: Initializes Quiz with the given parameters for (...)
    public Quiz(String difficultyLevel, ArrayList<Question> questions) {
        this.difficultyLevel = difficultyLevel;
        this.questions = questions;
        currentScore = 0;
        index = 0;
        timeLimit = 60;
        answers = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Quiz created"));
    }


    // MODIFIES: this
    // EFFECTS: Answers the current question updating the score if correct
    public String answerQuestion(int i) {
        if (i > questions.get(index).getOptions().size() - 1) {
            return "Thats not an option!";
        }
        answers.add(questions.get(index).getOptions().get(i));
        if (questions.get(index).checkAnswer(i)) {
            index++;
            currentScore++;
            return "Correct!";
        } else {
            index++;
            return "Incorrect";
        }
    }

    // EFFECTS: Returns a list of reviewed questions with correct answers
    public List<String> reviewQuiz() {
        List<String> review = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            Question q = questions.get(i);
            review.add("Q: " + q.getQuestion());
            review.add("Correct Answer: " + q.getAnswer());
        }
        return review;
    }

    // EFFECTS: Returns true if there are more questions left in the quiz
    public boolean hasNextQuestion() {
        return index < questions.size();
    }

    // EFFECTS: Adds questions to the quiz
    // MODIFIES: this
    public void addQuestion(Question question) {
        questions.add(question);
        EventLog.getInstance().logEvent(new Event("Question added to " + difficultyLevel + " quiz"));
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public String getDifficultyLevel() { 
        return difficultyLevel;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getIndex() {
        return index; 
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("difficultyLevel", difficultyLevel);
    
        JSONArray questionsArray = new JSONArray();
        for (Question question : questions) {
            questionsArray.put(question.toJson());
        }
        json.put("questions", questionsArray);
        return json;
    }
    
}
