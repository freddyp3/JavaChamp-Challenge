package persistence;

import model.Question;
import model.Quiz;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterQuizTest extends JsonQuizTest {

    @Test
    void testQuizWriterInvalidFile() {
        try {
            Quiz quiz = new Quiz("Easy", new ArrayList<>());
            JsonWriterQuiz writer = new JsonWriterQuiz("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testQuizWriterEmpty() {
        try {
            Quiz quiz = new Quiz("Easy", new ArrayList<>());
            JsonWriterQuiz writer = new JsonWriterQuiz("./data/testJson/testWriterEmptyQuiz.json");
            writer.open();
            writer.write(quiz);
            writer.close();
    
            JsonReaderQuiz reader = new JsonReaderQuiz("./data/testJson/testWriterEmptyQuiz.json");
            quiz = reader.read();
            checkQuiz("Easy", new ArrayList<>(), quiz);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
    
    @Test
    void testQuizWriterGeneral() {
        try {
            ArrayList<Question> questions = new ArrayList<>();
            ArrayList<String> options1 = new ArrayList<>(List.of("A", "B", "C", "D"));
            questions.add(new Question("What is 2+2?", "B", options1));
    
            Quiz quiz = new Quiz("Medium", questions);
    
            JsonWriterQuiz writer = new JsonWriterQuiz("./data/testJson/testWriterGeneralQuiz.json");
            writer.open();
            writer.write(quiz);
            writer.close();
    
            JsonReaderQuiz reader = new JsonReaderQuiz("./data/testJson/testWriterGeneralQuiz.json");
            quiz = reader.read();
    
            checkQuiz("Medium", questions, quiz);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}