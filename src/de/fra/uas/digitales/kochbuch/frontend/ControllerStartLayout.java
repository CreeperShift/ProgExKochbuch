package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchbox.setText("");
        labelName.setText("Alle Rezepte");
        List<Recipe> startRecipes = null;
        try {
            startRecipes = DataManager.get().getStartRecipes(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        addChildren(startRecipes);
    }


    private void addChildren(List<Recipe> recipeList) {

        if (recipeList != null && !recipeList.isEmpty()) {

            for (int i = 0; i < recipeList.size(); i++) {
                AnchorPane pane = new AnchorPane();
                pane.prefWidthProperty().bind(gridV.widthProperty().divide(3));
                pane.prefHeightProperty().bind(gridV.heightProperty().divide(3));

                ImageView imageView = new ImageView(recipeList.get(i).getImage());
                //imageView.setPreserveRatio(true);
                imageView.fitHeightProperty().bind(pane.heightProperty().subtract(15));
                imageView.fitWidthProperty().bind(pane.widthProperty().subtract(15));
                imageView.getStyleClass().add("startimage");
                setupImageListeners(pane, imageView, recipeList.get(i).getName());

                int gridX = i % 3;
                int gridY = i / 3;

                gridV.add(pane, gridX, gridY);
            }
        }

    }

    private void setupImageListeners(Pane pane, ImageView imageView, String name) {

        AnchorPane pane2 = new AnchorPane();
        pane2.getStyleClass().add("startimage");
        pane2.setPadding(new Insets(1,1,1,1));
        pane2.minHeightProperty().bind(pane.heightProperty().subtract(15));
        pane2.prefHeightProperty().bind(pane.heightProperty().subtract(15));
        pane2.minWidthProperty().bind(pane.widthProperty().subtract(15));
        pane2.prefWidthProperty().bind(pane.widthProperty().subtract(15));
        pane2.maxWidthProperty().bind(pane.widthProperty().subtract(15));
        pane2.maxHeightProperty().bind(pane.heightProperty().subtract(15));
        pane2.getChildren().add(imageView);

        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                    Label label = new Label(name);
                    label.setFont(Font.font("Segeo UI", 23));
                    label.setAlignment(Pos.CENTER);
                    label.setTextOverrun(OverrunStyle.ELLIPSIS);
                    label.setTextAlignment(TextAlignment.CENTER);
                    label.minWidthProperty().bind(pane2.minWidthProperty());
                    label.minHeightProperty().bind(pane2.minHeightProperty().divide(2).subtract(15));
                    label.maxWidthProperty().bind(pane.widthProperty().subtract(25));


                    AnchorPane background = new AnchorPane();
                    background.setLayoutY(63);
                    background.setStyle("-fx-background-color: black; -fx-opacity: 0.65");
                    background.setMouseTransparent(true);
                    background.getChildren().add(label);
                    pane2.getChildren().add(background);

                    label.setTextFill(Color.WHITE);
                    ScaleTransition scale = new ScaleTransition(Duration.millis(50), pane2);
                    scale.setToX(1.07);
                    scale.setToY(1.07);
                    scale.play();

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
                Main.controllerRecipe.output(DataManager.get().getRecipeByName(name));
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        });

        pane.getChildren().add(pane2);


    }


    public void onBtnBack(ActionEvent actionEvent) throws SQLException {
        int i = Integer.parseInt(page.getText());
        if (i > 0) {
            i--;

            gridV.getChildren().clear();
            addChildren(DataManager.get().getStartRecipes(i));
            page.setText("" + i);
        }

        if (i == 0) {
            btnBack.setDisable(true);
        }

    }


    public void onBtnFront(ActionEvent actionEvent) throws SQLException {

        int i = Integer.parseInt(page.getText());
        i++;

        gridV.getChildren().clear();

        addChildren(DataManager.get().getStartRecipes(i));
        page.setText("" + i);
        btnBack.setDisable(true);
        btnFront.setDisable(true);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                btnFront.setDisable(false);
                btnBack.setDisable(false);
            }

        };
        timer.schedule(task, 800);
    }


    public void onBtnSuche(ActionEvent actionEvent) throws SQLException, IOException {

        gridV.getChildren().clear();
        String temp = searchbox.getText();
        addChildren(DataManager.get().getRecipeList(temp, 0));
        if (temp.isBlank()) {
            temp = "Alle Rezepte";
        }
        labelName.setText(temp);
        searchbox.setText("");
        //TODO: add more pages to search

    }
}
