package main.java.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import main.java.Core.main_dict.Word;
import main.java.Core.main_dict.WordsManager;
import main.java.Core.main_dict.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

public class MenuController implements Initializable {
    @FXML
    private AnchorPane container;

    @FXML
    private Button dictionaryBtn;

    @FXML
    private Button gameBtn;

    @FXML
    private Button translateBtn;

    @FXML
    ChoiceBox<String> gameChoiceBox;

    String[] game = { "Flashcard", "MCQ", "Wordament" };

    private void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }

    @FXML
    private void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(getClass().getResource(path));
            setNode(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param event
     * @throws IOException
     */
    public void switchToDictionary(ActionEvent e) throws IOException {
        showComponent("../../resources/assets/DictionaryUI.fxml");
    }

    /**
     * @param event
     * @throws IOException
     */
    public void switchToTranslate(ActionEvent e) throws IOException {
        showComponent("../../resources/assets/TranslateUI.fxml");
    }

    /**
     * @param event
     * @throws IOException
     */
    public void switchToGame(ActionEvent e) throws IOException {
        String path = "../../resources/assets/" + gameChoiceBox.getValue() + "UI.fxml";
        showComponent(path);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // System.out.println(getClass().getResource("src/main/resources/EV.txt"));
        DBManager.scan("/src/main/resources/EV.txt");
        // System.out.println(getClass().getResource("../resources/assets/DictionaryUI.fxml"));
        showComponent("../../resources/assets/DictionaryUI.fxml");
        gameChoiceBox.getItems().addAll(game);
        gameChoiceBox.setValue(game[0]);
    }
}
