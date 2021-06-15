package de.fra.uas.digitales.kochbuch.backend;

import java.util.List;

public class DataManager implements IDataManager {


    @Override
    public Recipe getRecipeByName(String name) {
        return null;
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
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
