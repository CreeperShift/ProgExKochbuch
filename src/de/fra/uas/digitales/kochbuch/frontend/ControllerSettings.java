package de.fra.uas.digitales.kochbuch.frontend;



import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ControllerSettings {
    @FXML
    public TextField pass;
    public TextField root;
    public TextField port;
    public TextField address;
    public Label passL;
    public Label rootL;
    public Label portL;
    public Label addressL;

    public void btnZurucksetting(ActionEvent actionEvent) {

        Main.controllerBase.btnStart.fire();

        //Main.controllerBase.btnStart.fire();

    }

    public void btnUbernehmen(ActionEvent actionEvent) {

pass.setText(pass.getText());
address.setText(address.getText());
port.setText(port.getText());
root.setText(root.getText());

        Main.controllerBase.btnStart.fire();


    }

    private void clearButtons() {
    }


}
