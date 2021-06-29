package de.fra.uas.digitales.kochbuch.backend;

import de.fra.uas.digitales.kochbuch.Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DataManager implements IDataManager {

    private Connection connection;
    private Statement statement;
    private Statement statement2;
    private Statement statement3;
    private Statement statement4;
    private Statement statement5;
    private static DataManager INSTANCE;
    private static String url, user, password;

    private DataManager() {

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement2 = connection.createStatement();
            statement = connection.createStatement();
            statement3 = connection.createStatement();
            statement4 = connection.createStatement();
            statement5 = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static DataManager get() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public static void setDatabase(String surl, String suser, String spassword) {
        url = surl;
        user = suser;
        password = spassword;
    }

    public List<Recipe> getRecipeList(String name, int i) throws SQLException, IOException {
        List<Recipe> recipeList = new LinkedList<>();
        ResultSet rs = statement4.executeQuery("SELECT * FROM recipe WHERE recipeName LIKE '%" + name + "%' LIMIT " + i * 9 + ",9");
        while (rs.next()) {
            Recipe recipe = new Recipe();
            recipe.setName(rs.getString("recipeName"));
            recipe.setRating(rs.getInt("rating"));
            recipe.setDesc(rs.getString("recipeDescription"));
            recipe.setSteps(rs.getString("instructions"));
            recipe.setTime(rs.getFloat("recipeTime"));
            recipe.setId(rs.getInt("id"));
            recipe.setImageRaw(rs.getBytes("picture"));
            recipe.setIngredients(getAllIngredients(rs.getInt("id")));
            recipeList.add(recipe);
        }
        return recipeList;
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
            recipe.setImageRaw(resultSet.getBytes("picture"));
            recipe.setIngredients(getAllIngredients(resultSet.getInt("id")));
        }
        return recipe;
    }

    private List<Ingredient> getAllIngredients(int id) throws SQLException {


        List<Ingredient> temp = new LinkedList<>();

        ResultSet resultSetIng = statement2.executeQuery("SELECT * FROM recipeingredients WHERE recipe='" + id + "'");
        while (resultSetIng.next()) {

            ResultSet rsName = statement3.executeQuery("SELECT * FROM ingredients WHERE id='" + resultSetIng.getInt("ingredient") + "'");
            while (rsName.next()) {
                Ingredient tmp = new Ingredient(rsName.getString("ingredientName"), resultSetIng.getInt("amount"), resultSetIng.getString("unit"));
                temp.add(tmp);
            }

        }

        return temp;

    }


    @Override
    public void deleteRecipe(Recipe recipe) throws SQLException {

        String deleteRecipe = "DELETE FROM recipe WHERE recipeName=?";
        PreparedStatement preDeleteRecipe = connection.prepareStatement(deleteRecipe);
        preDeleteRecipe.setString(1, recipe.getName());
        preDeleteRecipe.execute();
        System.out.println("Rezept wurde gelöscht!");


        Main.controllerBase.btnStart.fire();



    }

    @Override
    public void editRecipe(Recipe recipeNeu) throws SQLException {

        Recipe recipeAlt = getRecipeByName(recipeNeu.getName());
        if(recipeAlt!=null){
            deleteRecipe(recipeAlt);
            System.out.println("Altes Rezept wurde gelöscht!");
        }

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
            String select9 = "SELECT * from recipe ORDER BY id LIMIT " + page * 9 + ",9";
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
