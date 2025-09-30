package ui;

import model.Quiz;
import model.Question;
import model.Leaderboard;
import persistence.JsonWriterQuiz;
import persistence.JsonReaderQuiz;
import persistence.JsonWriterLeaderboard;
import persistence.JsonReaderLeaderboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the console-based UI for JavaChamp Challenge.
 */
public class JavaChampApp {

    private Quiz quiz;
    private Leaderboard leaderboard;
    private Scanner input;
    private ArrayList<Question> questionPoolEasy;
    private ArrayList<Question> questionPoolMedium;
    private ArrayList<Question> questionPoolHard;
    private JsonWriterQuiz quizEasyWriter;
    private JsonReaderQuiz quizEasyReader;
    private JsonWriterQuiz quizMediumWriter;
    private JsonReaderQuiz quizMediumReader;
    private JsonWriterQuiz quizHardWriter;
    private JsonReaderQuiz quizHardReader;
    private JsonWriterLeaderboard leaderboardWriter;
    private JsonReaderLeaderboard leaderboardReader;
   
    // EFFECTS: Initializes the game, leaderboard, and default questions, then runs the application.
    public JavaChampApp() {
        leaderboard = new Leaderboard();
        input = new Scanner(System.in);
        questionPoolEasy = new ArrayList<>();
        questionPoolMedium = new ArrayList<>();
        questionPoolHard = new ArrayList<>();
        initializeDefaultQuestionsEasy();
        initializeDefaultQuestionsMedium();
        initializeDefaultQuestionsHard();

        quizEasyWriter = new JsonWriterQuiz("./data/quizEasy.json");
        quizEasyReader = new JsonReaderQuiz("./data/quizEasy.json");
        quizMediumWriter = new JsonWriterQuiz("./data/quizMedium.json");
        quizMediumReader = new JsonReaderQuiz("./data/quizMedium.json");
        quizHardWriter = new JsonWriterQuiz("./data/quizHard.json");
        quizHardReader = new JsonReaderQuiz("./data/quizHard.json");
        leaderboardWriter = new JsonWriterLeaderboard("./data/leaderboard.json");
        leaderboardReader = new JsonReaderLeaderboard("./data/leaderboard.json");

        runApplication();
    }

    // MODIFIES: this
    // EFFECTS: Initializes the default set of easy difficulty questions.
    public void initializeDefaultQuestionsEasy() {
        ArrayList<String> options1 = new ArrayList<>();
        options1.add("Scripting");
        options1.add("Functional");
        options1.add("Object Oriented");
        options1.add("Procedural");

        ArrayList<String> options2 = new ArrayList<>();
        options2.add("1995");
        options2.add("1994");
        options2.add("1996");
        options2.add("1959");

        ArrayList<String> options3 = new ArrayList<>();
        options3.add("JVM");
        options3.add("JRE");
        options3.add("JDK");
        options3.add("Compiler");

        questionPoolEasy.add(new Question("What type of programming language is Java?", "Object Oriented", options1));
        questionPoolEasy.add(new Question("When was Java created?", "1995", options2));
        questionPoolEasy.add(new Question("Which component is required to run Java programs?", "JRE", options3));
    }

    // MODIFIES: this
    // EFFECTS: Initializes the default set of medium difficulty questions.
    public void initializeDefaultQuestionsMedium() {
        ArrayList<String> options1 = new ArrayList<>();
        options1.add("char");
        options1.add("int");
        options1.add("String");
        options1.add("float");

        ArrayList<String> options2 = new ArrayList<>();
        options2.add("Heap Memory");
        options2.add("Stack Memory");
        options2.add("Method Area");
        options2.add("Registers");

        ArrayList<String> options3 = new ArrayList<>();
        options3.add("for");
        options3.add("while");
        options3.add("do-while");
        options3.add("foreach");

        questionPoolMedium.add(new Question("Which data type is used to store text in Java?", "String", options1));
        questionPoolMedium.add(new Question("Where are objects stored in memory?", "Heap Memory", options2));
        questionPoolMedium.add(new Question("Which loop guarantees at least one execution?", "do-while", options3));
    }

    // MODIFIES: this
    // EFFECTS: Initializes the default set of hard difficulty questions.
    public void initializeDefaultQuestionsHard() {
        ArrayList<String> options1 = new ArrayList<>();
        options1.add("Polymorphism");
        options1.add("Abstraction");
        options1.add("Encapsulation");
        options1.add("Inheritance");

        ArrayList<String> options2 = new ArrayList<>();
        options2.add("ArrayList");
        options2.add("LinkedList");
        options2.add("HashMap");
        options2.add("HashSet");

        ArrayList<String> options3 = new ArrayList<>();
        options3.add("final");
        options3.add("static");
        options3.add("volatile");
        options3.add("synchronized");

        questionPoolHard.add(new Question("Which OOP principle allows method overriding?", "Polymorphism", options1));
        questionPoolHard.add(new Question("Which data structure maintains insertion order?", "LinkedList", options2));
        questionPoolHard.add(new Question("Which keyword prevents thread interference?", "synchronized", options3));
    }

    // MODIFIES: this
    // EFFECTS: Runs the main menu of the application
    @SuppressWarnings("methodlength")
    public void runApplication() {
        boolean keepGoing = true;
        while (keepGoing) {
            displayMainMenu();
            int command = input.nextInt();
            switch (command) {
                case 1:
                    startQuiz();
                    break;
                case 2:
                    addQuestionToPool();
                    break;
                case 3:
                    displayHighScores();
                    break;
                case 4:
                    saveLeaderboard();
                    break;
                case 5:
                    loadLeaderboard();
                    break;
                case 0:
                    keepGoing = false;
                    break;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        }
        System.out.println("\nThanks for playing JavaChamp Challenge!");
    }

    // EFFECTS: Saves the leaderboard to JSON
    public void saveLeaderboard() {
        try {
            leaderboardWriter.open();
            leaderboardWriter.write(leaderboard);
            leaderboardWriter.close();

            System.out.println("Leaderboard saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving leaderboard.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads the leaderboard from JSON
    public void loadLeaderboard() {
        try {
            leaderboard = leaderboardReader.read();
            System.out.println("Leaderboard loaded successfully!");
        } catch (IOException e) {
            System.out.println("Error loading leaderboard.");
        }
    }

    // EFFECTS: Saves the question sets to JSON
    public void saveQuestions(int difficulty) {
        try {
            switch (difficulty) {
                case 0:
                    quizEasyWriter.open();
                    quizEasyWriter.write(new Quiz("EASY", questionPoolEasy));
                    quizEasyWriter.close();
                    break;
                case 1:
                    quizMediumWriter.open();
                    quizMediumWriter.write(new Quiz("MEDIUM", questionPoolMedium));
                    quizMediumWriter.close();
                    break;
                case 2:
                    quizHardWriter.open();
                    quizHardWriter.write(new Quiz("HARD", questionPoolHard));
                    quizHardWriter.close();
                    break;
            }
        } catch (IOException e) {
            System.out.println("Error saving question set.");
        }
    }

    // EFFECTS: Prompts the user to select a difficulty level and loads the corresponding question set.
    //          If an invalid choice is made, displays an error message.
    public void loadQuestions() {
        System.out.print("Enter difficulty to load (0-EASY, 1-MEDIUM, 2-HARD): ");
        int difficultyChoice = input.nextInt();
    
        if (difficultyChoice >= 0 && difficultyChoice <= 2) {
            loadQuestionSet(difficultyChoice);
        } else {
            System.out.println("Invalid choice. Returning to menu.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads the question set for the selected difficulty
    public void loadQuestionSet(int difficultyChoice) {
        JsonReaderQuiz reader = getQuizReader(difficultyChoice);
        ArrayList<Question> questionPool = getQuestionPool(difficultyChoice);
        String difficulty = getDifficultyName(difficultyChoice);

        if (reader == null || questionPool == null) {
            System.out.println("Invalid difficulty selection.");
            return;
        }

        try {
            Quiz loadedQuiz = reader.read();
            questionPool.clear();
            questionPool.addAll(loadedQuiz.getQuestions());
            System.out.println(difficulty + " question set loaded successfully!");
        } catch (IOException e) {
            System.out.println("Error loading " + difficulty.toLowerCase() + " question set.");
        }
    }

    // EFFECTS: Returns the corresponding JsonReaderQuiz for the given difficulty
    public JsonReaderQuiz getQuizReader(int difficultyChoice) {
        switch (difficultyChoice) {
            case 0: return quizEasyReader;
            case 1: return quizMediumReader;
            case 2: return quizHardReader;
            default: return null;
        }
    }

    // EFFECTS: Returns the corresponding question pool for the given difficulty
    public ArrayList<Question> getQuestionPool(int difficultyChoice) {
        switch (difficultyChoice) {
            case 0: return questionPoolEasy;
            case 1: return questionPoolMedium;
            case 2: return questionPoolHard;
            default: return null;
        }
    }

    // EFFECTS: Returns the difficulty name as a string
    public String getDifficultyName(int difficultyChoice) {
        switch (difficultyChoice) {
            case 0: return "Easy";
            case 1: return "Medium";
            case 2: return "Hard";
            default: return "Unknown";
        }
    }
 
    // EFFECTS: Displays the main menu
    public void displayMainMenu() {
        System.out.println("\nWelcome to JavaChamp Challenge!");
        System.out.println("1 -> Select Difficulty & Start Quiz");
        System.out.println("2 -> Add Questions to the Pool");
        System.out.println("3 -> View Leaderboard");
        System.out.println("4 -> Save Leaderboard");
        System.out.println("5 -> Load Leaderboard");
        System.out.println("0 -> Quit");
        System.out.print("Enter your choice: ");
    }

    // MODIFIES: this
    // EFFECTS: Starts a quiz session based on the selected difficulty
    @SuppressWarnings("methodlength")
    public void startQuiz() {
        System.out.print("Would you like to load a saved quiz with saved questions? (y/n): ");
        input.nextLine();
        String response = input.nextLine().trim().toLowerCase();
        if (response.equals("y")) {
            loadQuestions();
        }

        System.out.print("Enter difficulty (0-EASY, 1-MEDIUM, 2-HARD): ");
        int difficultyChoice = input.nextInt();
        ArrayList<Question> selectedPool;
        String difficulty;
        
        switch (difficultyChoice) {
            case 0:
                selectedPool = questionPoolEasy;
                difficulty = "EASY";
                break;
            case 1:
                selectedPool = questionPoolMedium;
                difficulty = "MEDIUM";
                break;
            case 2:
                selectedPool = questionPoolHard;
                difficulty = "HARD";
                break;
            default:
                System.out.println("Invalid difficulty. Returning to menu.");
                return;
        }

        quiz = new Quiz(difficulty, new ArrayList<>(selectedPool));
        playQuiz();
    }

    // MODIFIES: this
    // EFFECTS: Runs the quiz and updates scores
    public void playQuiz() {
        while (quiz.hasNextQuestion()) {
            displayQuestion();
            int answer = input.nextInt();
            System.out.println(quiz.answerQuestion(answer));
        }

        System.out.println("\nQuiz Over! Final Score: " + quiz.getCurrentScore());
        leaderboard.updateHighScore(quiz.getDifficultyLevel(), quiz.getCurrentScore());
        reviewQuiz();
    }

    // EFFECTS: Reviews the quiz session and displays user answers and correct answers
    public void reviewQuiz() {
        System.out.println("\nQuiz Review:");
        List<String> questions = quiz.reviewQuiz();
        List<String> userAnswers = quiz.getAnswers();

        for (int i = 0; i < questions.size() / 2; i++) {
            System.out.println(questions.get(i * 2));
            System.out.println("Your Answer: " + userAnswers.get(i));
            System.out.println(questions.get(i * 2 + 1));
        }
    }

    // EFFECTS: Displays the current quiz question and answer choices
    public void displayQuestion() {
        System.out.println("\n" + quiz.getQuestions().get(quiz.getIndex()).getQuestion());
        List<String> options = quiz.getQuestions().get(quiz.getIndex()).getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println(i + ": " + options.get(i));
        }
        System.out.print("Enter your answer (0-" + (options.size() - 1) + "): ");
    }

    // EFFECTS: Displays leaderboard scores
    public void displayHighScores() {
        System.out.println("\nLeaderboard:");
        System.out.println(leaderboard.displayHighScores());
    }
    
    // MODIFIES: this
    // EFFECTS: Adds questions to the specified pool
    @SuppressWarnings("methodlength")
    public void addQuestionToPool() {
        input.nextLine();
        System.out.print("Enter the question text: ");
        String questionText = input.nextLine();
        
        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            System.out.print("Enter option " + i + ": ");
            options.add(input.nextLine());
        }

        System.out.print("Enter the index of the correct answer (0-3): ");
        int correctAnswer = input.nextInt();

        System.out.print("Enter difficulty level (0-EASY, 1-MEDIUM, 2-HARD): ");
        int difficultyChoice = input.nextInt();
        
        Question newQuestion = new Question(questionText, options.get(correctAnswer), new ArrayList<>(options));
        
        switch (difficultyChoice) {
            case 0:
                questionPoolEasy.add(newQuestion);
                break;
            case 1:
                questionPoolMedium.add(newQuestion);
                break;
            case 2:
                questionPoolHard.add(newQuestion);
                break;
            default:
                System.out.println("Invalid difficulty. Question not added.");
                return;
        }

        System.out.println("Question added successfully!");
        System.out.print("Would you like to save the updated quiz? (y/n): ");
        input.nextLine();
        String response = input.nextLine().trim().toLowerCase();
    
        if (response.equals("y")) {
            saveQuestions(difficultyChoice);
        }
    }

    // EFFECTS: Main entry point
    public static void main(String[] args) {
        new JavaChampApp();
    }
}