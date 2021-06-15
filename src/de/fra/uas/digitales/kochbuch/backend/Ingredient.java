package de.fra.uas.digitales.kochbuch.backend;

/*
Records are classes in Java16 which have getters + setters+constructor already and only accept data.
Perfect for this. You can't write functions for it.
 */
public record Ingredient(String name, float amount, String unit) {
    public Ingredient(String name, float amount) {
        this(name, amount, "x");
    }
}
