package de.fra.uas.digitales.kochbuch;

import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.frontend.ControllerBase;
import de.fra.uas.digitales.kochbuch.frontend.ControllerEditRecipe;
import de.fra.uas.digitales.kochbuch.frontend.ControllerNewRecipe;
import de.fra.uas.digitales.kochbuch.frontend.ControllerRecipe;
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

    public static BorderPane mainPanel;
    public static VBox startPane;
    public static VBox recipePage;
    public static VBox filterPage;
    public static VBox newRecipePage;
    public static VBox editRecipePage;
    public static AnchorPane settingsPage;
    public static ControllerRecipe controllerRecipe;
    public static ControllerEditRecipe controllerEditRecipe;
    public static ControllerBase controllerBase;
    public static Stage stage;
    public static ControllerNewRecipe controllerNewRecipe;

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
        controllerNewRecipe = loaderNewRecipe.getController();

        FXMLLoader loaderEditRecipe = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/editRecipeLayout.fxml")));
        editRecipePage = loaderEditRecipe.load();
        controllerEditRecipe = loaderEditRecipe.getController();

        FXMLLoader loaderFilter = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/filterLayout.fxml")));
        filterPage = loaderFilter.load();
        FXMLLoader loaderSettings = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/SettingsLayout.fxml")));
        settingsPage = loaderSettings.load();

        primaryStage.setTitle("Digitales Kochbuch");
        Scene s = new Scene(mainPanel, 1280, 700);
        s.getStylesheets().add("de/fra/uas/digitales/kochbuch/frontend/fxml/main.css");
        primaryStage.setScene(s);
        primaryStage.show();
        stage = primaryStage;
    }

    public static void main(String[] args) {

        launch(args);

    }
}
