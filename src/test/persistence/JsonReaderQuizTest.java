package persistence;

import model.Quiz;
import model.Question;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderQuizTest extends JsonQuizTest {

    private JsonReaderQuiz reader;

    @Test
    void testReaderNonExistentFile() {
        reader = new JsonReaderQuiz("./data/testJson/nonexistent.json");
        try {
            Quiz quiz = reader.read();
            fail("IOException was expected but not thrown");
        } catch (IOException e) {
            // Test passes (IOException was expected)
        }
    }

    @Test
    void testReaderEmptyQuiz() {
        reader = new JsonReaderQuiz("./data/testJson/quizEmpty.json");
        try {
            Quiz quiz = reader.read();
            assertNotNull(quiz);
            checkQuiz("Easy", new ArrayList<>(), quiz);
        } catch (IOException e) {
            fail();
        }
    }
    
    @Test
    void testReaderGeneralQuiz() {
        reader = new JsonReaderQuiz("./data/testJson/quizGeneral.json");
        try {
            Quiz quiz = reader.read();
            assertNotNull(quiz);
    
            List<Question> expectedQuestions = new ArrayList<>();
            ArrayList<String> options1 = new ArrayList<>(List.of("A", "B", "C", "D"));
            expectedQuestions.add(new Question("What is 2+2?", "B", options1));
    
            checkQuiz("Medium", expectedQuestions, quiz);
        } catch (IOException e) {
            fail();
        }
    }

}