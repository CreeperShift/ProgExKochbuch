package de.fra.uas.digitales.kochbuch.frontend;


import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class ControllerSettings implements Initializable {

    public Circle led;
    public Label statustext;
    public TextField fieldPort;
    public TextField fieldAddress;
    public PasswordField fieldPassword;
    public TextField fieldUsername;
    public Button btnBack;
    public AnchorPane topAnchor;
    public TextField fieldDatabaseName;
    private boolean isConnected = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        topAnchor.getChildren().remove(btnBack);
        statustext.setText("");

    }

    public void setAlreadyConnected(){
        led.setFill(Color.GREEN);
        statustext.setText("Verbunden");
    }

    public boolean isConnected() {
        return isConnected;
    }


    private void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void onBtnConnect(ActionEvent actionEvent) throws SQLException {
        try {
            DataManager.setDatabase("jdbc:mysql://" + fieldAddress.getText() + ":" + fieldPort.getText() + "/" + fieldDatabaseName.getText(), fieldUsername.getText(), fieldPassword.getText());
            DataManager.get().connect();
        } catch (Exception e) {
            statustext.setText("Fehler beim Verbinden, bitte Daten überprüfen!");
            isConnected = false;
            led.setFill(Color.RED);
            Main.controllerBase.initButtons(true);
            topAnchor.getChildren().remove(btnBack);
            DataManager.get().stopConnection();
        }
        if (DataManager.get().isConnected()) {

            Path path = Paths.get("Database.info");
            String status = "Verbunden, Daten wurden gespeichert.";
            byte[] toWrite = ("jdbc:mysql://" + fieldAddress.getText() + ":" + fieldPort.getText() + "/" + fieldDatabaseName.getText() + "\n" + fieldUsername.getText() + "\n" + fieldPassword.getText()).getBytes();
            try {
                Files.write(path, toWrite);
            } catch (IOException e) {
                e.printStackTrace();
                status = "Verbunden, Fehler beim schreiben der Datei.";
            }


            statustext.setText(status);
            led.setFill(Color.GREEN);
            Main.controllerBase.initButtons(false);
            isConnected = true;
            if (!topAnchor.getChildren().contains(btnBack)) {
                topAnchor.getChildren().add(btnBack);
            }
            Main.controllerStartLayout.startConnected();
            Main.controllerFilter.startConnected();
            Main.controllerNewRecipe.onConnected();
            Main.controllerBase.btnStart.fire();
        }


    }


    public void onBtnBack(ActionEvent actionEvent) {
        Main.controllerBase.btnStart.fire();
    }
}
