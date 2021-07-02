package de.fra.uas.digitales.kochbuch;

import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.frontend.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    public static VBox settingsPage;
    public static ControllerRecipe controllerRecipe;
    public static ControllerEditRecipe controllerEditRecipe;
    public static ControllerBase controllerBase;
    public static Stage stage;
    public static ControllerNewRecipe controllerNewRecipe;
    public static ControllerStartLayout controllerStartLayout;
    public static ControllerFilter controllerFilter;
    public static ControllerSettings controllerSettings;

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader loaderBase = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/baseLayout.fxml")));
        mainPanel = loaderBase.load();
        controllerBase = loaderBase.getController();

        FXMLLoader loaderStart = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/startLayout.fxml")));
        startPane = loaderStart.load();
        controllerStartLayout = loaderStart.getController();
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
        controllerFilter = loaderFilter.getController();

        FXMLLoader loaderSettings = new FXMLLoader(Objects.requireNonNull(getClass().getResource("frontend/fxml/SettingsLayout.fxml")));
        settingsPage = loaderSettings.load();
        controllerSettings = loaderSettings.getController();


        Path path = Paths.get("Database.info");
        if (Files.exists(path)) {
            try {
                DataManager.setDatabase(Files.readAllLines(path).get(0), Files.readAllLines(path).get(1), Files.readAllLines(path).get(2));
                DataManager.get().connect();
                mainPanel.setCenter(startPane);
                controllerStartLayout.startConnected();
                controllerFilter.startConnected();
            } catch (Exception e) {
                System.out.println("Database info not found or incorrect, redirecting to settings page.");
                mainPanel.setCenter(settingsPage);
                controllerBase.btnSettings.fire();
            }
        } else {
            mainPanel.setCenter(settingsPage);
            controllerBase.btnSettings.fire();
        }

        primaryStage.setTitle("Digitales Kochbuch");
        Scene s = new Scene(mainPanel, 1280, 880);
        s.getStylesheets().add("de/fra/uas/digitales/kochbuch/frontend/fxml/main.css");
        primaryStage.setScene(s);
        primaryStage.show();
        stage = primaryStage;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (controllerSettings.isConnected()) {
            DataManager.get().stopConnection();
        }
    }

    public static void main(String[] args) {

        launch(args);

    }
}
