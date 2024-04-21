package main.java.controller;

import main.java.Core.Wordament.Wordament;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

public class WordamentController extends GameManage {
    @FXML
    private Button checkButton;
    @FXML
    private Button nextButton;

    @FXML
    private Button playAgain;

    @FXML
    private Label wordLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private TextField inputField;

    private String filePath = "./src/main/resources/FlashcardData.txt";
    private List<Wordament> words = new ArrayList<>();
    private int currentWordIndex = 0;
    private Wordament currentWord;
    private int score = 0;
    private int lives = 3;
    private int highestScore;
    private Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

    public void initialize() {
        playAgain.setVisible(false);
        nextButton.setDisable(true);
        loadWordsFromFile(filePath);
        Collections.shuffle(words);
        currentWord = words.get(currentWordIndex);
        update();
        highestScore = preferences.getInt("highestScore", 0);
        scoreLabel.setText("Score: " + score + " Lives: " + lives + " Highest Score: " + highestScore);
        scoreLabel.getStyleClass().add("scorelabel");
        messageLabel.setText("Congratulations!\nWord: " + currentWord.getWord() + "\nMeaning: " + currentWord.getMeaning());
        messageLabel.getStyleClass().add("messagelabel");
        messageLabel.setVisible(false);
    }

    private void loadWordsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String word = parts[0].trim();
                    String meaning = parts[1].trim();
                    words.add(new Wordament(word, meaning));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        wordLabel.setText(shuffleUntilDifferent(currentWord.getWord()));
        inputField.clear();
    }

    @FXML
    private void checkWord() {
        String userInput = inputField.getText().trim();
        if (userInput.equalsIgnoreCase(currentWord.getWord())) {
            inputField.clear();
            showMeaning();
        } else {
            nextButton.setDisable(true);
            inputField.clear();
            lives--;
            updateScoreLabel();
            if (lives <= 0) {
                gameover();
            }
        }
    }

    private void showMeaning() {
        checkButton.setDisable(true);
        messageLabel.setVisible(true);
        nextButton.setDisable(false);
        messageLabel
                .setText("Congratulations!\nWord: " + currentWord.getWord() + "\nMeaning: " + currentWord.getMeaning());
        score += 10;
        if (score > highestScore) {
            highestScore = score;
            preferences.putInt("highestScore", highestScore);
            updateScoreLabel();
        }
        updateScoreLabel();
    }

    @FXML
    private void showNextWord() {
        checkButton.setDisable(false);
        messageLabel.setVisible(false);
        nextButton.setDisable(true);
        currentWordIndex++;
        if (currentWordIndex < words.size()) {
            currentWord = words.get(currentWordIndex);
            update();
        } else {
            if (lives <= 0) {
                gameover();
            }
            if(currentWordIndex == words.size()){
                gameover();
            }
        }
    }

    private void gameover() {
        wordLabel.setText("Game over");
        inputField.setVisible(false);
        checkButton.setVisible(false);
        nextButton.setVisible(false);
        playAgain.setVisible(true);
    }

    private String shuffleWord(String word) {
        List<String> letters = new ArrayList<>();
        for (char letter : word.toCharArray()) {
            letters.add(String.valueOf(letter));
        }
        Collections.shuffle(letters);
        StringBuilder shuffledWord = new StringBuilder();
        for (String letter : letters) {
            shuffledWord.append(letter);
        }
        return shuffledWord.toString();
    }
    public String shuffleUntilDifferent(String word) {
        String shuffledWord = shuffleWord(word);
        while (shuffledWord.equals(word)) {
            shuffledWord = shuffleWord(word);
        }
        return shuffledWord;
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score + " Lives: " + lives + " Highest Score: " + highestScore);
    }
    @FXML
    void again(ActionEvent event) {
        currentWordIndex =0;
        score = 0;
        lives =3;
        updateScoreLabel();
        showNextWord();
        inputField.setVisible(true);
        checkButton.setVisible(true);
        nextButton.setVisible(true);
        playAgain.setVisible(false);
    }
}
