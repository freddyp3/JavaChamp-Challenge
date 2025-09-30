package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import model.Question;
import model.Quiz;
import java.util.List;

public class JsonQuizTest {
    protected void checkQuiz(String difficulty, List<Question> expectedQuestions, Quiz quiz) {
        assertEquals(difficulty, quiz.getDifficultyLevel());
        assertEquals(expectedQuestions.size(), quiz.getQuestions().size());

        for (int i = 0; i < expectedQuestions.size(); i++) {
            Question expected = expectedQuestions.get(i);
            Question actual = quiz.getQuestions().get(i);

            assertEquals(expected.getQuestion(), actual.getQuestion());
            assertEquals(expected.getAnswer(), actual.getAnswer());
            assertEquals(expected.getOptions(), actual.getOptions());
        }
    }
}