package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.MockDataManager;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerStartLayout implements Initializable {
    public GridPane gridPane;
    public VBox vBox;
    public Label page;
    public Button btnBack;
    public Button btnFront;
    private ColumnConstraints column1;
    private RowConstraints row1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        gridPane = new GridPane();
        gridPane.setPrefHeight(5000);
        gridPane.getStyleClass().add("grid");
        setupGrid(gridPane);
        vBox.getChildren().add(1, gridPane);
        addChildren(0);

    }


    private void addChildren(int p) {
        List<Recipe> startRecipes = null;
        try {
            startRecipes = Main.dataManager.getStartRecipes(p);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (startRecipes != null && !startRecipes.isEmpty()) {

            for (int i = 0; i < startRecipes.size(); i++) {
                Pane pane = new Pane();
                pane.prefWidthProperty().bind(column1.prefWidthProperty());
                pane.prefHeightProperty().bind(row1.prefHeightProperty());

                ImageView imageView = new ImageView(startRecipes.get(i).getImage());
                //imageView.setPreserveRatio(true);
                imageView.fitHeightProperty().bind(pane.heightProperty().subtract(15));
                imageView.fitWidthProperty().bind(pane.widthProperty().subtract(15));
                imageView.getStyleClass().add("startPicture");

                setupImageListeners(pane, imageView, startRecipes.get(i).getName());

                int gridX = i % 3;
                int gridY = i / 3;

                gridPane.add(pane, gridX, gridY);
            }
        }

    }

    private void setupImageListeners(Pane pane, ImageView imageView, String name) {

        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                    Label label = new Label(name);
                    label.setFont(Font.font("Segeo UI", 21));
                    label.setAlignment(Pos.TOP_CENTER);
                    label.setTextOverrun(OverrunStyle.ELLIPSIS);
                    label.setTextAlignment(TextAlignment.CENTER);
                    AnchorPane background = new AnchorPane();
                    background.setStyle("-fx-background-color: black; -fx-opacity: 0.65");
                    background.setLayoutY(pane.getHeight() - 80);
                    background.setLayoutX(0);
                    background.setMinWidth(pane.getWidth() - 15);
                    background.setMaxWidth(pane.getWidth() - 15);
                    background.setMinHeight(45);

                    label.setMouseTransparent(true);
                    background.setMouseTransparent(true);
                    label.setMinWidth(pane.getWidth() - 15);
                    label.setMaxWidth(pane.getWidth() - 15);

                    pane.getChildren().add(background);
                    label.setPadding(new Insets(0, 15, 0, 15));


                    label.setLayoutY(pane.getHeight() - 75);
                    label.setTextFill(Color.WHITE);
                    ScaleTransition scale = new ScaleTransition(Duration.millis(50), imageView);
                    ScaleTransition scale2 = new ScaleTransition(Duration.millis(50), label);
                    ScaleTransition scale3 = new ScaleTransition(Duration.millis(50), background);
                    scale.setToX(1.07);
                    scale2.setToX(1.07);
                    scale3.setToX(1.07);
                    scale.setToY(1.07);
                    scale2.setToY(1.07);
                    scale3.setToY(1.07);
                    scale.play();
                    scale2.play();
                    scale3.play();
                    pane.getChildren().add(label);
                }
        );
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, event -> {
                    final Label[] l = {null};
                    pane.getChildren().forEach(c -> {
                        if (c instanceof Label) {
                            l[0] = (Label) c;
                        }
                    });
                    final AnchorPane[] a = {null};
                    pane.getChildren().forEach(c -> {
                        if (c instanceof AnchorPane) {
                            a[0] = (AnchorPane) c;
                        }
                    });
                    pane.getChildren().removeAll(l);
                    pane.getChildren().removeAll(a);
                    ScaleTransition scale = new ScaleTransition(Duration.millis(50), imageView);
                    scale.setToX(1f);
                    scale.setToY(1f);
                    scale.play();
                }
        );

        imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
        {
            Main.mainPanel.setCenter(Main.recipePage);
            try {
                Main.controllerRecipe.setRecipe(MockDataManager.getInstance().getRecipeByName("sadasd"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        pane.getChildren().add(imageView);


    }

    private void setupGrid(GridPane gridPane) {

        column1 = new ColumnConstraints();
        column1.setPercentWidth(33.3);
        gridPane.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(33.3);
        gridPane.getColumnConstraints().add(column2);

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(33.3);
        gridPane.getColumnConstraints().add(column3);

        row1 = new RowConstraints();
        row1.setPercentHeight(33.3);
        gridPane.getRowConstraints().add(row1);

        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(33.3);
        gridPane.getRowConstraints().add(row2);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(33.3);
        gridPane.getRowConstraints().add(row3);

    }

    public void onBtnBack(ActionEvent actionEvent) {
        int i = Integer.parseInt(page.getText());
        if (i > 0) {
            i--;

            gridPane.getChildren().clear();
            addChildren(i);
            page.setText("" + i);
        }

        if (i == 0) {
            btnBack.setDisable(true);
        }

    }


    public void onBtnFront(ActionEvent actionEvent) {

        int i = Integer.parseInt(page.getText());
        i++;

        gridPane.getChildren().clear();
        addChildren(i);
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


}
