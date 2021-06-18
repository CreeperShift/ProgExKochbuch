package de.fra.uas.digitales.kochbuch.backend;

import javafx.scene.image.Image;

import java.util.List;

public interface IRecipe {

    int getID();

    String getName();

    String getDesc();

    Image getImage();

    String getInstructions();

    List<String> getIngredients();

    List<String> getSteps();

    int getRating();

    int getTime();

}
