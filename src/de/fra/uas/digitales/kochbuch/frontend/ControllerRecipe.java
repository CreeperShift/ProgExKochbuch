package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerRecipe implements Initializable {

    @FXML
    public Label NameLabelNeu;
    public ImageView BildRezeptNeu;
    public Label LabelBeschreibung;
    public Label labelZutaten;
    public Label labelSteps;
    public ImageView bildRating;
    public Recipe currentRecipe;
    public VBox boxNameDescRat;
    private Rating rating;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rating = new Rating();
        rating.setMax(5);
        rating.setUpdateOnHover(false);
        rating.addEventFilter(MouseEvent.ANY, Event::consume);
        boxNameDescRat.getChildren().add(1, rating);
    }

    @FXML
    public void output(Recipe recipe) throws IOException {

        NameLabelNeu.setText(recipe.getName());
        LabelBeschreibung.setText(recipe.getDesc());
        StringBuilder tmp = new StringBuilder("\n");
        for (Ingredient i : recipe.getIngredients()) {
            tmp.append(" - ").append(i.toString()).append("\n");
        }
        labelZutaten.setText(tmp.toString());
        labelSteps.setText(recipe.getSteps());
        BildRezeptNeu.setImage(recipe.getImage());
        rating.ratingProperty().setValue(recipe.getRating());

        currentRecipe = recipe;

    }


    public void onBtnEdit(ActionEvent actionEvent) {

    }

    public void onBtnBack(ActionEvent actionEvent) {
        Main.controllerBase.btnStart.fire();

    }
}
