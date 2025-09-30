package ui;

import model.Quiz;
import model.Question;
import model.Leaderboard;
import persistence.JsonWriterQuiz;
import persistence.JsonReaderQuiz;
import persistence.JsonWriterLeaderboard;
import persistence.JsonReaderLeaderboard;
import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the GUI-based UI for JavaChamp Challenge.
 */
public class JavaChampAppGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Quiz quiz;
    private Leaderboard leaderboard;
    private Quiz easyQuiz;
    private Quiz mediumQuiz;
    private Quiz hardQuiz;
    
    private JsonWriterQuiz quizEasyWriter;
    private JsonReaderQuiz quizEasyReader;
    private JsonWriterQuiz quizMediumWriter;
    private JsonReaderQuiz quizMediumReader;
    private JsonWriterQuiz quizHardWriter;
    private JsonReaderQuiz quizHardReader;
    private JsonWriterLeaderboard leaderboardWriter;
    private JsonReaderLeaderboard leaderboardReader;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JButton submitAnswerButton;
    private JButton returnToMenuButton;
    private int selectedOption = -1;

    private JTextField questionTextField;
    private JTextField[] optionTextFields;
    private JTextField correctAnswerTextField;
    private JComboBox<String> difficultyComboBox;

    private LeaderboardPodiumPanel leaderboardPodiumPanel;

    // EFFECTS: Initializes the GUI application for JavaChamp Challenge
    public JavaChampAppGUI() {
        super("JavaChamp Challenge");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                printEvents();
            }
        });
        setSize(600, 500);
        setLocationRelativeTo(null);

        leaderboard = new Leaderboard();
        easyQuiz = new Quiz("EASY", new ArrayList<>());
        mediumQuiz = new Quiz("MEDIUM", new ArrayList<>());
        hardQuiz = new Quiz("HARD", new ArrayList<>());

        initializeWriterAndReaders();

        initializeDefaultQuestionsEasy();
        initializeDefaultQuestionsMedium();
        initializeDefaultQuestionsHard();

        setupGUI();
        setVisible(true);
    }

    // EFFECTS: Initializes all Writers
    // MODIFIES: this
    public void initializeWriterAndReaders() {
        quizEasyWriter = new JsonWriterQuiz("./data/quizEasy.json");
        quizEasyReader = new JsonReaderQuiz("./data/quizEasy.json");
        quizMediumWriter = new JsonWriterQuiz("./data/quizMedium.json");
        quizMediumReader = new JsonReaderQuiz("./data/quizMedium.json");
        quizHardWriter = new JsonWriterQuiz("./data/quizHard.json");
        quizHardReader = new JsonReaderQuiz("./data/quizHard.json");
        leaderboardWriter = new JsonWriterLeaderboard("./data/leaderboard.json");
        leaderboardReader = new JsonReaderLeaderboard("./data/leaderboard.json");
    }

    // EFFECTS: Sets up the main GUI structure
    public void setupGUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupMainMenuPanel();
        setupQuizPanel();
        setupAddQuestionPanel();
        setupLeaderboardPanel();

        getContentPane().add(mainPanel);
    }

    // EFFECTS: Sets up the main menu panel
    @SuppressWarnings("methodlength")
    public void setupMainMenuPanel() {
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridLayout(5, 1, 10, 10));
        mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel titleLabel = new JLabel("JavaChamp Challenge", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton startQuizButton = createButton("Select Difficulty & Start Quiz", e -> showStartQuizDialog());
        JButton addQuestionButton = createButton(
                "Add Questions to the Pool", 
                e -> cardLayout.show(mainPanel, "AddQuestion"));
        JButton viewQuestionsButton = createButton("View Question Pool", e -> showViewQuestionsDialog());
        JButton viewLeaderboardButton = createButton("View Leaderboard", e -> {
            leaderboardPodiumPanel.updateLeaderboard(leaderboard);
            cardLayout.show(mainPanel, "Leaderboard");
        });
        JButton removeScoreButton = createButton("Remove Score", e -> showRemoveScoreDialog());
        JButton saveLeaderboardButton = createButton("Save Leaderboard", e -> saveLeaderboard());
        JButton loadLeaderboardButton = createButton("Load Leaderboard", e -> loadLeaderboard());
        JButton quitButton = createButton("Quit", e -> printEvents());

        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 10, 3));
        buttonPanel.add(startQuizButton);
        buttonPanel.add(addQuestionButton);
        buttonPanel.add(viewQuestionsButton);
        buttonPanel.add(viewLeaderboardButton);
        buttonPanel.add(removeScoreButton);
        buttonPanel.add(saveLeaderboardButton);
        buttonPanel.add(loadLeaderboardButton);
        buttonPanel.add(quitButton);

        mainMenuPanel.add(titleLabel);
        mainMenuPanel.add(buttonPanel);

        mainPanel.add(mainMenuPanel, "MainMenu");
    }

    public void printEvents() {
        System.out.println("\n--- JavaChamp Event Log ---");
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.toString());
        }
        System.out.println("--- End of Event Log ---\n");
        System.exit(0);
    }

    // EFFECTS: Shows a dialog to view questions for a specific difficulty
    public void showViewQuestionsDialog() {
        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(this,
                "Select difficulty level to view questions:", "View Questions",
                JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);

        if (selectedDifficulty != null) {
            viewQuestions(selectedDifficulty);
        }
    }

    // EFFECTS: Displays the questions for the selected difficulty level
    @SuppressWarnings("methodlength")
    public void viewQuestions(String difficulty) {
        ArrayList<Question> questionPool;
        
        switch (difficulty) {
            case "EASY":
                questionPool = easyQuiz.getQuestions();
                break;
            case "MEDIUM":
                questionPool = mediumQuiz.getQuestions();
                break;
            case "HARD":
                questionPool = hardQuiz.getQuestions();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid difficulty selection.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }
        
        if (questionPool.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available for " + difficulty + " difficulty.",
                    "Empty Question Pool", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JTextArea textArea = createQuestionListTextArea(questionPool);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JDialog dialog = new JDialog(this, difficulty + " Questions", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    // EFFECTS: Creates and populates a text area with questions from the pool
    public JTextArea createQuestionListTextArea(ArrayList<Question> questionPool) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        for (int i = 0; i < questionPool.size(); i++) {
            Question q = questionPool.get(i);
            textArea.append("Question " + (i + 1) + ": " + q.getQuestion() + "\n");
            
            List<String> options = q.getOptions();
            for (int j = 0; j < options.size(); j++) {
                textArea.append("  Option " + j + ": " + options.get(j) + "\n");
            }
            
            textArea.append("  Correct Answer: " + q.getAnswer() + "\n\n");
        }
        
        return textArea;
    }

    // EFFECTS: Shows a dialog to remove a score for a specific difficulty
    public void showRemoveScoreDialog() {
        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(this,
                "Select difficulty level to reset score:", "Remove Score",
                JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);

        if (selectedDifficulty != null) {
            int confirmResponse = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to reset the score for " + selectedDifficulty + " difficulty?",
                    "Confirm Score Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirmResponse == JOptionPane.YES_OPTION) {
                removeScore(selectedDifficulty);
            }
        }
    }

    // EFFECTS: Removes score for the specified difficulty and updates the leaderboard
    // MODIFIES: this, leaderboard
    public void removeScore(String difficulty) {
        leaderboard.resetScoreForDifficulty(difficulty);
        
        if (leaderboardPodiumPanel != null) {
            leaderboardPodiumPanel.updateLeaderboard(leaderboard);
        }
        
        JOptionPane.showMessageDialog(this, 
                "Score for " + difficulty + " difficulty has been reset.", 
                "Score Reset", JOptionPane.INFORMATION_MESSAGE);
        
        int saveResponse = JOptionPane.showConfirmDialog(this,
                "Would you like to save the updated leaderboard?",
                "Save Changes", JOptionPane.YES_NO_OPTION);
        
        if (saveResponse == JOptionPane.YES_OPTION) {
            saveLeaderboard();
        }
    }

    // EFFECTS: Creates a styled JButton with provided text and action listener
    public JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(listener);
        return button;
    }

    // EFFECTS: Shows a dialog to start a quiz with difficulty selection
    public void showStartQuizDialog() {
        String[] options = {"Yes", "No"};
        int response = JOptionPane.showOptionDialog(this,
                "Would you like to load a saved quiz with saved questions?",
                "Load Quiz", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (response == 0) {
            loadQuestions();
        }

        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(this,
                "Select difficulty level:", "Difficulty Selection",
                JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);

        if (selectedDifficulty != null) {
            startQuiz(selectedDifficulty);
        }
    }

    // EFFECTS: Sets up the quiz panel for displaying questions and answers
    @SuppressWarnings("methodlength")
    public void setupQuizPanel() {
        JPanel quizPanel = new JPanel(new BorderLayout(10, 10));
        quizPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.CENTER);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            final int index = i;
            optionButtons[i].addActionListener(e -> selectedOption = index);
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        submitAnswerButton = createButton("Submit Answer", e -> submitAnswer());
        returnToMenuButton = createButton("Return to Menu", e -> cardLayout.show(mainPanel, "MainMenu"));
        buttonPanel.add(submitAnswerButton);
        buttonPanel.add(returnToMenuButton);

        quizPanel.add(questionPanel, BorderLayout.NORTH);
        quizPanel.add(optionsPanel, BorderLayout.CENTER);
        quizPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(quizPanel, "Quiz");
    }

    // EFFECTS: Sets up the panel for adding new questions
    @SuppressWarnings("methodlength")
    public void setupAddQuestionPanel() {
        JPanel addQuestionPanel = new JPanel(new BorderLayout(10, 10));
        addQuestionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add New Question", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        formPanel.add(new JLabel("Question:"));
        questionTextField = new JTextField();
        formPanel.add(questionTextField);

        optionTextFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            formPanel.add(new JLabel("Option " + i + ":"));
            optionTextFields[i] = new JTextField();
            formPanel.add(optionTextFields[i]);
        }

        formPanel.add(new JLabel("Correct Answer Index (0-3):"));
        correctAnswerTextField = new JTextField();
        formPanel.add(correctAnswerTextField);

        formPanel.add(new JLabel("Difficulty Level:"));
        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        difficultyComboBox = new JComboBox<>(difficulties);
        formPanel.add(difficultyComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = createButton("Add Question", e -> addQuestion());
        JButton cancelButton = createButton("Return to Menu", e -> cardLayout.show(mainPanel, "MainMenu"));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        addQuestionPanel.add(titleLabel, BorderLayout.NORTH);
        addQuestionPanel.add(formPanel, BorderLayout.CENTER);
        addQuestionPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(addQuestionPanel, "AddQuestion");
    }

    // EFFECTS: Sets up the panel for displaying the leaderboard
    public void setupLeaderboardPanel() {
        JPanel leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPodiumPanel = new LeaderboardPodiumPanel(leaderboard);
        
        JButton backButton = createButton("Return to Menu", e -> cardLayout.show(mainPanel, "MainMenu"));
        
        leaderboardPanel.add(leaderboardPodiumPanel, BorderLayout.CENTER);
        leaderboardPanel.add(backButton, BorderLayout.SOUTH);
        
        mainPanel.add(leaderboardPanel, "Leaderboard");
    }

    // EFFECTS: Starts a quiz with the selected difficulty
    // MODIFIES: this
    @SuppressWarnings("methodlength")
    public void startQuiz(String difficulty) {
        ArrayList<Question> selectedPool;
        
        switch (difficulty) {
            case "EASY":
                selectedPool = new ArrayList<>(easyQuiz.getQuestions());
                break;
            case "MEDIUM":
                selectedPool = new ArrayList<>(mediumQuiz.getQuestions());
                break;
            case "HARD":
                selectedPool = new ArrayList<>(hardQuiz.getQuestions());
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid difficulty selection.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }
        
        quiz = new Quiz(difficulty, selectedPool);
        if (quiz.getQuestions().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available for this difficulty.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        updateQuizDisplay();
        cardLayout.show(mainPanel, "Quiz");
    }

    // EFFECTS: Updates the quiz display with the current question
    public void updateQuizDisplay() {
        if (quiz.hasNextQuestion()) {
            Question currentQuestion = quiz.getQuestions().get(quiz.getIndex());
            questionLabel.setText("<html><div style='text-align: center;'>" 
                    + currentQuestion.getQuestion() + "</div></html>");

            List<String> options = currentQuestion.getOptions();
            for (int i = 0; i < optionButtons.length; i++) {
                if (i < options.size()) {
                    optionButtons[i].setText(options.get(i));
                    optionButtons[i].setVisible(true);
                } else {
                    optionButtons[i].setVisible(false);
                }
            }

            optionGroup.clearSelection();
            selectedOption = -1;
        } else {
            showQuizResults();
        }
    }

    // EFFECTS: Submits the selected answer and updates the quiz
    public void submitAnswer() {
        if (selectedOption == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = quiz.answerQuestion(selectedOption);
        JOptionPane.showMessageDialog(this, result, "Result", 
                result.equals("Correct!") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        
        if (quiz.hasNextQuestion()) {
            updateQuizDisplay();
        } else {
            showQuizResults();
        }
    }

    // EFFECTS: Shows the quiz results and updates the leaderboard
    public void showQuizResults() {
        leaderboard.updateHighScore(quiz.getDifficultyLevel(), quiz.getCurrentScore());
        JOptionPane.showMessageDialog(this, "Quiz Over!\nFinal Score: " 
                + quiz.getCurrentScore() + "/" + quiz.getQuestions().size(), 
                "Quiz Results", JOptionPane.INFORMATION_MESSAGE);
        
        showQuizReview();
        cardLayout.show(mainPanel, "MainMenu");
    }

    // EFFECTS: Shows a dialog with the quiz review
    public void showQuizReview() {
        List<String> review = quiz.reviewQuiz();
        List<String> userAnswers = quiz.getAnswers();
        
        StringBuilder reviewText = new StringBuilder("Quiz Review:\n\n");
        for (int i = 0; i < review.size() / 2; i++) {
            reviewText.append(review.get(i * 2)).append("\n");
            reviewText.append("Your Answer: ").append(userAnswers.get(i)).append("\n");
            reviewText.append(review.get(i * 2 + 1)).append("\n\n");
        }
        
        JTextArea textArea = new JTextArea(reviewText.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Quiz Review", JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: Adds a new question to the selected difficulty pool
    // MODIFIES: this
    public void addQuestion() {
        try {
            String questionText = questionTextField.getText();
            if (questionText.isEmpty()) {
                showErrorMessage("Question text cannot be empty.");
                return;
            }

            List<String> options = getOptions();
            if (options == null) {
                return; 
            }

            int correctAnswerIndex = validateCorrectAnswerIndex();
            if (correctAnswerIndex == -1) {
                return; 
            }

            addQuestionToPool(questionText, options, correctAnswerIndex);
            postAddQuestionActions();
            
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter a valid number for the correct answer index.");
        } catch (Exception e) {
            showErrorMessage("Error adding question: " + e.getMessage());
        }
    }

    // EFFECTS: Retrieves and validates options entered by the user
    public List<String> getOptions() {
        List<String> options = new ArrayList<>();
        for (JTextField field : optionTextFields) {
            String option = field.getText();
            if (option.isEmpty()) {
                showErrorMessage("All options must be filled.");
                return null;
            }
            options.add(option);
        }
        return options;
    }

    // EFFECTS: Validates the correct answer index returns -1 if invalid
    public int validateCorrectAnswerIndex() {
        int index = Integer.parseInt(correctAnswerTextField.getText());
        if (index < 0 || index >= 4) {
            showErrorMessage("Correct answer index must be between 0 and 3.");
            return -1;
        }
        return index;
    }

    // EFFECTS: Adds the question to the appropriate difficulty pool
    // MODIFIES: this
    public void addQuestionToPool(String questionText, List<String> options, int correctAnswerIndex) {
        String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
        Question newQuestion = new Question(questionText, options.get(correctAnswerIndex), 
                new ArrayList<>(options));

        switch (selectedDifficulty) {
            case "EASY":
                easyQuiz.addQuestion(newQuestion);
                break;
            case "MEDIUM":
                mediumQuiz.addQuestion(newQuestion);
                break;
            case "HARD":
                hardQuiz.addQuestion(newQuestion);
                break;
            default:
                showErrorMessage("Invalid difficulty selection.");
        }
    }

    // EFFECTS: Handles post-question addition actions like clearing the form and saving
    // MODIFIES: this
    public void postAddQuestionActions() {
        clearAddQuestionForm();
        JOptionPane.showMessageDialog(this, "Question added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        int saveResponse = JOptionPane.showConfirmDialog(this, 
                "Would you like to save the updated quiz?", "Save Quiz", JOptionPane.YES_NO_OPTION);
        if (saveResponse == JOptionPane.YES_OPTION) {
            saveQuestions((String) difficultyComboBox.getSelectedItem());
        }
    }

    // EFFECTS: Displays an error message dialog
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: Clears the add question form
    public void clearAddQuestionForm() {
        questionTextField.setText("");
        for (JTextField field : optionTextFields) {
            field.setText("");
        }
        correctAnswerTextField.setText("");
    }

    // EFFECTS: Converts difficulty string to numeric choice
    public int getDifficultyChoice(String difficulty) {
        switch (difficulty) {
            case "EASY": return 0;
            case "MEDIUM": return 1;
            case "HARD": return 2;
            default: return -1;
        }
    }

    // EFFECTS: Shows a dialog to load a specific question set
    public void loadQuestions() {
        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(this,
                "Select difficulty to load:", "Load Questions",
                JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);
        
        if (selectedDifficulty != null) {
            loadQuestionSet(selectedDifficulty);
        }
    }

    // EFFECTS: Loads the question set for the selected difficulty
    // MODIFIES: this
    @SuppressWarnings("methodlength")
    public void loadQuestionSet(String difficulty) {
        try {
            JsonReaderQuiz reader;
            Quiz targetQuiz;
            
            switch (difficulty) {
                case "EASY":
                    reader = quizEasyReader;
                    targetQuiz = reader.read();
                    easyQuiz = targetQuiz;
                    break;
                case "MEDIUM":
                    reader = quizMediumReader;
                    targetQuiz = reader.read();
                    mediumQuiz = targetQuiz;
                    break;
                case "HARD":
                    reader = quizHardReader;
                    targetQuiz = reader.read();
                    hardQuiz = targetQuiz;
                    break;
                default:
                    showErrorMessage("Invalid difficulty selection.");
                    return;
            }
            JOptionPane.showMessageDialog(this, difficulty + " question set loaded successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showErrorMessage("Error loading " + difficulty.toLowerCase() + " question set.");
        }
    }

    // EFFECTS: Saves the question sets to JSON
    @SuppressWarnings("methodlength")
    public void saveQuestions(String difficulty) {
        try {
            JsonWriterQuiz writer;
            Quiz quizToSave;
            
            switch (difficulty) {
                case "EASY":
                    writer = quizEasyWriter;
                    quizToSave = easyQuiz;
                    break;
                case "MEDIUM":
                    writer = quizMediumWriter;
                    quizToSave = mediumQuiz;
                    break;
                case "HARD":
                    writer = quizHardWriter;
                    quizToSave = hardQuiz;
                    break;
                default:
                    showErrorMessage("Invalid difficulty selection.");
                    return;
            }
            
            writer.open();
            writer.write(quizToSave);
            writer.close();
            
            JOptionPane.showMessageDialog(this, 
                    "Questions saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showErrorMessage("Error saving question set.");
        }
    }

    // EFFECTS: Saves the leaderboard to JSON
    public void saveLeaderboard() {
        try {
            leaderboardWriter.open();
            leaderboardWriter.write(leaderboard);
            leaderboardWriter.close();
            JOptionPane.showMessageDialog(this, "Leaderboard saved successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving leaderboard: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: Loads the leaderboard from JSON
    // MODIFIES: this
    public void loadLeaderboard() {
        try {
            leaderboard = leaderboardReader.read();
            if (leaderboardPodiumPanel != null) {
                leaderboardPodiumPanel.updateLeaderboard(leaderboard);
            }
            JOptionPane.showMessageDialog(this, "Leaderboard loaded successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading leaderboard: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: Initializes the default set of easy difficulty questions
    // MODIFIES: this
    @SuppressWarnings("methodlength")
    public void initializeDefaultQuestionsEasy() {
        ArrayList<String> options1 = new ArrayList<>();
        options1.add("Scripting");
        options1.add("Functional");
        options1.add("Object Oriented");
        options1.add("Procedural");

        Question q1 = new Question("What type of programming language is Java?", 
                "Object Oriented", options1);
        easyQuiz.addQuestion(q1);

        ArrayList<String> options2 = new ArrayList<>();
        options2.add("1991");
        options2.add("1995");
        options2.add("2000");
        options2.add("1985");

        Question q2 = new Question("In what year was Java officially released?", 
                "1995", options2);
        easyQuiz.addQuestion(q2);

        ArrayList<String> options3 = new ArrayList<>();
        options3.add("JVM");
        options3.add("JRE");
        options3.add("JDK");
        options3.add("JAR");

        Question q3 = new Question("Which component of Java is responsible for running bytecode?", 
                "JVM", options3);
        easyQuiz.addQuestion(q3);
    }

    // EFFECTS: Initializes the default set of medium difficulty questions
    // MODIFIES: this
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

        Question q1 = new Question("Which data type is used to store text in Java?", "String", options1);
        mediumQuiz.addQuestion(q1);
        Question q2 = new Question("Where are objects stored in memory?", "Heap Memory", options2);
        mediumQuiz.addQuestion(q2);
        Question q3 = new Question("Which loop guarantees at least one execution?", "do-while", options3);
        mediumQuiz.addQuestion(q3);
    }

    // EFFECTS: Initializes the default set of hard difficulty questions
    // MODIFIES: this
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

        Question q1 = new Question("Which OOP principle allows method overriding?", "Polymorphism", options1);
        hardQuiz.addQuestion(q1);
        Question q2 = new Question("Which data structure maintains insertion order?", "LinkedList", options2);
        hardQuiz.addQuestion(q2);
        Question q3 = new Question("Which keyword prevents thread interference?", "synchronized", options3);
        hardQuiz.addQuestion(q3);
    }

    // EFFECTS: Main entry point for the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JavaChampAppGUI());
    }
}