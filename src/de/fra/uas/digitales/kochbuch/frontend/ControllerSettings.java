package de.fra.uas.digitales.kochbuch.frontend;


import de.fra.uas.digitales.kochbuch.Main;
import javafx.event.ActionEvent;



public class ControllerSettings {

    public void btnZurucksetting(ActionEvent actionEvent) {
        Main.mainPanel.setCenter(Main.startPane);

    }

    private void clearButtons() {
    }
}
