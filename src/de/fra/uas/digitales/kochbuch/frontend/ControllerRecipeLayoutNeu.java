package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerRecipeLayoutNeu implements Initializable {

    @FXML
    public Label NameLabelNeu;
    public ImageView BildRezeptNeu;
    public Label LabelBeschreibung;
    public Label labelZutaten;
    public Label labelSteps;
    public ImageView bildRating;

    public Recipe rectmp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void output(Recipe recipe) throws IOException {

        this.NameLabelNeu.setText(recipe.getName());
        this.LabelBeschreibung.setText(recipe.getDesc());
        StringBuilder tmp = new StringBuilder("\n");
        for (Ingredient i : recipe.getIngredients()) {
            tmp.append(" - ").append(i.toString()).append("\n");
        }
        this.labelZutaten.setText(tmp.toString());
        this.labelSteps.setText(recipe.getSteps());
        this.BildRezeptNeu.setImage(recipe.getImage());
        this.bildRating.setImage(getRatingBild(recipe.getRating(), recipe));
        this.rectmp=recipe;

    }

    public Image getRatingBild(int rat, Recipe rec) throws IOException {

        File file;
        switch (rat) {
            case 1:
                file = new File("resources/images/1Stern.jpg");
                rec.setImageRawRating(file);
                break;
            case 2:
                file = new File("resources/images/2Stern.jpg");
                rec.setImageRawRating(file);
                break;
            case 3:
                file = new File("resources/images/3Stern.jpg");
                rec.setImageRawRating(file);
                break;
            case 4:
                file = new File("resources/images/4Stern.jpg");
                rec.setImageRawRating(file);
                break;
            case 5:
                file = new File("resources/images/5Stern.jpg");
                rec.setImageRawRating(file);
                break;
            default:
                file = new File("resources/images/0Stern.jpg");
                rec.setImageRawRating(file);
                break;
        }

        return rec.getImageRating();

    }

    public void editMethode(ActionEvent actionEvent) throws SQLException {

        Main.mainPanel.setCenter(Main.newRecipePage);
        Main.newRecipePage.prefWidthProperty().bind(Main.mainPanel.widthProperty().subtract(200));
        Main.newRecipePage.prefHeightProperty().bind(Main.mainPanel.heightProperty().subtract(50)); //TODO: This is messy
        Main.controllerNewRecipe.output(this.rectmp);

    }


}
