package de.fra.uas.digitales.kochbuch.frontend;

import de.fra.uas.digitales.kochbuch.Main;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import javafx.animation.ScaleTransition;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerStartLayout implements Initializable {
    public GridPane gridPane;
    public VBox vBox;
    private ColumnConstraints column1;
    private RowConstraints row1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gridPane = new GridPane();
        gridPane.setPrefHeight(5000);
        gridPane.getStyleClass().add("grid");
        setupGrid(gridPane);
        vBox.getChildren().add(1, gridPane);

        List<Recipe> startRecipes = Main.dataManager.getStartRecipes(0);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Pane pane = new Pane();
                pane.prefWidthProperty().bind(column1.prefWidthProperty());
                pane.prefHeightProperty().bind(row1.prefHeightProperty());

                    ImageView imageView = new ImageView(startRecipes.get(i).getImage());
                    imageView.fitHeightProperty().bind(pane.heightProperty().subtract(15));
                    imageView.fitWidthProperty().bind(pane.widthProperty().subtract(15));
                    imageView.getStyleClass().add("startPicture");
                    
                    setupImageListeners(pane, imageView, startRecipes.get(i).getName());

                gridPane.add(pane, i, j);
            }
        }


    }

    private void setupImageListeners(Pane pane, ImageView imageView, String name) {

        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                    Label label = new Label("Flammkuchen");
                    label.setFont(Font.font("Segeo UI", 20));
                    label.setLayoutY(pane.getHeight()-50);
                    label.setTextFill(Color.WHITE);
                    ScaleTransition scale = new ScaleTransition(Duration.millis(50), imageView);
                    scale.setToX(1.07);
                    scale.setToY(1.07);
                    scale.play();
                    pane.getChildren().add(label);
                }
        );
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, event -> {
                    final Label[] l = {null};
                    pane.getChildren().forEach(c -> {
                        if (c instanceof Label){
                            l[0] = (Label) c;
                        }
                    });
                    pane.getChildren().removeAll(l);
                    ScaleTransition scale = new ScaleTransition(Duration.millis(50), imageView);
                    scale.setToX(1f);
                    scale.setToY(1f);
                    scale.play();
                }
        );

        imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
        {
            Main.mainPanel.setCenter(Main.recipePage);
            Main.controllerRecipe.setRecipe();
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
}
