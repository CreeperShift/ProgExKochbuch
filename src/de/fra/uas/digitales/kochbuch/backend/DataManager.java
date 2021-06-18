package de.fra.uas.digitales.kochbuch.backend;

import java.sql.*;
import java.util.List;

public class DataManager implements IDataManager {

    private Connection connection;
    private Statement statement;

    public DataManager() throws SQLException {

        //Change user and password if necessary!
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kochbuch","root", "toor");
        statement = connection.createStatement();

    }

    @Override
    public Recipe getRecipeByName(String name) throws SQLException {

        ResultSet resultSet = statement.executeQuery("SELECT * FROM recipe WHERE recipeName='" + name + "'");

        Recipe recipe = new Recipe();

        while(resultSet.next()){
            recipe.recipeName = resultSet.getString("recipeName");
            recipe.rating = resultSet.getInt("rating");
            recipe.recipeDescription = resultSet.getString("recipeDescription");
            recipe.instructions = resultSet.getString("instructions");
            recipe.recipeTime = resultSet.getInt("recipeTime");
            //to do picture
        }
        return recipe;
    }

    @Override
    public IRecipe deleteRecipe(IRecipe recipe) {
        return null;
    }

    @Override
    public void editRecipe(IRecipe recipe) {

    }

    @Override
    public Recipe getRecipeByID(int id) throws SQLException {

        ResultSet resultSet = statement.executeQuery("SELECT * FROM recipe WHERE id='" + id + "'");
        Recipe recipe = new Recipe();

        while(resultSet.next()){
            recipe.recipeName = resultSet.getString("recipeName");
            recipe.rating = resultSet.getInt("rating");
            recipe.recipeDescription = resultSet.getString("recipeDescription");
            recipe.instructions = resultSet.getString("instructions");
            recipe.recipeTime = resultSet.getInt("recipeTime");
            //to do picture
        }

        return recipe;
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
