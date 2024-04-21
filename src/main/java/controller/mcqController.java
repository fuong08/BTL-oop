package main.java.controller;
import main.java.Core.MCQ.Question;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;

public class mcqController extends GameManage {
    @FXML
    private AnchorPane root;

    @FXML
    private Label questionLabel;
    @FXML
    private Label scoreLabel;

    @FXML
    private Button answerButton1;

    @FXML
    private Button answerButton2;

    @FXML
    private Button answerButton3;

    @FXML
    private Button answerButton4;

    @FXML
    private Button playAgain;
    private String filePath = "./src/main/resources/FlashcardData.txt";
    private int score = 0;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int highestScore;
    private Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

    public void initialize() {
        playAgain.setVisible(false);
        questions = loadQuestionsFromFile(filePath);
        highestScore = preferences.getInt("highestScore", 0);
        scoreLabel.setText("Score: " + score + " Highest Score: " + highestScore);
        scoreLabel.getStyleClass().add("scorelabel");
        nextQuestion();
    }

    private List<Question> loadQuestionsFromFile(String filename) {
        List<Question> loadedQuestions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    loadedQuestions.add(new Question(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedQuestions;
    }

    private void nextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getWord());

            List<String> choices = generateRandomChoices(currentQuestion.getMeaning());
            answerButton1.setText(choices.get(0));
            answerButton2.setText(choices.get(1));
            answerButton3.setText(choices.get(2));
            answerButton4.setText(choices.get(3));

            currentQuestionIndex++;
        } else {
            // End of the game
            questionLabel.setText("Game Over! Your Score: " + score);
            answerButton1.setVisible(false);
            answerButton2.setVisible(false);
            answerButton3.setVisible(false);
            answerButton4.setVisible(false);
            playAgain.setVisible(true);
        }
    }

    @FXML
    private void handleAnswer() {
        Button selectedButton = (Button) root.getScene().getFocusOwner();
        Question currentQuestion = questions.get(currentQuestionIndex - 1);

        if (currentQuestion.getMeaning().equals(selectedButton.getText())) {
            selectedButton.getStyleClass().add("correct-answer");
            score++;
            if (score > highestScore) {
                highestScore = score;
                preferences.putInt("highestScore", highestScore);
            }
        } else {
            selectedButton.getStyleClass().add("incorrect-answer");
            List<Button> answerButtons = Arrays.asList(answerButton1, answerButton2, answerButton3, answerButton4);
            for (Button button : answerButtons) {
                if (currentQuestion.getMeaning().equals(button.getText())) {
                    button.getStyleClass().add("correct-answer");
                }
            }
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            updateScoreLabel();
            resetButtonStyles();
            nextQuestion();
        }));
        timeline.play();
    }

    @FXML
    private void again() {
        currentQuestionIndex = 0;
        score = 0;
        updateScoreLabel();
        nextQuestion();
        answerButton1.setVisible(true);
        answerButton2.setVisible(true);
        answerButton3.setVisible(true);
        answerButton4.setVisible(true);
        playAgain.setVisible(false);
    }

    private List<String> generateRandomChoices(String correctAnswer) {
        List<String> choices = new ArrayList<>();
        choices.add(correctAnswer);

        // Generate random incorrect choices
        while (choices.size() < 4) {
            String randomChoice = getRandomMeaning();
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice);
            }
        }

        Collections.shuffle(choices);
        return choices;
    }

    private String getRandomMeaning() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(questions.size());
        return questions.get(randomIndex).getMeaning();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score + " Highest Score: " + highestScore);
    }

    private void resetButtonStyles() {
        answerButton1.getStyleClass().removeAll("correct-answer", "incorrect-answer");
        answerButton2.getStyleClass().removeAll("correct-answer", "incorrect-answer");
        answerButton3.getStyleClass().removeAll("correct-answer", "incorrect-answer");
        answerButton4.getStyleClass().removeAll("correct-answer", "incorrect-answer");
    }
}