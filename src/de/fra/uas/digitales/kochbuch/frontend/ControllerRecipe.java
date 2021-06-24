package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerRecipe implements Initializable {

    @FXML public Label NameAusgabeRezept;
    @FXML public Label AusgabeBeschreibungRezept;
    @FXML public Label AusgabeInstructionsRezept;
    @FXML public Label AusgabeDauerRezept;
    @FXML public TextField suchFeld;
    @FXML public ImageView ratingBild;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void output(Recipe recipe){

        this.NameAusgabeRezept.setText(recipe.getName());
        this.AusgabeBeschreibungRezept.setText(recipe.getDesc());
        this.AusgabeInstructionsRezept.setText(recipe.getSteps());
        this.AusgabeDauerRezept.setText(recipe.getTime()+" Minuten");
        this.ratingBild.setImage(this.getRatingBild(recipe.getRating()));

    }

    @FXML
    public void suchMethode(ActionEvent actionEvent) throws SQLException {

        String temp = suchFeld.getText();
        suchFeld.setText("");
        DataManager dataManager = new DataManager();
        this.output(dataManager.getRecipeByName(temp));

    }

    public Image getRatingBild(int r){

        Image bild = null;
        switch(r){
            case 1: bild = new Image(getClass().getResourceAsStream("1Stern.jpg"));break;
            case 2: bild = new Image(getClass().getResourceAsStream("2Stern.jpg"));break;
            case 3: bild = new Image(getClass().getResourceAsStream("3Stern.jpg"));break;
            case 4: bild = new Image(getClass().getResourceAsStream("4Stern.jpg"));break;
            case 5: bild = new Image(getClass().getResourceAsStream("5Stern.jpg"));break;
            default: bild = new Image(getClass().getResourceAsStream("0Stern.jpg"));
        }
        return bild;
    }

}
