package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.controlsfx.control.Rating;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerNewRecipe implements Initializable {
    public TextArea recipeSteps;
    public TextArea recipeDesc;
    public TextField recipeName;
    public TextField amountIngredient;
    public TextField textIngredient;
    public TextField unitIngredient;
    public VBox ingredientBox;
    public TextField picName;
    public TextField picPath;
    public Rating recipeRating;
    public TextField recipeTime;
    private File currentImage;
    private final List<Ingredient> ingredientList = new LinkedList<>();
    private Recipe aktuellesRezept;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeRating.setMax(5);
        recipeRating.setPartialRating(false);
    }

    @FXML
    public void output(Recipe recipe) {
        aktuellesRezept = recipe;
        recipeName.setText(recipe.getName());
        recipeSteps.setText(recipe.getSteps());
        recipeDesc.setText(recipe.getDesc());
    }

    public void onSave(ActionEvent actionEvent) throws SQLException, IOException {
        if (isReadySave()) {
            System.out.println("new rec");
            Recipe r = new Recipe();
            r.setName(recipeName.getText())
                    .setDesc(recipeDesc.getText())
                    .setImageRaw(currentImage)
                    .setIngredients(ingredientList)
                    .setRating(((int) recipeRating.getRating()))
                    .setSteps(recipeSteps.getText());

            float time = 0;
            try {
                time = Float.parseFloat(recipeTime.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            r.setTime(time);

            if (this.aktuellesRezept != null) {
                DataManager.get().editRecipe(this.aktuellesRezept);
            }
            DataManager.get().addNewRecipe(r);
            clearRecipe();
            Main.controllerBase.btnStart.fire();
        }
    }

    public void onDeleteRezept(ActionEvent actionEvent) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warnung!");
        alert.setHeaderText("Rezept wirklich löschen?");
        alert.setContentText("Kann nicht rückgängig gemacht werden!");
        Optional<ButtonType> bestätigung = alert.showAndWait();
        if (bestätigung.get() == ButtonType.OK) {
            if (this.aktuellesRezept != null) {
                DataManager.get().deleteRecipe(this.aktuellesRezept);
                Alert weg = new Alert(Alert.AlertType.INFORMATION);
                weg.setTitle("Information");
                weg.setHeaderText("Rezept wurde gelöscht!!");
                weg.show();
            } else {
                Alert nicht = new Alert(Alert.AlertType.INFORMATION);
                nicht.setTitle("Information");
                nicht.setHeaderText("Ooh, da ist wohl was schief gelaufen!");
                nicht.setContentText("Kein Rezept ausgewählt!");
                nicht.show();
            }
        } else {
            Alert abbruch = new Alert(Alert.AlertType.INFORMATION);
            abbruch.setTitle("Information");
            abbruch.setHeaderText("Löschvorgang wurde abgebrochen!");
            abbruch.show();
        }
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
                box.setPadding(new Insets(5, 5, 0, 0));
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

        //Wenn nichts fehlt ready!
        if (!recipeDesc.getText().isBlank()) {
            if (!recipeName.getText().isBlank()) {
                if (!recipeSteps.getText().isBlank()) {
                    if (currentImage != null) {
                        //if(ingredientList != null){
                        return true;
                        // }

                    }
                }
            }
        }

        //Warnung das was fehlt!
        String wasFehltNoch = "Fehlende Komponenten:\n";
        if (recipeName.getText().isBlank()) {
            wasFehltNoch += "-Name\n";
        }
        if (recipeDesc.getText().isBlank()) {
            wasFehltNoch += "-Beschreibung\n";
        }
        if (recipeSteps.getText().isBlank()) {
            wasFehltNoch += "-Schritte\n";
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Leider fehlt etwas!");
        alert.setContentText(wasFehltNoch);
        alert.showAndWait();

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

    public void btnZuruck(ActionEvent actionEvent) {

        //Main.mainPanel.setCenter(Main.startPane);
        Main.controllerBase.btnStart.fire();

    }

    public void btnAbbrechen(ActionEvent actionEvent) {


       Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Title");
        alert.setHeaderText("Sind Sie sicher?");
        //alert.setContentText("");
      ButtonType  buttonTypeOne = new ButtonType("Ja");
       ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();


      //  Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeOne){
           Main.mainPanel.setCenter(Main.newRecipePage);
             clearRecipe();

        }


    }
}