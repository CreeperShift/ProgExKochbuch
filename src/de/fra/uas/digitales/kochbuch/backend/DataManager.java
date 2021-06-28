package de.fra.uas.digitales.kochbuch.backend;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DataManager implements IDataManager {

    private final Connection connection;
    private final Statement statement;
    private final Statement statement2;
    private final Statement statement3;
    private final Statement statement4;

    public DataManager() throws SQLException {

        //Change user and password if necessary!
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kochbuch", "root", "toor");
        statement = connection.createStatement();
        statement2 = connection.createStatement();
        statement3 = connection.createStatement();
        statement4 = connection.createStatement();

    }

    public List<Recipe> getRecipeList(String name) throws SQLException, IOException {
        List<Recipe> recipeList = new LinkedList<>();
        ResultSet rs = statement4.executeQuery("SELECT * FROM recipe WHERE recipeName LIKE '%" + name + "%';");
        while(rs.next()){
            Recipe recipe = new Recipe();
            recipe.setName(rs.getString("recipeName"));
            recipe.setRating(rs.getInt("rating"));
            recipe.setDesc(rs.getString("recipeDescription"));
            recipe.setSteps(rs.getString("instructions"));
            recipe.setTime(rs.getFloat("recipeTime"));
            recipe.setId(rs.getInt("id"));
            Blob picture = rs.getBlob("picture");
            if(picture!=null){
                BufferedInputStream bis = new BufferedInputStream(picture.getBinaryStream());
                BufferedImage bi = ImageIO.read(bis);
                Image im = SwingFXUtils.toFXImage(bi, null);
                recipe.setBild(im);
            }
            int idIng = rs.getInt("id");
            recipe.setIngredients(getAllIngredients(idIng));
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
            Blob picture = resultSet.getBlob("picture");
            if(picture!=null){
                BufferedInputStream bis = new BufferedInputStream(picture.getBinaryStream());
                BufferedImage bi = ImageIO.read(bis);
                Image im = SwingFXUtils.toFXImage(bi, null);
                recipe.setBild(im);
            }
            int idIng = resultSet.getInt("id");
            recipe.setIngredients(getAllIngredients(idIng));

        }
        return recipe;
    }

    private List<Ingredient> getAllIngredients(int id) throws SQLException {


        List<Ingredient> temp = new LinkedList<>();

        ResultSet resultSetIng = statement2.executeQuery("SELECT * FROM recipeingredients WHERE recipe='" + id + "'");
        while(resultSetIng.next()){

            ResultSet rsName = statement3.executeQuery("SELECT * FROM ingredients WHERE id='" + resultSetIng.getInt("ingredient") + "'");
            while (rsName.next()){
                Ingredient tmp = new Ingredient(rsName.getString("ingredientName"), resultSetIng.getInt("amount"), resultSetIng.getString("unit"));
                temp.add(tmp);
            }

        }

        return temp;

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
