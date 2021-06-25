package de.fra.uas.digitales.kochbuch;

import com.opencsv.bean.CsvToBeanBuilder;
import de.fra.uas.digitales.kochbuch.backend.DataManager;
import de.fra.uas.digitales.kochbuch.backend.Ingredient;
import de.fra.uas.digitales.kochbuch.backend.Recipe;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Scraper {

    private static DataManager dataManager;
    private static final String path = "G:\\Code\\Uni\\ProgExKochbuch\\src\\de\\fra\\uas\\digitales\\kochbuch\\gutekueche(1).csv";

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {

        dataManager = new DataManager();

        List<NameBean> beans = new CsvToBeanBuilder(new InputStreamReader(new FileInputStream("src/de/fra/uas/digitales/kochbuch/sortedrecipes.csv"), "windows-1252"))
                .withType(NameBean.class).build().parse();

        for (NameBean b : beans) {
            if (b.getImglink().equals("")) {
                continue;
            }

            Recipe recipe = new Recipe();
            recipe.setRating(b.getRating());
            recipe.setName(b.getName());
            recipe.setDesc(b.getBeschreibung());
            System.out.println(b.getImglink());
            recipe.setImageRaw(downloadUrl(new URL(b.getImglink())));

            String[] strings = StringUtils.substringsBetween(b.getIngr(), "{\"ingr\":\"", "\"}");
            String[] steps = StringUtils.substringsBetween(b.getSchritte(), "{\"schritte\":\"", "\"}");
            if (steps == null) {
                continue;
            }
            String stepsAll = "";
            for (String s : steps) {
                stepsAll = stepsAll.concat("\n" + s);
            }
            recipe.setSteps(stepsAll);


            int f = 0;
            IngredientObj obj = new IngredientObj();
            for (String string : strings) {
                if (obj == null) {
                    obj = new IngredientObj();
                }
                f++;
                if (f == 1) {
                    obj.setAmount(string);

                } else if (f == 2) {
                    obj.setUnit(string);
                } else {
                    obj.setName(string);
                    b.setIngredientObj(obj);
                    f = 0;
                    obj = null;
                }

            }
            List<Ingredient> inglist = new LinkedList<>();
            for (IngredientObj o : b.getIngredientObj()) {
                inglist.add(new Ingredient(o.getName(), Float.parseFloat(o.getAmount()), o.getUnit()));

            }
            recipe.setIngredients(inglist);

            b.getIngredientObj().forEach(System.out::println);


            for (String a : strings) {
                System.out.println(a);
            }

            dataManager.addNewRecipe(recipe);

        }
        System.out.println("b = " + beans.size());

    }

    private static byte[] downloadUrl(URL toDownload) throws InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Thread.sleep(2000);
        return outputStream.toByteArray();
    }


}
