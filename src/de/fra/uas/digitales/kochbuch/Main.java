package de.fra.uas.digitales.kochbuch;

import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.IDataManager;
import de.fra.uas.digitales.kochbuch.backend.MockDataManager;
import de.fra.uas.digitales.kochbuch.frontend.ControllerBase;
import de.fra.uas.digitales.kochbuch.frontend.ControllerRecipe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    /*
    True means we use the MockDataManger.
     */
    public static final boolean isLocal = true;

    public static IDataManager dataManager;
    public static BorderPane mainPanel;
    public static AnchorPane startPane;
    public static ScrollPane recipePage;
    public static VBox newRecipePage;
    public static ControllerRecipe controllerRecipe;
    public static ControllerBase controllerBase;

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (isLocal) {
            dataManager = MockDataManager.getInstance();
        } else {
            dataManager = new DataManager();
        }

        FXMLLoader loaderBase = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/baseLayout.fxml")));
        mainPanel = loaderBase.load();
        controllerBase = loaderBase.getController();

        startPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("frontend/fxml/startLayout.fxml")));
        mainPanel.setCenter(startPane);

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/recipeLayout.fxml")));
        recipePage = loader.load();
        controllerRecipe = loader.getController();

        newRecipePage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("frontend/fxml/newRecipeLayout.fxml")));

        primaryStage.setTitle("Digitales Kochbuch");
        primaryStage.setScene(new Scene(mainPanel, 1280, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
