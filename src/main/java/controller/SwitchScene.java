package main.java.controller;

import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SwitchScene implements Initializable{
    @FXML
    private AnchorPane container;

    @FXML
    private Button dictionaryBtn;

    @FXML
    private Tooltip dictionaryTooltip;

    @FXML
    private Button translateBtn;

    @FXML
    private Tooltip translateTooltip;

    @FXML
    private Button flashcardBtn;

    @FXML
    private Tooltip flashcardTooltip;

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
        showComponent("../resources/assets/Dictionary.fxml");
    }

    /**
     * @param event
     * @throws IOException
     */
    public void switchToTranslate(ActionEvent e) throws IOException {
        showComponent("../resources/assets/Translate.fxml");
    }

    /**
     * @param event
     * @throws IOException
     */
    public void switchToFlashcard(ActionEvent e) throws IOException {
        showComponent("../resources/assets/Flashcard.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showComponent("../../resources/assets/Dictionary.fxml");
        dictionaryTooltip.setShowDelay(Duration.seconds(0.5));
        translateTooltip.setShowDelay(Duration.seconds(0.5));
        flashcardTooltip.setShowDelay(Duration.seconds(0.5));
    }

}
