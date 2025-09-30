package persistence;

import model.Leaderboard;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads leaderboards from JSON data stored in file
public class JsonReaderLeaderboard {
    private String source; 

    // EFFECTS: constructs reader to read from source file
    public JsonReaderLeaderboard(String source) {
        this.source = source;
    }

    // EFFECTS: reads leaderboard from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Leaderboard read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLeaderboard(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses leaderboard from JSON object and returns it
    private Leaderboard parseLeaderboard(JSONObject jsonObject) {
        int easy = jsonObject.getInt("easyHighScore");
        int medium = jsonObject.getInt("mediumHighScore");
        int hard = jsonObject.getInt("hardHighScore");
        return new Leaderboard(easy, medium, hard);
    }

}
