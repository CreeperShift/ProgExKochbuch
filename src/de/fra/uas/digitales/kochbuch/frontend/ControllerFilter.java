package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
import java.util.*;
import java.util.stream.Collectors;

public class ControllerFilter implements Initializable {
    public Button btnFind;
    public Button btnFilter;
    public List<Ingredient> ingredients = new LinkedList<>();
    public VBox[] boxes = new VBox[4];
    public VBox box1;
    public VBox box2;
    public VBox box3;
    public VBox box4;
    public SearchableComboBox<String> ingredientSearch;
    public GridPane grid;
    public Slider slider;
    public Label ingrAmount;
    public Button btnReset;
    public Button btnFront;
    public Label page;
    public Button btnBack;
    private List<String> names = new LinkedList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxes[0] = box1;
        boxes[1] = box2;
        boxes[2] = box3;
        boxes[3] = box4;
        btnBack.setText("");
        btnBack.setDisable(true);
        btnFront.setDisable(true);
        btnFront.setText("");
        btnBack.setGraphic(new Glyph("FontAwesome", "chevron_left").size(25));
        btnFront.setGraphic(new Glyph("FontAwesome", "chevron_right").size(25));

        slider.valueProperty().addListener((obs, oldVal, newVal) -> slider.setValue(newVal.intValue()));
    }

    public void startConnected() {
        try {
            List<String> list = DataManager.get().getAllIngredientNames();

            ingredientSearch.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void reset() {
        grid.getChildren().clear();
        btnFront.setDisable(true);
        btnBack.setDisable(true);
        btnReset.fire();
    }

    public void onNewFilter(ActionEvent actionEvent) {

        if (ingredientSearch.getValue() != null && canAddFilter()) {
            addIngredientToBox(ingredientSearch.getValue());
            slider.setMax(ingredients.size());
            slider.setValue(slider.getMax());
        }


    }

    private boolean canAddFilter() {

        if (ingredientSearch.getValue().isBlank()) {
            return false;
        } else if (isAlreadyFilter(ingredientSearch.getValue())) {
            return false;
        } else if (ingredients.size() >= 12) {
            return false;
        }

        return true;
    }


    private boolean isAlreadyFilter(String text) {
        for (Ingredient i : ingredients) {
            if (i.name().equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    private void addIngredientToBox(String ingr) {
        HBox box = new HBox();
        TextField text = new TextField(ingr);
        text.setEditable(false);
        Button button = new Button("X");
        box.getChildren().add(text);
        box.getChildren().add(button);
        int whichBox = ingredients.size() % boxes.length;
        boxes[whichBox].getChildren().add(box);
        Ingredient i = new Ingredient(ingr, 0, "");
        ingredients.add(i);
        ingrAmount.setText(Integer.parseInt(ingrAmount.getText()) + 1 + "");

        button.setOnAction(actionEvent1 -> {
            boxes[whichBox].getChildren().remove(box);
            ingredients.remove(i);
            ingrAmount.setText(Integer.parseInt(ingrAmount.getText()) - 1 + "");
            slider.setMax(slider.getMax() - 1);
            slider.setValue(slider.getMax());
        });

    }

    public void onBtnFind(ActionEvent actionEvent) throws SQLException {

        names = ingredients.stream().map(Ingredient::name).collect(Collectors.toList());

        if (!names.isEmpty()) {

            grid.getChildren().clear();
            List<Recipe> recipeList = DataManager.get().getRecipeByIngredient(names, ((int) slider.getValue()), 0);
            btnFront.setDisable(true);
            btnBack.setDisable(true);
            if (recipeList.size() >= 6) {
                btnFront.setDisable(false);
                List<Recipe> recipeListNext = DataManager.get().getRecipeByIngredient(names, ((int) slider.getValue()), 1);
                if (recipeListNext.isEmpty()) {
                    btnFront.setDisable(true);
                }
            }
            addChildren(recipeList);
        }

    }


    private void addChildren(List<Recipe> recipeList) {

        if (recipeList != null && !recipeList.isEmpty()) {

            for (int i = 0; i < recipeList.size(); i++) {
                Pane pane = new Pane();
                pane.prefWidthProperty().bind(grid.widthProperty().divide(3));
                pane.prefHeightProperty().bind(grid.heightProperty().divide(2));
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

                grid.add(pane, gridX, gridY);
            }
        } else if (recipeList != null) {

            Label l = new Label("Keine Rezepte...");
            btnBack.setDisable(true);
            btnFront.setDisable(true);
            l.setFont(Font.font("Segeo UI", 50));
            l.setLayoutX(grid.getWidth() / 3);
            grid.add(l, 1, 0);

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
                Main.controllerRecipe.output(DataManager.get().getRecipeByName(name), true);
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        });
        pane2.getChildren().add(imageView);
        pane.getChildren().add(pane2);


    }


    public void onBtnBack(ActionEvent actionEvent) {

        Main.controllerBase.btnStart.fire();

    }

    public void onBtnReset(ActionEvent actionEvent) {
        ingredients.clear();
        ingrAmount.setText("0");
        slider.setValue(1);
        slider.setMax(1);
        btnBack.setDisable(true);
        btnFront.setDisable(true);
        page.setText("1");
        grid.getChildren().clear();
        for (VBox box : boxes) {
            box.getChildren().clear();
        }
    }

    public void onBtnFront(ActionEvent actionEvent) throws SQLException {


        int i = Integer.parseInt(page.getText());
        i++;

        grid.getChildren().clear();

        List<Recipe> recipeList = DataManager.get().getRecipeByIngredient(names, ((int) slider.getValue()), i - 1);
        addChildren(recipeList);


        page.setText("" + i);
        btnBack.setDisable(true);
        btnFront.setDisable(true);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (recipeList.size() >= 6) {
                    btnFront.setDisable(false);
                }
                btnBack.setDisable(false);
            }

        };
        timer.schedule(task, 400);

    }

    public void onBtnPageBack(ActionEvent actionEvent) throws SQLException {

        int i = Integer.parseInt(page.getText());
        if (i > 0) {
            i--;

            grid.getChildren().clear();
            List<Recipe> recipeList = DataManager.get().getRecipeByIngredient(names, ((int) slider.getValue()), i - 1);
            addChildren(recipeList);
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
}
