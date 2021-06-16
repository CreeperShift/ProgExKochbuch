package de.fra.uas.digitales.kochbuch.backend;

import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Recipe {

    private int id = -1;
    private String name = "";
    private String desc = "";
    private Image image = null;
    private List<Ingredient> ingredients = new LinkedList<>();
    private List<String> steps = new LinkedList<>();
    private int rating = 0;
    private float time = 0;

    public Recipe setId(int id) {
        this.id = id;
        return this;
    }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public Recipe setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public Recipe setImage(Image image) {
        this.image = image;
        return this;
    }

    public Recipe setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Recipe setSteps(List<String> steps) {
        this.steps = steps;
        return this;
    }

    public Recipe setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public Recipe setTime(float time) {
        this.time = time;
        return this;
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<String> getIngredientNames() {
        return ingredients.stream().map(Ingredient::name).collect(Collectors.toList());
    }

    public final List<String> getFormattedIngredients() {

        return ingredients.stream().map(Ingredient::toString).collect(Collectors.toList());
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
