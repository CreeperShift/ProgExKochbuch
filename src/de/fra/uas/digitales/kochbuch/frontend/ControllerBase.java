package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerBase implements Initializable {

    public Button btnStart;
    public Button btnRezept;
    public Button btnFilter;

    public Button[] navButtons;
    public Button btnSettings;
    public static boolean isConnected = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navButtons = new Button[]{btnStart, btnRezept, btnFilter, btnSettings};
        setButtonActive(btnStart);
    }

    public void onBtnStart(ActionEvent actionEvent) {
        clearButtons();
        Main.mainPanel.setCenter(Main.startPane);
        setButtonActive(btnStart);
    }

    public void onBtnRezept(ActionEvent actionEvent) {
        clearButtons();
        Main.mainPanel.setCenter(Main.newRecipePage);
        Main.newRecipePage.prefWidthProperty().bind(Main.mainPanel.widthProperty().subtract(200));
        Main.newRecipePage.prefHeightProperty().bind(Main.mainPanel.heightProperty().subtract(50)); //TODO: This is messy
        setButtonActive(btnRezept);
    }

    public void onBtnFilter(ActionEvent actionEvent) {
        clearButtons();
        Main.mainPanel.setCenter(Main.filterPage);
        Main.filterPage.prefWidthProperty().bind(Main.mainPanel.widthProperty().subtract(200));
        Main.filterPage.prefHeightProperty().bind(Main.mainPanel.heightProperty().subtract(50)); //TODO: This is messy
        setButtonActive(btnFilter);
    }

    public void clearButtons() {
        Arrays.stream(navButtons).forEach(b -> b.getStyleClass().remove("navbuttonActive"));
    }

    private void setButtonActive(Button button) {
        button.getStyleClass().add("navbuttonActive");
    }

    public void onBtnSettings(ActionEvent actionEvent) {
        clearButtons();
        Main.mainPanel.setCenter(Main.settingsPage);
        Main.settingsPage.prefWidthProperty().bind(Main.mainPanel.widthProperty().subtract(200));
        Main.settingsPage.prefHeightProperty().bind(Main.mainPanel.heightProperty().subtract(50)); //TODO: This is messy
        setButtonActive(btnSettings);
        if (DataManager.get().isConnected()) {
            Main.controllerSettings.setAlreadyConnected();
        }
        if (!DataManager.get().isConnected()) {
            initButtons(true);
        }
    }

    public void initButtons(boolean disabled) {
        btnStart.setDisable(disabled);
        btnFilter.setDisable(disabled);
        btnRezept.setDisable(disabled);
    }
}
