package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerNewRecipeLayout implements Initializable {
    public TextArea recipeSteps;
    public TextArea recipeDesc;
    public TextField recipeName;
    public TextField amountIngredient;
    public TextField textIngredient;
    public TextField unitIngredient;
    public VBox ingredientBox;
    public TextField picName;
    public TextField picPath;
    private final List<Ingredient> ingredientList = new LinkedList<>();
    private File currentImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void output(Recipe recipe){

        recipeName.setText(recipe.getName());
        recipeSteps.setText(recipe.getSteps());
        recipeDesc.setText(recipe.getDesc());
        //todo: restlichen Sachen wie Ingredients, bild, ...


    }



    private void clearRecipe() {
        recipeSteps.setText("");
        recipeDesc.setText("");
        recipeName.setText("");
        ingredientList.clear();
        currentImage = null;
        ingredientBox.getChildren().clear();
        picName.setText("");
        picPath.setText("");

    }

    public void onSave(ActionEvent actionEvent) throws SQLException, IOException {

        if (isReadySave()) {
            System.out.println("new rec");
            Recipe r = new Recipe();
            r.setName(recipeName.getText())
                    .setDesc(recipeDesc.getText())
                    .setImageRaw(currentImage)
                    .setIngredients(ingredientList)
                    .setRating(0)
                    .setTime(15.0f)
                    .setSteps(recipeSteps.getText());
            DataManager.get().addNewRecipe(r);
            clearRecipe();
            Main.controllerBase.btnStart.fire();

        }
    }

    public void onBtnIngredient(ActionEvent actionEvent) {

        try {
            float amount = Float.parseFloat(amountIngredient.getText());
            String name = textIngredient.getText();
            String unit = unitIngredient.getText();
            Ingredient ingredient;

            if (!name.isBlank()) {
                if (unit.isBlank()) {
                    ingredient = new Ingredient(name, amount);
                } else {
                    ingredient = new Ingredient(name, amount, unit);
                }
                HBox box = new HBox();
                TextField text = new TextField(ingredient.toString());
                text.setEditable(false);
                Button button = new Button("X");
                box.getChildren().add(text);
                box.getChildren().add(button);
                ingredientBox.getChildren().add(box);
                ingredientList.add(ingredient);

                button.setOnAction(actionEvent1 -> {
                    ingredientBox.getChildren().remove(box);
                    ingredientList.remove(ingredient);
                });

                clearIngredientButtons();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearIngredientButtons() {
        amountIngredient.clear();
        textIngredient.clear();
        unitIngredient.clear();
    }

    private boolean isReadySave() {

        if (!recipeDesc.getText().isBlank()) {
            if (!recipeName.getText().isBlank()) {
                if (!recipeSteps.getText().isBlank()) {
                    if (currentImage != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public void onBtnPicture(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Bild auswählen");
        File file = fileChooser.showOpenDialog(Main.stage);
        if (file != null) {
            currentImage = file;
            picPath.setText(file.getAbsolutePath());
            picName.setText(file.getName());
        }
    }
}
