package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerRecipeLayoutNeu implements Initializable {

    @FXML
    public Label NameLabelNeu;
    public ImageView BildRezeptNeu;
    public Label LabelBeschreibung;
    public Label labelZutaten;
    public Label labelSteps;
    public ImageView bildRating;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void output(Recipe recipe){

        this.NameLabelNeu.setText(recipe.getName());
        this.LabelBeschreibung.setText(recipe.getDesc());
        String tmp="\n";
        for(int i=0; i<recipe.getIngredients().size(); i++){
            tmp+=" - " + recipe.getIngredients().get(i).toString()+"\n";
        }
        this.labelZutaten.setText(tmp);
        this.labelSteps.setText(recipe.getSteps());
        this.BildRezeptNeu.setImage(recipe.getBild());
        this.bildRating.setImage(this.getRatingBild(recipe.getRating()));

    }

    public Image getRatingBild(int r){

        Image bild = null;
        switch(r){
            case 1: bild = new Image(getClass().getResourceAsStream("1Stern.jpg")); break;
            case 2: bild = new Image(getClass().getResourceAsStream("2Stern.jpg")); break;
            case 3: bild = new Image(getClass().getResourceAsStream("3Stern.jpg")); break;
            case 4: bild = new Image(getClass().getResourceAsStream("4Stern.jpg")); break;
            case 5: bild = new Image(getClass().getResourceAsStream("5Stern.jpg")); break;
            default: bild = new Image(getClass().getResourceAsStream("0Stern.jpg"));

        }
        return bild;
    }

}
