package de.fra.uas.digitales.kochbuch.backend;

import javafx.scene.image.Image;

import java.util.List;

public class Recipe implements IRecipe {

    int id;
    String recipeName;
    int rating;
    String recipeDescription;
    String instructions;
    //to do picture
    int recipeTime;

    public int getID(){
        return id;
    }

    public String getName(){
        return recipeName;
    }

    public String getDesc(){
        return recipeDescription;
    }

    //to do
    public Image getImage(){
        return null;
    }

    public String getInstructions() {
        return instructions;
    }

    //to do
    public List<String> getIngredients(){
        return null;
    }

    //to do
    public List<String> getSteps(){
        return null;
    }

    public int getRating(){
        return rating;
    }

    public int getTime(){
        return recipeTime;
    }

}
