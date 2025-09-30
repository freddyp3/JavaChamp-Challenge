package model;

//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

/**
 * Represents a single multiple-choice question.
 */
public class Question implements Writable {
    
    private String question;
    private String answer;
    private ArrayList<String> options;

    // EFFECTS: Initializes Question using parameters
    public Question(String question, String answer, ArrayList<String> options) {
        this.question = question;
        this.answer = answer;
        this.options = options;
        EventLog.getInstance().logEvent(new Event("Question created"));
    }

    public String getAnswer() {
        return answer;
    }
    
    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    // EFFECTS: checks whether the answer given is correct or not
    public boolean checkAnswer(int i) {
        if (i > options.size() - 1 || i < 0) {
            return false;
        }
        return options.get(i).equals(answer);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("question", question);
        json.put("answer", answer);

        JSONArray optionsArray = new JSONArray();
        for (String option : options) {
            optionsArray.put(option);
        }
        json.put("options", optionsArray);
        return json;
    }
}