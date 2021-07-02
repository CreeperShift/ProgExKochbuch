package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerStartLayout implements Initializable {
    public VBox vBox;
    public Label page;
    public Button btnBack;
    public Button btnFront;
    public TextField searchbox;
    public AnchorPane gridArea;
    public GridPane gridV;
    public Label labelName;
    public Button btnHome;
    public Button btnSearch;
    public SearchableComboBox<String> recipetag;
    private String activeSearch = null;
    public List<String> categoryList;
    private int i = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnHome.setGraphic(new Glyph("FontAwesome", "home").size(30));
        searchbox.setText("");
        btnBack.setText("");
        btnBack.setDisable(true);
        btnFront.setText("");
        btnBack.setGraphic(new Glyph("FontAwesome", "chevron_left").size(25));
        btnFront.setGraphic(new Glyph("FontAwesome", "chevron_right").size(25));
        labelName.setText("Alle Rezepte");


    }


    public void startConnected() {
        List<Recipe> startRecipes = null;
        try {
            startRecipes = DataManager.get().getStartRecipes(0);
            categoryList = DataManager.get().getCategories();
            recipetag.setValue("Alle");
            ObservableList<String> list = FXCollections.observableArrayList(categoryList);
            list.add(0, "Alle");
            recipetag.setItems(list);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        addChildren(startRecipes);
    }


    private void addChildren(List<Recipe> recipeList) {

        if (recipeList != null && !recipeList.isEmpty()) {

            for (int i = 0; i < recipeList.size(); i++) {
                Pane pane = new Pane();
                pane.prefWidthProperty().bind(gridV.widthProperty().divide(3));
                pane.prefHeightProperty().bind(gridV.heightProperty().divide(3));
                StackPane pane2 = new StackPane();
                pane2.getStyleClass().add("bildRand");

                ImageView imageView = new ImageView(recipeList.get(i).getImage());
                //imageView.setPreserveRatio(true);
                imageView.fitHeightProperty().bind(pane.heightProperty().subtract(15));
                imageView.fitWidthProperty().bind(pane.widthProperty().subtract(15));
                imageView.getStyleClass().add("startimage");
                setupImageListeners(pane, imageView, recipeList.get(i).getName(), pane2);

                int gridX = i % 3;
                int gridY = i / 3;

                gridV.add(pane, gridX, gridY);
            }
        } else if (recipeList != null) {

            Label l = new Label("Keine Rezepte gefunden");
            btnBack.setDisable(true);
            btnFront.setDisable(true);
            l.setFont(Font.font("Segeo UI", 50));
            l.setLayoutX(gridV.getWidth() / 3);
            gridV.add(l, 1, 1);

        }
    }

    private void setupImageListeners(Pane pane, ImageView imageView, String name, StackPane pane2) {

        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                    Label label = new Label(name);
                    label.setFont(Font.font("Segeo UI", 21));
                    label.setAlignment(Pos.CENTER);
                    label.setTextOverrun(OverrunStyle.ELLIPSIS);
                    label.setTextAlignment(TextAlignment.CENTER);

                    AnchorPane background = new AnchorPane();
                    background.setStyle("-fx-background-color: black; -fx-opacity: 0.65");
                    background.setMaxHeight(pane2.getHeight() * 0.2);
                    background.setPadding(new Insets(pane2.getHeight() * 0.1, 0, pane2.getHeight() - pane2.getHeight() * 0.9, 0));
                    background.setLayoutY(background.getHeight() / 2);
                    background.setLayoutX(background.getWidth() / 2);
                    background.setMouseTransparent(true);
                    background.getChildren().add(label);
                    label.setMouseTransparent(true);
                    label.setMaxWidth(pane2.getWidth() - 60);
                    pane2.getChildren().add(background);

                    label.setTextFill(Color.WHITE);
                    ScaleTransition scale = new ScaleTransition(Duration.millis(50), pane2);
                    ScaleTransition scale1 = new ScaleTransition(Duration.millis(50), label);
                    ScaleTransition scale2 = new ScaleTransition(Duration.millis(50), background);
                    scale.setToX(1.07);
                    scale.setToY(1.07);
                    scale1.setToX(1.07);
                    scale1.setToY(1.07);
                    scale2.setToX(1.0);
                    scale2.setToY(1.0);
                    scale.play();
                    scale1.play();
                    scale2.play();
                    pane2.getChildren().add(label);

                }
        );
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, event -> {
                    final Label[] l = {null};
                    pane2.getChildren().forEach(c -> {
                        if (c instanceof Label) {
                            l[0] = (Label) c;
                        }
                    });
                    final AnchorPane[] a = {null};
                    pane2.getChildren().forEach(c -> {
                        if (c instanceof AnchorPane) {
                            a[0] = (AnchorPane) c;
                        }
                    });
                    pane2.getChildren().removeAll(l);
                    pane2.getChildren().removeAll(a);
                    ScaleTransition scale = new ScaleTransition(Duration.millis(50), pane2);
                    scale.setToX(1f);
                    scale.setToY(1f);
                    scale.play();
                }
        );

        imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
        {
            Main.mainPanel.setCenter(Main.recipePage);
            try {
                Main.controllerRecipe.output(DataManager.get().getRecipeByName(name), false);
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        });
        pane2.getChildren().add(imageView);
        pane.getChildren().add(pane2);


    }


    public void onBtnBack(ActionEvent actionEvent) throws SQLException, IOException {
        int i = Integer.parseInt(page.getText());
        if (i > 0) {
            i--;

            gridV.getChildren().clear();
            if (activeSearch == null) {
                addChildren(DataManager.get().getStartRecipes(i - 1));
            } else {
                addChildren(DataManager.get().getRecipeList(activeSearch, i - 1));
            }
            page.setText("" + i);
        }
        btnBack.setDisable(true);
        btnFront.setDisable(true);
        Timer timer = new Timer();
        int finalI = i;
        TimerTask task = new TimerTask() {
            public void run() {
                btnFront.setDisable(false);
                if (finalI > 1) {
                    btnBack.setDisable(false);
                }
            }

        };
        timer.schedule(task, 400);


    }


    public void onBtnFront(ActionEvent actionEvent) throws SQLException, IOException {

        int i = Integer.parseInt(page.getText());
        i++;

        gridV.getChildren().clear();

        final List<Recipe> recipeList;

        if (activeSearch == null) {
            recipeList = DataManager.get().getStartRecipes(i - 1);
            addChildren(recipeList);
        } else {
            recipeList = DataManager.get().getRecipeList(activeSearch, i - 1);
            addChildren(recipeList);
        }
        page.setText("" + i);
        btnBack.setDisable(true);
        btnFront.setDisable(true);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (recipeList.size() >= 9) {
                    btnFront.setDisable(false);
                }
                btnBack.setDisable(false);
            }

        };
        timer.schedule(task, 400);
    }


    public void onBtnSuche(ActionEvent actionEvent) throws SQLException, IOException {

        gridV.getChildren().clear();
        String temp = searchbox.getText();
        List<Recipe> recipeList = DataManager.get().getRecipeList(temp, 0);
        addChildren(recipeList);
        if (temp.isBlank()) {
            labelName.setText("Alle Rezepte");
            page.setText("1");
            activeSearch = null;
        } else {
            labelName.setText("Rezepte für " + temp);
            page.setText("1");
            activeSearch = temp;
            btnFront.setDisable(false);
            btnBack.setDisable(true);
            recipetag.setValue("Alle");

            if (recipeList.size() < 9) {
                btnFront.setDisable(true);
            } else {
                List<Recipe> recipeListNext = DataManager.get().getRecipeList(temp, 1);
                if (recipeListNext.isEmpty()) {
                    btnFront.setDisable(true);
                }
            }
        }
    }

    public void onHome(ActionEvent actionEvent) throws SQLException {
        gridV.getChildren().clear();
        addChildren(DataManager.get().getStartRecipes(0));
        labelName.setText("Alle Rezepte");
        recipetag.setValue("Alle");
        searchbox.setText("");
        page.setText("1");
        btnFront.setDisable(false);
        btnBack.setDisable(true);
        activeSearch = null;
    }

    public void onKey(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.ENTER) {
            btnSearch.fire();
        }

    }

    public void onCategory(Event actionEvent) {
        i++;
        if (i == 2) {
            System.out.println(recipetag.getValue());
            gridV.getChildren().clear();
            if (recipetag.getValue() != null && recipetag.getValue().equalsIgnoreCase("Alle")) {
                btnHome.fire();
            } else {
                List<Recipe> recipeList = DataManager.get().getRecipeByTag(recipetag.getValue(), 0);
                activeSearch = null;
                searchbox.setText("");
                labelName.setText("Alle Rezepte in Kategorie " + recipetag.getValue());
                addChildren(recipeList);
            }
            i = 0;
        }
    }
}
