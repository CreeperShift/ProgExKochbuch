package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void output(Recipe recipe){

        this.NameLabelNeu.setText(recipe.getName());
        this.LabelBeschreibung.setText(recipe.getDesc());
        //TODO:Zutaten labelZutaten
        this.labelSteps.setText(recipe.getSteps());
        this.BildRezeptNeu.setImage(recipe.getBild());

    }









}
