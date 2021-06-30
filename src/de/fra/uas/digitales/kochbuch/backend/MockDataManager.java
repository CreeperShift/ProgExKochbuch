package de.fra.uas.digitales.kochbuch.backend;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/*
Mock implementation for IDataManager without the database.
This class can be used for local testing.
A fake recipe is created, but as soon as the user adds a recipe, all the fake recipes are instead replaced by this new recipe.
 */


public class MockDataManager implements IDataManager {

    private static IDataManager INSTANCE = null;
    private static Recipe mockRecipe;
    private static final List<Ingredient> ingredientList = new LinkedList<>();
    private static final List<String> stepList = new LinkedList<>();
    private static final List<Recipe> fakeRecipeList = new LinkedList<>();

    private MockDataManager() {

    }

    public static IDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDataManager();
            mockRecipe = new Recipe();
            try {
                setupRecipes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    private static void setupRecipes() throws IOException {
        File file = new File("resources/images/flammkuchen.jpg");

        ingredientList.add(new Ingredient("Mehl", 200, "g"));
        ingredientList.add(new Ingredient("Wasser", 50, "ml"));
        ingredientList.add(new Ingredient("Salz", 1, "g"));
        ingredientList.add(new Ingredient("Hefe", 2, "g"));
        ingredientList.add(new Ingredient("Creme Fraiche", 1, "Becher"));
        ingredientList.add(new Ingredient("Zwiebel", 1));
        ingredientList.add(new Ingredient("Speck", 1, "Packung"));

        stepList.add("Mehl, Salz und Zucker in einer Schüssel mischen");
        stepList.add("Hefe zerbröckeln, daruntermischen. Wasser dazugiessen, zu einem weichen, glatten Teig kneten. Zugedeckt bei Raumtemperatur ca. 1 Std. aufs Doppelte aufgehen lassen.");
        stepList.add("Teig auf wenig Mehl oval, ca. 3 mm dick auswallen, in ein mit Backpapier belegtes Blech legen.");
        stepList.add("Ofen auf 240 Grad vorheizen.");
        stepList.add("Crème fraîche auf dem Teig verteilen, dabei ringsum einen Rand von ca. 1 cm frei lassen. Zwiebel schälen, in feine Ringe schneiden, mit den Speckwürfeli auf der Crème fraîche verteilen, würzen.");


        mockRecipe.setId(1)
                .setDesc("Diese leckere Spezialität aus dem Elsass eignet sich sehr gut auch zum Apéro. Dünn ausgewallter Brotteig mit Sauerrahm, Zwiebeln und Speckwürfeli!")
                //.setImageRaw(file)
                .setIngredients(ingredientList)
                .setRating(3)
                .setTime(15.0f)
                //.setSteps(stepList.toString())
                .setName("Flammkuchen");


        /*
        Start at 2 -> 11 because the first mockRecipe is ID 1
         */
        for (int i = 2; i < 11; i++) {
            Recipe r = new Recipe()
                    .setId(1)
                    .setDesc("Diese leckere Spezialität aus dem Elsass eignet sich sehr gut auch zum Apéro. Dünn ausgewallter Brotteig mit Sauerrahm, Zwiebeln und Speckwürfeli!")
                    .setIngredients(ingredientList)
                    .setRating(3)
                    .setTime(15.0f)
                    //.setSteps(stepList.toString())
                    .setName("Flammkuchen");
                    //.setImageRaw(file);
            fakeRecipeList.add(r);
        }


    }


    @Override
    public Recipe getRecipeByName(String name) {
        return mockRecipe;
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
    }

    @Override
    public void editRecipe(Recipe recipe) {
    }

    @Override
    public Recipe getRecipeByID(int id) {
        return mockRecipe;
    }

    @Override
    public void addNewRecipe(Recipe iRecipe) {
        mockRecipe = iRecipe;
        fakeRecipeList.clear();
        for(int i = 0; i < 9; i++){
            fakeRecipeList.add(iRecipe);
        }
    }

    @Override
    public final List<Recipe> getStartRecipes(int page) {
        return fakeRecipeList;
    }

    @Override
    public final List<Recipe> getRecipeByTag(List<String> tags, int page) {
        return fakeRecipeList;
    }

    @Override
    public final List<Recipe> getRecipeByIngredient(List<String> ingredients, int page) {
        return fakeRecipeList;
    }
}
