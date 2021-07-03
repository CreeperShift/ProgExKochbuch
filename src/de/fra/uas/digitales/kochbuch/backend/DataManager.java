package de.fra.uas.digitales.kochbuch.backend;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DataManager implements IDataManager {

    private static Connection connection;
    private static DataManager INSTANCE;
    private static String url, user, password;
    private boolean isConnected = false;

    private DataManager() {
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

    public List<String> getAllIngredientNames() throws SQLException {
        List<String> ret = new LinkedList<>();

        String getIngredName = "select ingredientName from ingredients";
        PreparedStatement pState = connection.prepareStatement(getIngredName);
        ResultSet result = pState.executeQuery();

        while (result.next()) {
            ret.add(result.getString("ingredientName"));
        }
        pState.close();
        return ret;
    }

    public List<Recipe> getRecipeList(String name, int i) throws SQLException, IOException {
        Statement statement = connection.createStatement();
        List<Recipe> recipeList = new LinkedList<>();
        ResultSet rs = statement.executeQuery("SELECT * FROM recipe WHERE recipeName LIKE '%" + name + "%' LIMIT " + i * 9 + ",9");
        while (rs.next()) {

            recipeList.add(getRecipeFromResult(rs));
        }
        statement.close();
        return recipeList;
    }

    @Override
    public Recipe getRecipeByName(String name) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM recipe WHERE recipeName='" + name + "'");
        if (resultSet.next()) {
            Recipe r = getRecipeFromResult(resultSet);
            statement.close();
            return r;
        } else {
            statement.close();
            return null;
        }

    }


    private List<Ingredient> getAllIngredients(int id) throws SQLException {
        Statement statement2 = connection.createStatement();
        Statement statement3 = connection.createStatement();

        List<Ingredient> temp = new LinkedList<>();

        ResultSet resultSetIng = statement2.executeQuery("SELECT * FROM recipeingredients WHERE recipe='" + id + "'");
        while (resultSetIng.next()) {

            ResultSet rsName = statement3.executeQuery("SELECT * FROM ingredients WHERE id='" + resultSetIng.getInt("ingredient") + "'");
            while (rsName.next()) {
                Ingredient tmp = new Ingredient(rsName.getString("ingredientName"), resultSetIng.getInt("amount"), resultSetIng.getString("unit"));
                temp.add(tmp);
            }

        }
        statement2.close();
        statement3.close();
        return temp;

    }


    @Override
    public void deleteRecipe(Recipe recipe) throws SQLException {

        String deleteRecipe = "DELETE FROM recipe WHERE id=?";
        PreparedStatement preDeleteRecipe = connection.prepareStatement(deleteRecipe);
        preDeleteRecipe.setString(1, String.valueOf(recipe.getID()));
        preDeleteRecipe.execute();
        preDeleteRecipe.close();

    }

    @Override
    public void editRecipe(Recipe recipe) throws SQLException {
        PreparedStatement preparedStatement;
        if (recipe.isHasImageChanged()) {
            String editQuery = "UPDATE recipe set recipeName = ?, rating = ?, recipeDescription = ?, instructions = ?, picture = ?, recipeTime = ?, category = ? where id = ? ";
            preparedStatement = connection.prepareStatement(editQuery);
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setInt(2, recipe.getRating());
            preparedStatement.setString(3, recipe.getDesc());
            preparedStatement.setString(4, recipe.getSteps());
            preparedStatement.setBinaryStream(5, new ByteArrayInputStream(recipe.getImageRaw()));
            preparedStatement.setFloat(6, recipe.getTime());
            preparedStatement.setString(7, recipe.getCategory());
            preparedStatement.setInt(8, recipe.getID());

        } else {
            String editQuery = "UPDATE recipe set recipeName = ?, rating = ?, recipeDescription = ?, instructions = ?, recipeTime = ?, category = ? where id = ? ";
            preparedStatement = connection.prepareStatement(editQuery);
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setInt(2, recipe.getRating());
            preparedStatement.setString(3, recipe.getDesc());
            preparedStatement.setString(4, recipe.getSteps());
            preparedStatement.setFloat(5, recipe.getTime());
            preparedStatement.setString(6, recipe.getCategory());
            preparedStatement.setInt(7, recipe.getID());

        }

        String removeIngrQuery = "Delete from recipeingredients where recipe = " + recipe.getID() + ";";
        PreparedStatement removeIngredRel = connection.prepareStatement(removeIngrQuery);

        preparedStatement.executeUpdate();
        removeIngredRel.executeUpdate();

        String insertIngredient = "insert into recipeingredients (recipe, ingredient, amount, unit)" + " values (?, ?, ?, ?)";
        PreparedStatement insertIngr = connection.prepareStatement(insertIngredient);

        for (Ingredient i : recipe.getIngredients()) {
            insertIngr.setInt(1, recipe.getID());
            insertIngr.setInt(2, getCreateIngredient(i));
            insertIngr.setFloat(3, i.amount());
            insertIngr.setString(4, i.unit());
            insertIngr.executeUpdate();
        }
        insertIngr.close();
        removeIngredRel.close();
        preparedStatement.close();

    }

    @Override
    public Recipe getRecipeByID(int id) throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM recipe WHERE id='" + id + "'");
        if (resultSet.next()) {
            Recipe r = getRecipeFromResult(resultSet);
            statement.close();
            return r;
        } else {
            statement.close();
            return null;
        }
    }

    @Override
    public void addNewRecipe(Recipe recipe) throws SQLException {
        String insertRecipe = "insert into recipe (recipename, rating, recipeDescription, instructions, picture, recipeTime, category)" + "values (? , ? , ? , ? ,?, ?, ?)";
        String insertIngredient = "insert into recipeingredients (recipe, ingredient, amount, unit)" + " values (?, ?, ?, ?)";
        PreparedStatement pState = connection.prepareStatement(insertRecipe);
        pState.setString(1, recipe.getName());
        pState.setInt(2, recipe.getRating());
        pState.setString(3, recipe.getDesc());
        pState.setString(4, recipe.getSteps());
        pState.setBinaryStream(5, new ByteArrayInputStream(recipe.getImageRaw()));
        pState.setFloat(6, recipe.getTime());
        pState.setString(7, recipe.getCategory());
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
        getId.close();
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
        String select9 = "SELECT * from recipe ORDER BY id LIMIT " + page * 9 + ",9";
        PreparedStatement statement = connection.prepareStatement(select9);
        ResultSet result = statement.executeQuery();
        while (result.next()) {

            rList.add(getRecipeFromResult(result));
        }

        statement.close();
        return rList;
    }

    @Override
    public List<Recipe> getRecipeByTag(String tag, int page) {
        List<Recipe> rList = new LinkedList<>();
        try {
            String select9 = "SELECT * from recipe where category = '" + tag + "' ORDER BY id LIMIT " + page * 9 + ",9";
            PreparedStatement statement = connection.prepareStatement(select9);
            ResultSet result = statement.executeQuery();
            while (result.next()) {

                rList.add(getRecipeFromResult(result));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rList;
    }

    @Override
    public List<Recipe> getRecipeByIngredient(List<String> ingredients, int limit, int page) throws SQLException {

        List<Recipe> recipeIDs = new LinkedList<>();

        String ingredientsString = "";
        for (int i = 0; i < ingredients.size(); i++) {
            if (i != 0) {
                ingredientsString = ingredientsString.concat(",'" + ingredients.get(i) + "'");
            } else {
                ingredientsString = ingredientsString.concat("'" + ingredients.get(i) + "'");
            }
        }

        String query = "select r.*\n" +
                "from recipeingredients ri\n" +
                "         inner join ingredients i on i.id = ri.ingredient\n" +
                "     join recipe r on r.id = ri.recipe\n" +
                "where ingredientName in (" + ingredientsString + ")\n" +
                "group by recipe\n" +
                "having count(distinct ingredientName) >= " + limit + " LIMIT " + page * 6 + " ,6;";

        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet result = statement.executeQuery();

        while (result.next()) {
            recipeIDs.add(getRecipeFromResult(result));
        }

        statement.close();
        result.close();

        return recipeIDs;
    }

    @Override
    public List<String> getCategories() throws SQLException {

        String query = "select distinct category from recipe";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet res = preparedStatement.executeQuery();
        List<String> categoryList = new LinkedList<>();
        while (res.next()) {
            categoryList.add(res.getString("category"));
        }

        return categoryList;
    }

    @Override
    public void setFavorite(int id, boolean isFav) throws SQLException {
        String query = "update recipe set isFavorite = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(2, id);
        preparedStatement.setBoolean(1, isFav);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Recipe> getRecipeFav(int page) {
        List<Recipe> rList = new LinkedList<>();
        try {
            String select9 = "SELECT * from recipe where isFavorite = true LIMIT " + page * 9 + ",9";
            PreparedStatement statement = connection.prepareStatement(select9);
            ResultSet result = statement.executeQuery();
            while (result.next()) {

                rList.add(getRecipeFromResult(result));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rList;
    }

    private Recipe getRecipeFromResult(ResultSet result) {
        Recipe r = new Recipe();

        try {
            r.setName(result.getString("recipeName"))
                    .setDesc(result.getString("recipeDescription"))
                    .setId(result.getInt("id"))
                    .setImageRaw(result.getBytes("picture"))
                    .setRating(result.getInt("rating"))
                    .setCategory(result.getString("category"))
                    .setSteps(result.getString("instructions"))
                    .setFav(result.getBoolean("isFavorite"))
                    .setIngredients(getAllIngredients(result.getInt("id")))
                    .setTime(result.getFloat("recipeTime"));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public void stopConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        isConnected = false;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        isConnected = true;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
