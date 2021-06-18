package de.fra.uas.digitales.kochbuch.backend;

import java.sql.SQLException;
import java.util.List;

public interface IDataManager {

    IRecipe getRecipeByName(String name) throws SQLException;

    IRecipe deleteRecipe(IRecipe recipe);

    void editRecipe(IRecipe recipe);

    IRecipe getRecipeByID(int id) throws SQLException;

    void addNewRecipe(IRecipe iRecipe);

    List<IRecipe> getStartRecipes(int page);

    List<IRecipe> getRecipeByTag(List<String> tags, int page);

    List<IRecipe> getRecipeByIngredient(List<String> ingredients, int page);

    //TODO: SEARCH

}
