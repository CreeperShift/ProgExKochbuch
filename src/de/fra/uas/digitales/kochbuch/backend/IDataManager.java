package de.fra.uas.digitales.kochbuch.backend;

import java.util.List;

public interface IDataManager {

    /**
     * Finds a recipe matching a String.
     * <p>
     * Will query the database for this name. Returns null if not found.
     * @param name name of the recipe
     * @return Recipe object if found
     * @see Recipe
     */
    Recipe getRecipeByName(String name);

    /**
     * Deletes matching recipe Object from the database.
     * <p>
     * Nothing happens if the object is not found.
     * </p>
     * @param recipe Recipe object to be deleted from the database
     * @see Recipe
     */
    void deleteRecipe(Recipe recipe);

    /**
     * Edits recipe by entering only changed values into the database.
     * @param recipe Recipe object to edit
     * @see Recipe
     */
    void editRecipe(Recipe recipe);

    /**
     * Finds a recipe by its ID.
     * <p>
     * Will query the database for this ID. Returns null if not found.
     * @param id id of the Recipe
     * @return Recipe object if found
     * @see Recipe
     */
    Recipe getRecipeByID(int id);

    /**
     * Adds a new recipe to the database
     * @param recipe Recipe Object to be added
     * @see Recipe
     */
    void addNewRecipe(Recipe recipe);

    /**
     * Gets exactly 9 sorted recipes from the database for the
     * starting screen. Specifying the page number allows for subsequent recipes.
     * Each page contains 9 recipes so every subsequent page returns the 9 subsequent recipes.
     * @param page which 9 recipes should be returned.
     * @return A list of 9 (or less) recipes.
     */
    List<Recipe> getStartRecipes(int page);

    /**
     * Same as getStartRecipes(); but the tags allow additional filtering.
     * @param page which 9 recipes should be returned.
     * @param tags a list of tags to be filtered for in the database
     * @return A list of 9 (or less) recipes.
     */
    List<Recipe> getRecipeByTag(List<String> tags, int page);

    /**
     * Same as getStartRecipes(); but the ingredients allow additional filtering.
     * @param page which 9 recipes should be returned.
     * @param ingredients a list of ingredients to be filtered for in the database
     * @return A list of 9 (or less) recipes.
     */
    List<Recipe> getRecipeByIngredient(List<String> ingredients, int page);

    //TODO: SEARCH

}
