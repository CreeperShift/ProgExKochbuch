package de.fra.uas.digitales.kochbuch.frontend;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerRecipe implements Initializable {
    public Label textName;
    public ImageView imageRezept;
    public TextFlow textBeschreibung;
    public ListView<String> textZutaten;
    public TextFlow textAnleitung;
    public AnchorPane imageAnchor;

    private boolean done = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setRecipe() {
        if(!done) {
            textName.setText("Flammkuchen");
            try {
                FileInputStream pic = new FileInputStream("resources/images/flammkuchen.jpg");
                Image image = new Image(pic);
                imageRezept.setImage(image);
                imageRezept.setLayoutY(imageAnchor.getWidth()/2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Text textbesch = new Text("Diese leckere Spezialität aus dem Elsass eignet sich sehr gut auch zum Apéro. Dünn ausgewallter Brotteig mit Sauerrahm, Zwiebeln und Speckwürfeli!");
            textBeschreibung.getChildren().add(textbesch);

            textZutaten.getItems().add("200g Mehl");
            textZutaten.getItems().add("50ml Wasser");
            textZutaten.getItems().add("1g Salz");
            textZutaten.getItems().add("2g Hefe");
            textZutaten.getItems().add("1 Becher Creme Fraiche");
            textZutaten.getItems().add("1 Zwiebel");
            textZutaten.getItems().add("1 Packung Speck");

            Text anl1 = new Text("Mehl, Salz und Zucker in einer Schüssel mischen\n\n");
            Text anl2 = new Text("Hefe zerbröckeln, daruntermischen. Wasser dazugiessen, zu einem weichen, glatten Teig kneten. Zugedeckt bei Raumtemperatur ca. 1 Std. aufs Doppelte aufgehen lassen.\n\n");
            Text anl3 = new Text("Teig auf wenig Mehl oval, ca. 3 mm dick auswallen, in ein mit Backpapier belegtes Blech legen.\n\n");
            Text anl4 = new Text("Ofen auf 240 Grad vorheizen.\n\n");
            Text anl5 = new Text("Crème fraîche auf dem Teig verteilen, dabei ringsum einen Rand von ca. 1 cm frei lassen. Zwiebel schälen, in feine Ringe schneiden, mit den Speckwürfeli auf der Crème fraîche verteilen, würzen.\n\n");
            textAnleitung.getChildren().addAll(anl1, anl2, anl3, anl4, anl5);
        done = true;
        }
    }


}
