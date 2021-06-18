package de.fra.uas.digitales.kochbuch.backend;

import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Recipe {

    private int id = -1;
    private String name = "";
    private String desc = "";
    private Image image = null;
    private List<Ingredient> ingredients = new LinkedList<>();
    private byte[] imageRaw;
    private String steps;
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


    public Recipe setImageRaw(File file) throws IOException {
        imageRaw = Files.readAllBytes(file.toPath());
        return this;
    }

    public Recipe setImageRaw(byte[] bytes) throws IOException {
        imageRaw = bytes;
        return this;
    }

    public Recipe setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Recipe setSteps(String steps) {
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
        if(image == null){
            image = new Image(new ByteArrayInputStream(imageRaw));
        }
        return image;
    }

    public byte[] getImageRaw() {
        return imageRaw;
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

    public String getSteps() {
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
