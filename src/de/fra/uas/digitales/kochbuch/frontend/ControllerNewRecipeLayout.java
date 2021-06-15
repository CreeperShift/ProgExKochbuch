package de.fra.uas.digitales.kochbuch.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerNewRecipeLayout implements Initializable {
    public TextArea recipeSteps;
    public TextArea recipeDesc;
    public TextField recipeName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onSave(ActionEvent actionEvent) {

        if (!recipeDesc.getText().isBlank()) {
            if (!recipeName.getText().isBlank()) {
                if (!recipeSteps.getText().isBlank()) {

                }
            }
        }

    }
}
