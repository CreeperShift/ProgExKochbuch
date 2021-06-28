package de.fra.uas.digitales.kochbuch;

import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.frontend.ControllerBase;
import de.fra.uas.digitales.kochbuch.frontend.ControllerNewRecipeLayout;
import de.fra.uas.digitales.kochbuch.frontend.ControllerRecipeLayoutNeu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


public class Main extends Application {

    /*
    True means we use the MockDataManger.
     */
    public static BorderPane mainPanel;
    public static AnchorPane startPane;
    public static AnchorPane recipePage;
    public static VBox filterPage;
    public static VBox newRecipePage;
    public static AnchorPane settingsPage;
    public static ControllerRecipeLayoutNeu controllerRecipe;
    public static ControllerBase controllerBase;
    public static Stage stage;
    public static ControllerNewRecipeLayout controllerNewRecipe;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Path path = Paths.get("resources/Database.info");

        DataManager.setDatabase(Files.readAllLines(path).get(0), Files.readAllLines(path).get(1), Files.readAllLines(path).get(2));



        FXMLLoader loaderBase = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/baseLayout.fxml")));
        mainPanel = loaderBase.load();
        controllerBase = loaderBase.getController();

        startPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("frontend/fxml/startLayout.fxml")));
        mainPanel.setCenter(startPane);

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/recipeLayoutNeu.fxml")));
        recipePage = loader.load();
        controllerRecipe = loader.getController();

        FXMLLoader loaderNewRecipe = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/newRecipeLayout.fxml")));
        newRecipePage = loaderNewRecipe.load();

        FXMLLoader loaderFilter = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/filterLayout.fxml")));
        filterPage = loaderFilter.load();

        FXMLLoader loaderSettings = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/SettingsLayout.fxml")));
        settingsPage = loaderSettings.load();


        primaryStage.setTitle("Digitales Kochbuch");
        primaryStage.setScene(new Scene(mainPanel, 1280, 700));
        primaryStage.show();
        stage = primaryStage;
    }


    public static void main(String[] args) {

        launch(args);

    }
}


