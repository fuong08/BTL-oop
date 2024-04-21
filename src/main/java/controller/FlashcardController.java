package main.java.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import main.java.Core.Flashcard.*;
import javafx.util.Duration;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;

public class FlashcardController extends GameManage {

    @FXML
    private ListView<String> listview;
    private List<Flashcard> flashcards = new ArrayList<>();
    //private FlashcardData flashcardData;
    private int currentCardIndex = 0;
    private boolean check = true;
    private boolean showQuestion = true;
    @FXML
    private Label flashcardLabel;
    @FXML
    private AnchorPane helpscene;

    public FlashcardController() {
        flashcardData = new FlashcardData("./src/main/resources/FlashcardData.txt");
        flashcards = flashcardData.getFlashcards();
    }

    @FXML
    void initialize() {
        helpscene.setVisible(false);
        ObservableList<String> questionItems = listview.getItems();
        for (Flashcard flashcard : flashcards) {
            questionItems.add(flashcard.getQuestion());
        }
        listview.setItems(questionItems);
        listview.requestFocus();
        listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedCardIndex = questionItems.indexOf(newValue);
                if (selectedCardIndex >= 0 && selectedCardIndex < flashcards.size()) {
                    currentCardIndex = selectedCardIndex;
                    flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());
                }
            }
        });

        if (!flashcards.isEmpty()) {
            flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());
        } else {
            flashcardLabel.setText("No flashcards available");
        }
    }

    @FXML
    void addCard(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Flashcard");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter a new flashcard question:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(question -> {
            TextInputDialog answerDialog = new TextInputDialog();
            answerDialog.setTitle("Add New Flashcard");
            answerDialog.setHeaderText(null);
            answerDialog.setContentText("Enter the answer for the flashcard:");

            Optional<String> answerResult = answerDialog.showAndWait();
            answerResult.ifPresent(answer -> {
                Flashcard newFlashcard = new Flashcard(question, answer);
                flashcardData.addFlashcard(newFlashcard);
                // Sort the flashcard questions and update the ListView
                ObservableList<String> questionItems = listview.getItems();
                questionItems.add(newFlashcard.getQuestion());
                FXCollections.sort(questionItems);
                listview.setItems(questionItems);
            });
        });
    }

    @FXML
    void deleteCard(ActionEvent event) {
        if (currentCardIndex >= 0 && currentCardIndex < flashcards.size()) {
            flashcards.remove(currentCardIndex);
            flashcardData.saveFlashcards(flashcards);

            if (flashcards.isEmpty()) {
                flashcardLabel.setText("No flashcards available");
            } else {
                ObservableList<String> questionItems = listview.getItems();
                questionItems.remove(currentCardIndex);
                listview.setItems(questionItems);

                if (currentCardIndex >= flashcards.size()) {
                    currentCardIndex = flashcards.size() - 1;
                }
                flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());
            }
        } else {
            System.out.println("No flashcard selected for deletion");
        }
    }

    @FXML
    void editCard(ActionEvent event) {
        if (currentCardIndex >= 0 && currentCardIndex < flashcards.size()) {
            Flashcard curr = flashcards.get(currentCardIndex);
            TextInputDialog questionDialog = new TextInputDialog(curr.getQuestion());
            questionDialog.setTitle("Edit Flashcard");
            questionDialog.setHeaderText(null);
            questionDialog.setContentText("Edit the question:");

            Optional<String> questionResult = questionDialog.showAndWait();
            questionResult.ifPresent(newQuestion -> {
                TextInputDialog answerDialog = new TextInputDialog(curr.getAnswer());
                answerDialog.setTitle("Edit Flashcard");
                answerDialog.setHeaderText(null);
                answerDialog.setContentText("Edit the answer:");

                Optional<String> answerResult = answerDialog.showAndWait();
                answerResult.ifPresent(newAnswer -> {
                    curr.setQuestion(newQuestion);
                    curr.setAnswer(newAnswer);
                    // Sort the flashcards by question
                    flashcards.sort(Comparator.comparing(Flashcard::getQuestion));
                    // Update the ListView with the sorted data
                    listview.getItems()
                            .setAll(flashcards.stream().map(Flashcard::getQuestion).collect(Collectors.toList()));

                    flashcardData.saveFlashcards(flashcards);
                    flashcardLabel.setText(newQuestion);
                });
            });
        } else {
            System.out.println("No flashcard selected for editing.");
        }
    }

    @FXML
    void help(ActionEvent event) {
        helpscene.setVisible(true);
    }

    @FXML
    void onclose(ActionEvent event) {
        helpscene.setVisible(false);
    }

    @FXML
    void show(KeyEvent event) {
        if (event.getCode() == KeyCode.D) {
            nextCard();
        } else if (event.getCode() == KeyCode.A) {
            prevCard();
        } else if (event.getCode() == KeyCode.S) {
            turnAround();
            toggleLabelContent();
            check = !check;
        } else if (event.getCode() == KeyCode.R) {
            restartCard();
        } else if (event.getCode() == KeyCode.W) {
            shuffleCards();
        }
    }

    public void nextCard() {
        if (!flashcards.isEmpty()) {
            currentCardIndex = (currentCardIndex + 1) % flashcards.size();
            flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());
        }
    }

    public void prevCard() {
        if (!flashcards.isEmpty()) {
            currentCardIndex = (currentCardIndex - 1 + flashcards.size()) % flashcards.size();
            flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());
        }
    }

    public void showQuestion() {
        if (!flashcards.isEmpty()) {
            flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());
        }
    }

    public void showAnswer() {
        if (!flashcards.isEmpty()) {
            flashcardLabel.setText(flashcards.get(currentCardIndex).getAnswer());
        }
    }

    private void shuffleCards() {
        Random random = new Random();
        if (!flashcards.isEmpty()) {
            int index = random.nextInt(flashcards.size());
            currentCardIndex = index;
        }
        flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());

    }

    private void restartCard() {
        currentCardIndex = 0;
        flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());
    }

    @FXML
    void toggleLabelContent(MouseEvent event) {
        if (showQuestion) {
            turnAround();
            showAnswer();
        } else {
            turnAround();
            showQuestion();
        }
        showQuestion = !showQuestion;
    }

    void toggleLabelContent() {
        if (showQuestion) {
            turnAround();
            showAnswer();
        } else {
            turnAround();
            showQuestion();
        }
        showQuestion = !showQuestion;
    }

    @FXML
    void handleListViewItemClick(MouseEvent event) {
        int selectedIndex = listview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            currentCardIndex = selectedIndex;
            flashcardLabel.setText(flashcards.get(currentCardIndex).getQuestion());
        }
    }

    public RotateTransition flip(Label label) {
        RotateTransition rotator = new RotateTransition(Duration.millis(500), label);
        rotator.setAxis(Rotate.Y_AXIS);
        if (check) {
            rotator.setFromAngle(0);
            rotator.setToAngle(360);
        } else {
            rotator.setFromAngle(360);
            rotator.setToAngle(0);
        }
        return rotator;
    }

    public PauseTransition changeCardFace(Label label) {
        PauseTransition pause = new PauseTransition(Duration.millis(500));
        return pause;
    }

    public void turnAround() {
        RotateTransition rotator = flip(flashcardLabel);
        PauseTransition pausetransition = changeCardFace(flashcardLabel);
        ParallelTransition paralleltransition = new ParallelTransition(rotator, pausetransition);
        paralleltransition.play();
        check = !check;
    }
}
