package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFilter implements Initializable {
    public Button btnFind;
    public Button btnFilter;
    public TextField txtFilter;
    public List<Ingredient> ingredients = new LinkedList<>();
    public VBox[] boxes = new VBox[4];
    public VBox box1;
    public VBox box2;
    public VBox box3;
    public VBox box4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxes[0] = box1;
        boxes[1] = box2;
        boxes[2] = box3;
        boxes[3] = box4;

    }

    public void onNewFilter(ActionEvent actionEvent) {
        if (!txtFilter.getText().isBlank() && !isAlreadyFilter(txtFilter.getText())) {

            addIngredientToBox();
            txtFilter.clear();
        }
    }

    private boolean isAlreadyFilter(String text) {
        for (Ingredient i : ingredients) {
            return i.name().equalsIgnoreCase(text);
        }
        return false;
    }

    private void addIngredientToBox() {
        HBox box = new HBox();
        TextField text = new TextField(txtFilter.getText());
        text.setEditable(false);
        Button button = new Button("X");
        box.getChildren().add(text);
        box.getChildren().add(button);
        int whichBox = ingredients.size() % boxes.length;
        boxes[whichBox].getChildren().add(box);
        Ingredient i = new Ingredient(txtFilter.getText(), 0, "");
        ingredients.add(i);

        button.setOnAction(actionEvent1 -> {
            boxes[whichBox].getChildren().remove(box);
            ingredients.remove(i);
        });

    }

    public void onBtnFind(ActionEvent actionEvent) {
    }
}
