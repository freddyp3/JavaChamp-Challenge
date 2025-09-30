package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Question class.
 */
public class QuestionTest {
    Question question;
    ArrayList<String> options;

    @BeforeEach
    void runBefore() {
        options = new ArrayList<>();
        options.add("Scripting");
        options.add("Functional");
        options.add("Object Oriented");

        question = new Question("What type of programming language is Java?", "Object Oriented", options);
    }

    @Test
    void testGetQuestion() {
        assertEquals("What type of programming language is Java?", question.getQuestion());
    }

    @Test
    void testGetAnswer() {
        assertEquals("Object Oriented", question.getAnswer());
    }

    @Test
    void testGetOptions() {
        assertEquals(3, question.getOptions().size());
        assertEquals("Scripting", question.getOptions().get(0));
        assertEquals("Functional", question.getOptions().get(1));
        assertEquals("Object Oriented", question.getOptions().get(2));
    }

    @Test
    void testCheckCorrectAnswer() {
        assertTrue(question.checkAnswer(2));
    }

    @Test
    void testCheckIncorrectAnswer() {
        assertFalse(question.checkAnswer(0));
        assertFalse(question.checkAnswer(1));
    }

    @Test
    void testCheckOutOfBoundsAnswer() {
        assertFalse(question.checkAnswer(3));
        assertFalse(question.checkAnswer(-1));
    }
}
