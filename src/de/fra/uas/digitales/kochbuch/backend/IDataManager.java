package de.fra.uas.digitales.kochbuch.backend;

import java.sql.SQLException;
import java.util.List;

public interface IDataManager {

    Recipe getRecipeByName(String name) throws SQLException;

    void deleteRecipe(Recipe recipe) throws SQLException;

    void editRecipe(Recipe recipe) throws SQLException;

    Recipe getRecipeByID(int id) throws SQLException;

    void addNewRecipe(Recipe iRecipe) throws SQLException;

    List<Recipe> getStartRecipes(int page) throws SQLException;

    List<Recipe> getRecipeByTag(List<String> tags, int page) throws SQLException;

    List<Recipe> getRecipeByIngredient(List<String> ingredients, int page) throws SQLException;

    //TODO: SEARCH

}
