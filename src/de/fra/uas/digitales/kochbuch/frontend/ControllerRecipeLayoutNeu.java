package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;
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
    public void output(Recipe recipe) throws IOException {

        this.NameLabelNeu.setText(recipe.getName());
        this.LabelBeschreibung.setText(recipe.getDesc());
        String tmp="\n";
        for(int i=0; i<recipe.getIngredients().size(); i++){
            tmp+=" - " + recipe.getIngredients().get(i).toString()+"\n";
        }
        this.labelZutaten.setText(tmp);
        this.labelSteps.setText(recipe.getSteps());
        this.BildRezeptNeu.setImage(recipe.getImage());
        this.bildRating.setImage(getRatingBild(recipe.getRating(), recipe));

    }

    public Image getRatingBild(int rat, Recipe rec) throws IOException {

        File file;
        switch(rat){
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

}
