package de.fra.uas.digitales.kochbuch;

import com.opencsv.bean.CsvBindByName;

import java.util.LinkedList;
import java.util.List;

public class NameBean extends CsvBean{


    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "Beschreibung")
    private String beschreibung;
    @CsvBindByName(column = "bild")
    private String imglink;
    @CsvBindByName(column = "rating")
    private String rating;
    @CsvBindByName(column = "ingr")
    private String ingr;
    @CsvBindByName(column = "schritte")
    private String schritte;
    @CsvBindByName(column = "Kategorien")
    private String kategorie;

    private List<IngredientObj> ingredientObj = new LinkedList<>();


    public String getName() {
        return name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String getImglink() {
        return imglink;
    }

    public int getRating() {
        return Integer.parseInt(rating.substring(0, 1));
    }

    public String getIngr() {
        return ingr;
    }

    public String getSchritte() {
        return schritte;
    }

    public String getKategorie() {
        return kategorie;
    }

    public List<IngredientObj> getIngredientObj() {
        return ingredientObj;
    }

    public void setIngredientObj(IngredientObj ingredientObj) {
        this.ingredientObj.add(ingredientObj);
    }
}
