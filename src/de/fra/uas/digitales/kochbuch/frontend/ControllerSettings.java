package de.fra.uas.digitales.kochbuch.frontend;


import de.fra.uas.digitales.kochbuch.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerSettings implements Initializable {

    public Circle led;
    public Label statustext;
    public TextField fieldPort;
    public TextField fieldAddress;
    public TextField fieldPassword;
    public TextField fieldUsername;
    private boolean isConnected = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        


    }

    public boolean isConnected() {
        return isConnected;
    }


    private void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void btnZurucksetting(ActionEvent actionEvent) {

        Main.controllerBase.btnStart.fire();

    }


    public void onInfoChanged(ActionEvent actionEvent) {
        System.out.println("test");

    }

    public void onBtnConnect(ActionEvent actionEvent) {
    }


}
