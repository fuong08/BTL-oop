package main.java.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import main.java.Core.API.VoiceRSS;
import main.java.Core.main_dict.DBManager;
import main.java.Core.API.APITranslator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class TranslateController extends Controller implements Initializable {
    private static final String[] languageChoices = { "Tiếng Anh", "Tiếng Việt", "Tiếng Hàn",
            "Tiếng Nhật", "Tiếng Nga" };

    @FXML
    private ChoiceBox<String> languageFrom;

    @FXML
    private ChoiceBox<String> languageTo;

    @FXML
    private Label resultPane;

    @FXML
    private TextArea writePane;

    @FXML
    private ImageView soundBtn1;

    @FXML
    private ImageView soundBtn2;

    // Set UTF-8 encoding
    static {
        System.setProperty("file.encoding", "UTF-8");
    }

    @FXML
    void HandleClickTranslateBtn(ActionEvent event) throws IOException {
        String text = writePane.getText();
        String langFrom = languageFrom.getValue();
        String langTo = languageTo.getValue();
        System.out.println(langFrom + ' ' + langTo + ' ' + text);
        resultPane.setText(APITranslator.translate(langFrom, langTo, text));
    }

    private void playHitSound() {
        String path = "src/main/resources/Audio/output.mp3";
        Media media;
        MediaPlayer mediaPlayer;
        // String path = getClass().getResource("output.mp3").getPath();
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(1);
        mediaPlayer.play();
    }

    @FXML
    void handleSoundBtn1(ActionEvent event) throws Exception {
        // String text = writePane.getText();
        // String language = languageFrom.getValue();
        // VoiceRSS.audioFilePath(text, language);
        // playHitSound();
        // System.out.println("end1");
        Thread speakFromThread = new Thread(() -> {
            try {
                String text = writePane.getText();
                String language = languageFrom.getValue();
                VoiceRSS.audioFilePath(text, language);
                playHitSound();
                System.out.println("end1");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        speakFromThread.start();
        String text = writePane.getText();
        String language = languageFrom.getValue();
        VoiceRSS.audioFilePath(text, language);
        playHitSound();
        System.out.println("end1");
    }

    @FXML
    void handleSoundBtn2(ActionEvent event) throws Exception {
        String text = resultPane.getText();
        String language = languageTo.getValue();
        VoiceRSS.audioFilePath(text, language);
        playHitSound();
        System.out.println("end2");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBManager.export();
        languageFrom.getItems().addAll(languageChoices);
        languageTo.getItems().addAll(languageChoices);
        languageFrom.setValue("Tiếng Anh");
        languageTo.setValue("Tiếng Việt");
    }
}
