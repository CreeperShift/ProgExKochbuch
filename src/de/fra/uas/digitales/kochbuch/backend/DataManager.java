package de.fra.uas.digitales.kochbuch.backend;

import java.util.List;

public class DataManager implements IDataManager {


    @Override
    public IRecipe getRecipeByName(String name) {
        return null;
    }

    @Override
    public IRecipe deleteRecipe(IRecipe recipe) {
        return null;
    }

    @Override
    public void editRecipe(IRecipe recipe) {

    }

    @Override
    public IRecipe getRecipeByID(int id) {
        return null;
    }

    @Override
    public void addNewRecipe(IRecipe iRecipe) {

    }

    @Override
    public List<IRecipe> getStartRecipes(int page) {
        return null;
    }

    @Override
    public List<IRecipe> getRecipeByTag(List<String> tags, int page) {
        return null;
    }

    @Override
    public List<IRecipe> getRecipeByIngredient(List<String> ingredients, int page) {
        return null;
    }
}
