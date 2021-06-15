package de.fra.uas.digitales.kochbuch.backend;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class MockDataManager implements IDataManager {

    private static IDataManager INSTANCE = null;
    private static Recipe mockRecipe;
    private static final List<String> ingredientList = new LinkedList<>();
    private static final List<String> stepList = new LinkedList<>();

    private MockDataManager() {

    }

    public static IDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDataManager();
            mockRecipe = new Recipe();
            try {
                setupRecipes();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    private static void setupRecipes() throws FileNotFoundException {
        FileInputStream pic = null;
        pic = new FileInputStream("resources/images/flammkuchen.jpg");

        ingredientList.add("200g Mehl");
        ingredientList.add("50ml Wasser");
        ingredientList.add("1g Salz");
        ingredientList.add("2g Hefe");
        ingredientList.add("1 Becher Creme Fraiche");
        ingredientList.add("1 Zwiebel");
        ingredientList.add("1 Packung Speck");

        stepList.add("Mehl, Salz und Zucker in einer Schüssel mischen");
        stepList.add("Hefe zerbröckeln, daruntermischen. Wasser dazugiessen, zu einem weichen, glatten Teig kneten. Zugedeckt bei Raumtemperatur ca. 1 Std. aufs Doppelte aufgehen lassen.");
        stepList.add("Teig auf wenig Mehl oval, ca. 3 mm dick auswallen, in ein mit Backpapier belegtes Blech legen.");
        stepList.add("Ofen auf 240 Grad vorheizen.");
        stepList.add("Crème fraîche auf dem Teig verteilen, dabei ringsum einen Rand von ca. 1 cm frei lassen. Zwiebel schälen, in feine Ringe schneiden, mit den Speckwürfeli auf der Crème fraîche verteilen, würzen.");


        Image image = new Image(pic);
        mockRecipe.setId(1)
                .setDesc("Diese leckere Spezialität aus dem Elsass eignet sich sehr gut auch zum Apéro. Dünn ausgewallter Brotteig mit Sauerrahm, Zwiebeln und Speckwürfeli!")
                .setImage(image)
                .setIngredients(ingredientList)
                .setRating(3)
                .setTime(15.0f)
                .setSteps(stepList)
                .setName("Flammkuchen");

    }


    @Override
    public Recipe getRecipeByName(String name) {
        return mockRecipe;
    }

    @Override
    public Recipe deleteRecipe(Recipe recipe) {
        return null;
    }

    @Override
    public void editRecipe(Recipe recipe) {

    }

    @Override
    public Recipe getRecipeByID(int id) {
        return null;
    }

    @Override
    public void addNewRecipe(Recipe iRecipe) {

    }

    @Override
    public List<Recipe> getStartRecipes(int page) {
        return null;
    }

    @Override
    public List<Recipe> getRecipeByTag(List<String> tags, int page) {
        return null;
    }

    @Override
    public List<Recipe> getRecipeByIngredient(List<String> ingredients, int page) {
        return null;
    }
}
