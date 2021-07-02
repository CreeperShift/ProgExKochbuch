package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.controlsfx.control.Rating;
import org.controlsfx.control.SearchableComboBox;

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
    public SearchableComboBox<String> recipetag;
    private File currentImage;
    private final List<Ingredient> ingredientList = new LinkedList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeRating.setMax(5);
        recipeRating.setPartialRating(false);
    }

    public void onConnected() {
        recipetag.setItems(FXCollections.observableArrayList(Main.controllerStartLayout.categoryList));
        recipetag.setValue("Keine Kategorie");
    }

    public void onSave(ActionEvent actionEvent) throws SQLException, IOException {
        if (isReadySave()) {
            Recipe r = new Recipe();
            r.setName(recipeName.getText())
                    .setDesc(recipeDesc.getText())
                    //.setImageRaw(currentImage)
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

            if (currentImage != null) {
                r.setImageRaw(currentImage);
            } else {
                File fileNoPic = new File("resources/images/noPicture.jpg");
                r.setImageRaw(fileNoPic);
            }

            DataManager.get().addNewRecipe(r);
            clearRecipe();
            Main.controllerBase.btnStart.fire();
        }
    }

    private void clearRecipe() {
        recipeSteps.setText("");
        recipetag.setValue("Keine");
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
                    //if (currentImage != null) {
                    //if(ingredientList != null){
                    return true;
                    // }

                    //}
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

        double einMB = 1048576;
        double sizeMB;

        File file = fileChooser.showOpenDialog(Main.stage);

        if (file != null) {

            sizeMB = file.length();
            String end = file.getName().toLowerCase();

            //todo: ggf. noch andere Bildformate...
            if (!end.endsWith(".jpg")) {
                Alert format = new Alert(Alert.AlertType.INFORMATION);
                format.setTitle("");
                format.setHeaderText("Falscher Dateityp!");
                format.setContentText("Bitte wählen Sie JPEG als Dateityp!");
                format.showAndWait();
                file = null;

            }
            if (sizeMB > einMB && file != null) {
                Alert big = new Alert(Alert.AlertType.INFORMATION);
                big.setTitle("");
                big.setHeaderText("Datei ist zu groß!");
                big.setContentText("Maximal 1MB");
                big.showAndWait();
                file = null;

            }
        }

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


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Title");
        alert.setHeaderText("Sind Sie sicher?");
        //alert.setContentText("");
        ButtonType buttonTypeOne = new ButtonType("Ja");
        ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();


        //  Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeOne) {
            Main.mainPanel.setCenter(Main.newRecipePage);
            clearRecipe();

        }


    }
}