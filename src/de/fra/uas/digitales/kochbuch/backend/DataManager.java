package de.fra.uas.digitales.kochbuch.backend;

import java.sql.*;
import java.util.List;

public class DataManager implements IDataManager {

    private final Connection connection;
    private final Statement statement;

    public DataManager() throws SQLException {

        //Change user and password if necessary!
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kochbuch", "root", "toor");
        statement = connection.createStatement();

    }

    @Override
    public Recipe getRecipeByName(String name) throws SQLException {

        ResultSet resultSet = statement.executeQuery("SELECT * FROM recipe WHERE recipeName='" + name + "'");

        return getFullRecipeFromResultSet(new Recipe(), resultSet);
    }


    private Recipe getFullRecipeFromResultSet(Recipe recipe, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            recipe.setName(resultSet.getString("recipeName"));
            recipe.setRating(resultSet.getInt("rating"));
            recipe.setDesc(resultSet.getString("recipeDescription"));
            recipe.setSteps(resultSet.getString("instructions"));
            recipe.setTime(resultSet.getFloat("recipeTime"));
            //TODO: picture
            //TODO: Ingredients
        }
        return recipe;
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
    }

    @Override
    public void editRecipe(Recipe recipe) {

    }

    @Override
    public Recipe getRecipeByID(int id) throws SQLException {

        ResultSet resultSet = statement.executeQuery("SELECT * FROM recipe WHERE id='" + id + "'");

        return getFullRecipeFromResultSet(new Recipe(), resultSet);
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
