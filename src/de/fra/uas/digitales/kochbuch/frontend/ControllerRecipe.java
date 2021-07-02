package de.fra.uas.digitales.kochbuch.frontend;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Rating;
import org.controlsfx.glyphfont.Glyph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerRecipe implements Initializable {

    @FXML
    public Label NameLabelNeu;
    public ImageView BildRezeptNeu;
    public Label LabelBeschreibung;
    public Label labelZutaten;
    public Label labelSteps;
    public Recipe currentRecipe;
    public VBox boxNameDescRat;
    public Label labelTime;
    public Button btnFav;
    public Label labelHeart;
    private Rating rating;
    private boolean isFilter = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rating = new Rating();
        rating.setMax(5);
        rating.setUpdateOnHover(false);
        rating.addEventFilter(MouseEvent.ANY, Event::consume);
        boxNameDescRat.getChildren().add(2, rating);
        btnFav.setGraphic(new Glyph("FontAwesome", "HEART_ALT").size(90).color(Color.INDIANRED).useHoverEffect());
    }

    @FXML
    public void output(Recipe recipe, boolean filter) throws IOException {

        isFilter = filter;
        if(recipe.isFav()){
            btnFav.setGraphic(new Glyph("FontAwesome", "HEART").size(90).color(Color.INDIANRED).useHoverEffect());
        }
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
        labelTime.setText("Kochzeit: " + recipe.getTime() + " Minuten");

        currentRecipe = recipe;

    }


    public void onBtnEdit(ActionEvent actionEvent) {

        Main.controllerEditRecipe.output(currentRecipe);
        Main.mainPanel.setCenter(Main.editRecipePage);

    }

    public void onBtnBack(ActionEvent actionEvent) {
        if (!isFilter) {
            Main.controllerBase.btnStart.fire();
        } else {
            Main.controllerBase.btnFilter.fire();
        }

    }

    public void onExport(ActionEvent actionEvent) throws IOException, DocumentException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Rezept Exportieren");
        fileChooser.setInitialFileName(currentRecipe.getName());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(Main.stage);
        if (file != null) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file.getPath()));
            document.open();
            getDocumentFromRecipe(document);
            document.close();
        }
    }


    private Document getDocumentFromRecipe(Document document) throws DocumentException, IOException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.BLACK);
        Font fontSmall = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);


        Paragraph paragraph = new Paragraph("Rezept " + currentRecipe.getName(), font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(new Paragraph(" ", font));
        Paragraph desc = new Paragraph(currentRecipe.getDesc(), fontSmall);
        desc.setAlignment(Element.ALIGN_CENTER);
        document.add(desc);
        document.add(new Paragraph(" ", fontSmall));
        Paragraph time = new Paragraph("Kochzeit: " + currentRecipe.getTime() + " Minuten. Rating: " + currentRecipe.getRating(), fontSmall);
        time.setAlignment(Element.ALIGN_CENTER);
        document.add(time);

        document.add(new Paragraph(" ", font));
        Image img = Image.getInstance(currentRecipe.getImageRaw());
        img.scalePercent(35);
        document.add(img);


        document.add(new Paragraph(" ", font));


        Paragraph ingred = new Paragraph("Zutaten:", font);
        document.add(ingred);
        for (Ingredient i : currentRecipe.getIngredients()) {
            document.add(new Paragraph(i.toString(), fontSmall));
        }
        document.add(new Paragraph(" ", font));


        document.add(new Paragraph("Anleitung:", font));
        document.add(new Paragraph(currentRecipe.getSteps(), fontSmall));

        return document;
    }

    public void btnFav(ActionEvent actionEvent) throws SQLException {
        if (currentRecipe.isFav()) {
            btnFav.setGraphic(new Glyph("FontAwesome", "HEART_ALT").size(90).color(Color.INDIANRED));
            DataManager.get().setFavorite(currentRecipe.getID(), false);
            currentRecipe.setFav(false);
        } else {
            btnFav.setGraphic(new Glyph("FontAwesome", "HEART").size(90).color(Color.INDIANRED));
            DataManager.get().setFavorite(currentRecipe.getID(), true);
            currentRecipe.setFav(true);
        }
        labelHeart.setGraphic(new Glyph("FontAwesome", "HEART").size(90).color(Color.INDIANRED));

        ScaleTransition transition = new ScaleTransition(Duration.millis(300), labelHeart);
        transition.setToX(2);
        transition.setToY(2);
        transition.play();


        Timer t1 = new Timer();
        t1.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ScaleTransition transitionReset = new ScaleTransition(Duration.millis(1), labelHeart);
                    transitionReset.setToX(1);
                    transitionReset.setToY(1);
                    transitionReset.play();
                    labelHeart.setGraphic(null);
                    t1.cancel();
                });
            }
        }, 350, 350);
    }
}
