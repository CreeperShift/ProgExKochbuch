package de.fra.uas.digitales.kochbuch.backend;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DataManager implements IDataManager {

    private final Connection connection;
    private final Statement statement;

    public DataManager() throws SQLException {

        //Change user and password if necessary!
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kochbuch", "root", "progex");
        statement = connection.createStatement();

    }

    @Override
    public Recipe getRecipeByName(String name) throws SQLException {

        ResultSet resultSet = statement.executeQuery("SELECT * FROM recipe WHERE recipeName='" + name + "'");

        try {
            return getFullRecipeFromResultSet(new Recipe(), resultSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Recipe getFullRecipeFromResultSet(Recipe recipe, ResultSet resultSet) throws SQLException, IOException {
        while (resultSet.next()) {
            recipe.setName(resultSet.getString("recipeName"));
            recipe.setRating(resultSet.getInt("rating"));
            recipe.setDesc(resultSet.getString("recipeDescription"));
            recipe.setSteps(resultSet.getString("instructions"));
            recipe.setTime(resultSet.getFloat("recipeTime"));
            Blob picture = resultSet.getBlob("picture");
            recipe.setImageRaw(picture.getBytes(0, 0));
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

        try {
            return getFullRecipeFromResultSet(new Recipe(), resultSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addNewRecipe(Recipe recipe) throws SQLException {
        String insertRecipe = "insert into recipe (recipename, rating, recipeDescription, instructions, picture, recipeTime)" + "values (? , ? , ? , ? ,?, ?)";
        String insertIngredient = "insert into recipeingredients (recipe, ingredient, amount, unit)" + " values (?, ?, ?, ?)";

        PreparedStatement pState = connection.prepareStatement(insertRecipe);
        pState.setString(1, recipe.getName());
        pState.setInt(2, recipe.getRating());
        pState.setString(3, recipe.getDesc());
        pState.setString(4, recipe.getSteps());
        pState.setBinaryStream(5, new ByteArrayInputStream(recipe.getImageRaw()));
        pState.setFloat(6, recipe.getTime());
        pState.executeUpdate();
        pState.close();

        Statement getId = connection.createStatement();
        ResultSet resultSet = getId.executeQuery("SELECT id FROM recipe WHERE recipeName='" + recipe.getName() + "'");
        if (resultSet.next()) {
            int id = resultSet.getInt("id");


            PreparedStatement ingredStatement = connection.prepareStatement(insertIngredient);

            for (Ingredient i : recipe.getIngredients()) {
                ingredStatement.setInt(1, id);
                ingredStatement.setInt(2, getCreateIngredient(i));
                ingredStatement.setFloat(3, i.amount());
                ingredStatement.setString(4, i.unit());
                ingredStatement.executeUpdate();
            }

            ingredStatement.close();
        }
    }

    private int getCreateIngredient(Ingredient ing) throws SQLException {
        int ingredientID = 0;
        Statement getI = connection.createStatement();
        final ResultSet ingred = getI.executeQuery("SELECT id FROM ingredients WHERE ingredientName='" + ing.name() + "'");
        if (ingred.next()) {
            ingredientID = ingred.getInt(1);
        } else {
            String query2 = "insert into ingredients (ingredientName)" + " values (?)";
            PreparedStatement createI = connection.prepareStatement(query2);
            createI.setString(1, ing.name());
            createI.executeUpdate();
            createI.close();
        }

        final ResultSet ingred2 = getI.executeQuery("SELECT id FROM ingredients WHERE ingredientName='" + ing.name() + "'");
        if (ingred2.next()) {
            ingredientID = ingred2.getInt(1);
        }
        ingred.close();
        ingred2.close();
        return ingredientID;
    }

    @Override
    public List<Recipe> getStartRecipes(int page) throws SQLException {
        List<Recipe> rList = new LinkedList<>();
        try {
            String select9 = "SELECT * from recipe ORDER BY id LIMIT 9";
            if (page == 0) {
                PreparedStatement statement = connection.prepareStatement(select9);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    Recipe r = new Recipe();

                    r.setName(result.getString("recipeName"))
                            .setDesc(result.getString("recipeDescription"))
                            .setImageRaw(result.getBytes("picture"))
                            .setRating(result.getInt("rating"))
                            .setSteps(result.getString("instructions"))
                            .setTime(result.getFloat("recipeTime"));
                    rList.add(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rList;
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
