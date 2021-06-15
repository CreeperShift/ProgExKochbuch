package de.fra.uas.digitales.kochbuch.backend;

import java.util.List;

public interface IDataManager {

    Recipe getRecipeByName(String name);

    void deleteRecipe(Recipe recipe);

    void editRecipe(Recipe recipe);

    Recipe getRecipeByID(int id);

    void addNewRecipe(Recipe iRecipe);

    List<Recipe> getStartRecipes(int page);

    List<Recipe> getRecipeByTag(List<String> tags, int page);

    List<Recipe> getRecipeByIngredient(List<String> ingredients, int page);

    //TODO: SEARCH

}
