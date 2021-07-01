package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.sql.SQLException;
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
    public SearchableComboBox<String> ingredientSearch;
    public GridPane grid;
    public Slider slider;
    public Label ingrAmount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxes[0] = box1;
        boxes[1] = box2;
        boxes[2] = box3;
        boxes[3] = box4;
        try {
            List<String> list = DataManager.get().getAllIngredientNames();

            ingredientSearch.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void onNewFilter(ActionEvent actionEvent) {

        if (ingredientSearch.getValue() != null && canAddFilter()) {
            addIngredientToBox(ingredientSearch.getValue());
            slider.setMax(slider.getMax() + 1);
            slider.setValue(slider.getMax());
        }


    }

    private boolean canAddFilter() {

        if (ingredientSearch.getValue().isBlank()) {
            return false;
        } else if (isAlreadyFilter(ingredientSearch.getValue())) {
            return false;
        } else if (ingredients.size() >= 12) {
            return false;
        }

        return true;
    }


    private boolean isAlreadyFilter(String text) {
        for (Ingredient i : ingredients) {
            if (i.name().equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    private void addIngredientToBox(String ingr) {
        HBox box = new HBox();
        TextField text = new TextField(ingr);
        text.setEditable(false);
        Button button = new Button("X");
        box.getChildren().add(text);
        box.getChildren().add(button);
        int whichBox = ingredients.size() % boxes.length;
        boxes[whichBox].getChildren().add(box);
        Ingredient i = new Ingredient(ingr, 0, "");
        ingredients.add(i);
        ingrAmount.setText(Integer.parseInt(ingrAmount.getText()) + 1 + "");

        button.setOnAction(actionEvent1 -> {
            boxes[whichBox].getChildren().remove(box);
            ingredients.remove(i);
            ingrAmount.setText(Integer.parseInt(ingrAmount.getText()) - 1 + "");
            slider.setMax(slider.getMax() - 1);
            slider.setValue(slider.getMax());
        });

    }

    public void onBtnFind(ActionEvent actionEvent) {

    }

    public void onBtnBack(ActionEvent actionEvent) {

        Main.controllerBase.btnStart.fire();

    }

    public void onBtnReset(ActionEvent actionEvent) {
        ingredients.clear();
        ingrAmount.setText("0");
        slider.setValue(0);
        slider.setMax(0);
        for (VBox box : boxes) {
            box.getChildren().clear();
        }
    }
}
