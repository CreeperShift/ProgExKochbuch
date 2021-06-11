package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerBase implements Initializable {

    public Button btnStart;
    public Button btnRezept;
    public Button btnZutat;
    public Button btnSuche;
    public Button btnFilter;

    public Button[] navButtons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStart.setStyle("-fx-background-color: gray");
        navButtons = new Button[]{btnStart, btnRezept, btnZutat, btnSuche, btnFilter};
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

    public void onBtnZutat(ActionEvent actionEvent) {
        clearButtons();
        setButtonActive(btnZutat);
    }

    public void onBtnSuche(ActionEvent actionEvent) {
        clearButtons();
        setButtonActive(btnSuche);
    }

    public void onBtnFilter(ActionEvent actionEvent) {
        clearButtons();
        setButtonActive(btnFilter);
    }

    private void clearButtons() {
        Arrays.stream(navButtons).forEach(b -> b.setStyle(null));
    }

    private void setButtonActive(Button button) {
        button.setStyle("-fx-background-color: gray");
    }

}
