package de.fra.uas.digitales.kochbuch.backend;

import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.List;

public class Recipe{

    private int id = -1;
    private String name = "";
    private String desc = "";
    private Image image = null;
    private List<String> ingredients = new LinkedList<>();
    private List<String> steps = new LinkedList<>();
    private int rating = 0;
    private float time = 0;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Image getImage() {
        return image;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public int getRating() {
        return rating;
    }

    public float getTime() {
        return time;
    }

    public Recipe() {
    }


}
