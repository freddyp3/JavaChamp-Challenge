package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Quiz class.
 */
public class QuizTest {

    Quiz quiz;
    Question q1;
    Question q2;
    ArrayList<String> options1;
    ArrayList<String> options2;
    ArrayList<Question> questions;
    
    @BeforeEach
    void runBefore() {
        options1 = new ArrayList<>();
        options1.add("Scripting");
        options1.add("Functional");
        options1.add("Object Oriented");

        options2 = new ArrayList<>();
        options2.add("1959");
        options2.add("1999");
        options2.add("1995");

        q1 = new Question("What type of programming language is Java?", "Object Oriented", options1);
        q2 = new Question("When was Java created?", "1995", options2);

        questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);

        quiz = new Quiz("EASY", questions);
    }

    @Test
    void testQuizConstructor() {
        assertEquals("EASY", quiz.getDifficultyLevel());
        assertEquals(2, quiz.getQuestions().size());
        assertEquals(0, quiz.getCurrentScore());
        assertEquals(0, quiz.getIndex());
        assertEquals(60, quiz.getTimeLimit());
    }

    @Test
    void testGetDifficultyLevel() {
        assertEquals("EASY", quiz.getDifficultyLevel());
    }

    @Test
    void testAnswerQuestionCorrectly() {
        assertEquals("Correct!", quiz.answerQuestion(2));
        assertEquals(1, quiz.getCurrentScore());
        assertEquals(1, quiz.getIndex());
        assertEquals("Object Oriented", quiz.getAnswers().get(0));
    }

    @Test
    void testAnswerQuestionIncorrectly() {
        assertEquals("Incorrect", quiz.answerQuestion(0));
        assertEquals(0, quiz.getCurrentScore());
        assertEquals(1, quiz.getIndex());
        assertEquals("Scripting", quiz.getAnswers().get(0));
    }

    @Test
    void testOutOfBoundsAnswer() {
        assertEquals("Thats not an option!", quiz.answerQuestion(5));
        assertTrue(quiz.getAnswers().isEmpty());
    }

    @Test
    void testHasNextQuestion() {
        assertTrue(quiz.hasNextQuestion()); // Call the actual method
        quiz.answerQuestion(2);
        assertTrue(quiz.hasNextQuestion()); // Call the actual method
        quiz.answerQuestion(2);
        assertFalse(quiz.hasNextQuestion()); // Call the actual method
    }

    @Test
    void testReviewQuiz() {
        quiz.answerQuestion(2);
        quiz.answerQuestion(1);

        List<String> review = quiz.reviewQuiz();
        assertEquals(4, review.size());
        assertEquals("Q: What type of programming language is Java?", review.get(0));
        assertEquals("Correct Answer: Object Oriented", review.get(1));
        assertEquals("Q: When was Java created?", review.get(2));
        assertEquals("Correct Answer: 1995", review.get(3));
    }
    
    @Test
    void testGetAnswers() {
        quiz.answerQuestion(2);
        quiz.answerQuestion(1);

        List<String> answers = quiz.getAnswers();
        assertEquals(2, answers.size());
        assertEquals("Object Oriented", answers.get(0));
        assertEquals("1999", answers.get(1));
    }

    @Test
    void testAddQuestion() {
        int initialSize = quiz.getQuestions().size();
        
        ArrayList<String> newOptions = new ArrayList<>();
        newOptions.add("Option A");
        newOptions.add("Option B");
        newOptions.add("Option C");
        
        Question newQuestion = new Question("New test question?", "Option B", newOptions);
        quiz.addQuestion(newQuestion);
        
        assertEquals(initialSize + 1, quiz.getQuestions().size());
        assertEquals(newQuestion, quiz.getQuestions().get(quiz.getQuestions().size() - 1));
    }

    @Test
    void testAddMultipleQuestions() {
        int initialSize = quiz.getQuestions().size();
        
        ArrayList<String> options1 = new ArrayList<>();
        options1.add("A1");
        options1.add("B1");
        
        ArrayList<String> options2 = new ArrayList<>();
        options2.add("A2");
        options2.add("B2");
        
        Question q1 = new Question("Q1?", "A1", options1);
        Question q2 = new Question("Q2?", "B2", options2);
        
        quiz.addQuestion(q1);
        quiz.addQuestion(q2);
        
        assertEquals(initialSize + 2, quiz.getQuestions().size());
        assertTrue(quiz.getQuestions().contains(q1));
        assertTrue(quiz.getQuestions().contains(q2));
    }
}