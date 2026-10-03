package com.example.smartpantry;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String ingredientsRaw;
    private String steps;

    public Recipe(int id, String name, String ingredientsRaw, String steps) {
        this.id = id;
        this.name = name;
        this.ingredientsRaw = ingredientsRaw;
        this.steps = steps;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getIngredientsRaw() { return ingredientsRaw; }
    public String getSteps() { return steps; }

    public List<String> getIngredientList() {
        List<String> list = new ArrayList<>();
        if (ingredientsRaw != null && !ingredientsRaw.isEmpty()) {
            for (String s : ingredientsRaw.split(",")) {
                list.add(s.trim().toLowerCase());
            }
        }
        return list;
    }
}