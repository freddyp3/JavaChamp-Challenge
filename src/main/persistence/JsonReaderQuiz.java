package persistence;

import model.Question;
import model.Quiz;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a reader that reads quizzes from JSON data stored in file
public class JsonReaderQuiz {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReaderQuiz(String source) {
        this.source = source;
    }

    // EFFECTS: reads quiz from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Quiz read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseQuiz(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses quiz from JSON object and returns it
    private Quiz parseQuiz(JSONObject jsonObject) {
        String difficulty = jsonObject.getString("difficultyLevel");
    
        JSONArray questionsArray = jsonObject.getJSONArray("questions");
        ArrayList<Question> questionsList = new ArrayList<>();
    
        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionJson = questionsArray.getJSONObject(i);
            
            String questionText = questionJson.getString("question");
            String correctAnswer = questionJson.getString("answer");
    
            JSONArray optionsArray = questionJson.getJSONArray("options");
            ArrayList<String> options = new ArrayList<>();
            for (int j = 0; j < optionsArray.length(); j++) {
                options.add(optionsArray.getString(j));
            }
    
            questionsList.add(new Question(questionText, correctAnswer, options));
        }
        return new Quiz(difficulty, questionsList);
    }
    
}