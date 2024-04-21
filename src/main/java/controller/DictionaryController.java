package main.java.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import main.java.Core.main_dict.Word;
import main.java.Core.main_dict.DBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.Node;

public class DictionaryController extends Controller implements Initializable {
    private static final String VOICE_KEY = "freetts.voices";
    private static final String VOICE_VALUE = "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory";
    private static final String VOICE_NAME = "kevin16";

    @FXML
    private AnchorPane addPane;

    @FXML
    private Label contentLable;

    @FXML
    private Label pronunciationLable;

    @FXML
    private TextField searchBox;

    @FXML
    private Button searchBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button editBtn;

    @FXML
    private ImageView soundBtn;

    @FXML
    private Label typeLable;

    @FXML
    private TextArea meaningArea;

    @FXML
    private TextArea exampleArea;

    @FXML
    private ListView<String> suggestion;

    private String target = "";

    ObservableList<String> list = FXCollections.observableArrayList();

    public void displayContent(Word word) {
        contentLable.setText(word.getContent());
    }

    public void displayType(Word word) {
        typeLable.setText(word.getType());
    }

    public void displayPronunciation(Word word) {
        pronunciationLable.setText(word.getPronunciation());
    }

    public void displayMeaning(Word word) {
        meaningArea.setText(word.getMeaning());
    }

    public void displayExample(Word word) {
        exampleArea.setText(word.getExample());
    }

    private void displayWord(Word word) {
        displayContent(word);
        displayType(word);
        displayPronunciation(word);
        displayMeaning(word);
        displayExample(word);
    }

    private void handleOnKeyTyped(String searchWord) {
        list.clear();
        List<Word> recWordList = DBManager.WM.suggestions(searchWord);

        for (Word word : recWordList) {
            list.add(word.getContent());
        }
        if (list.isEmpty()) {
            // notAvailable.setVisible(true);
            suggestion.setItems(list);
        } else {
            // notAvailable.setVisible(false);
            suggestion.setItems(list);

        }
    }

    /**
     * @param event
     * @throws IOException
     */
    @FXML
    private void HandleSearchBtn(ActionEvent e) throws IOException {
        if (!searchBox.getText().isEmpty()) {
            target = searchBox.getText();
        }
        System.out.println(target);
        Word word = DBManager.WM.searchWord(target);
        System.out.println(word.getMeaning());
        displayWord(word);
    }

    @FXML
    private void HandleMouseClickListView(MouseEvent e) {
        String selectedWord = suggestion.getSelectionModel().getSelectedItem();
        Word word = DBManager.WM.searchWord(selectedWord);
        target = word.getContent();
        displayWord(word);
        // System.out.println(word.getWordTarget() + " " + word.isFavorite());
    }

    @FXML
    private void HandleClickEditBtn(ActionEvent e) {
        meaningArea.setEditable(true);
        exampleArea.setEditable(true);
        editBtn.setVisible(false);
        saveBtn.setVisible(true);
    }

    @FXML
    void HandleMouseClickSoundBtn(MouseEvent event) {
        System.setProperty(VOICE_KEY, VOICE_VALUE);
        Voice voice = VoiceManager.getInstance().getVoice(VOICE_NAME);
        if (voice != null) {
            voice.allocate();
            voice.setRate(100);
            voice.speak(target);
            voice.deallocate();
        } else {
            throw new IllegalStateException("Cannot find voice: " + VOICE_NAME);
        }
    }

    @FXML
    void HandleClickSaveBtn(ActionEvent event) {
        Word word = DBManager.WM.searchWord(target);
        word.setMeaning(meaningArea.getText());
        word.setExample(exampleArea.getText());
        DBManager.WM.updateWord(word);
        meaningArea.setEditable(false);
        exampleArea.setEditable(false);
        saveBtn.setVisible(false);
        editBtn.setVisible(true);
    }

    @FXML
    void HandleClickDeleteBtn(ActionEvent event) {
        Word word = new Word("", "", "", "", "");
        displayWord(word);
        System.out.println("Từ cần xóa là: ");
        System.out.println(target);
        DBManager.deleteWord(target);
        System.out.println("Đã xóa từ");
        handleOnKeyTyped("");
    }

    @FXML
    void HandleClickAddBtn(ActionEvent event) {
        // Dialog<Word> dialog = new Dialog<Word>();
        // Optional<Word> result = dialog.showAndWait();
        // if(result.isPresent()) {
        // Word word = result.get();
        // System.out.println(word.toString());
        // }
        try {
            AnchorPane component = FXMLLoader.load(getClass().getResource("../../resources/assets/test.fxml"));
            addPane.getChildren().add(component);
            Button closeBtn = new Button(" X ");
            addPane.getChildren().add(closeBtn);
            closeBtn.setOnMouseClicked(e -> {
                addPane.getChildren().clear();
                // addPane.setVisible(false);
            });
            handleOnKeyTyped("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        meaningArea.setEditable(false);
        exampleArea.setEditable(false);
        saveBtn.setVisible(false);
        handleOnKeyTyped("");
        searchBox.setOnKeyReleased(e -> {
            if (searchBox.getText().isEmpty()) {
                // setListDefault();
            } else {
                String searchWord = searchBox.getText();
                handleOnKeyTyped(searchWord);
            }
        });
        // DBManager.WM.suggestions("al");
        // System.out.println("hehe");
    }
}
