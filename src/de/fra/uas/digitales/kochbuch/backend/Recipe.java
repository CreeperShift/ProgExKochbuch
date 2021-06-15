package de.fra.uas.digitales.kochbuch.backend;

import javafx.scene.image.Image;

import java.util.List;

public class Recipe implements IRecipe{
    @Override
    public int getID() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public List<String> getIngredients() {
        return null;
    }

    @Override
    public List<String> getSteps() {
        return null;
    }

    @Override
    public int getRating() {
        return 0;
    }

    @Override
    public int getTime() {
        return 0;
    }
}
