package de.fra.uas.digitales.kochbuch.backend;

import java.util.List;

public interface IDataManager {

    IRecipe getRecipeByName(String name);

    IRecipe deleteRecipe(IRecipe recipe);

    void editRecipe(IRecipe recipe);

    IRecipe getRecipeByID(int id);

    void addNewRecipe(IRecipe iRecipe);

    List<IRecipe> getStartRecipes(int page);

    List<IRecipe> getRecipeByTag(List<String> tags, int page);

    List<IRecipe> getRecipeByIngredient(List<String> ingredients, int page);

    //TODO: SEARCH

}
