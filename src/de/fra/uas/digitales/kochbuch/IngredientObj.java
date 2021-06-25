package de.fra.uas.digitales.kochbuch;

public class IngredientObj {


    private String amount, unit, name;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "IngredientObj{" +
                "amount='" + amount + '\'' +
                ", unit='" + unit + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
