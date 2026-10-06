package com.example.smartpantry;

import java.util.List;

public class StrictMatcher {

    /**
     * Returns true ONLY if every ingredient required by the recipe
     * is present in the pantry with at least the required quantity.
     */
    public static boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantry) {
        List<String> required = recipe.getIngredientList();

        for (String reqIngredient : required) {
            boolean found = false;

            for (PantryItem item : pantry) {
                String pantryName = item.getName().toLowerCase().trim();
                String requiredName = reqIngredient.toLowerCase().trim();

                // Handle singular/plural ("tomato" vs "tomatoes")
                if (namesMatch(pantryName, requiredName)) {
                    // Check quantity (we assume each recipe ingredient needs at least 1 unit)
                    if (item.getQuantity() >= 1.0) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                return false; // Missing even ONE ingredient = recipe excluded
            }
        }
        return true;
    }

    /**
     * Smart name matching: handles plurals and simple variations
     */
    private static boolean namesMatch(String pantryName, String requiredName) {
        // Exact match
        if (pantryName.equals(requiredName)) return true;

        // Simple plural handling
        if (pantryName.equals(requiredName + "s")) return true;
        if (requiredName.equals(pantryName + "s")) return true;
        if (pantryName.equals(requiredName + "es")) return true;
        if (requiredName.equals(pantryName + "es")) return true;

        // Handle "ies" vs "y" (e.g., "berries" vs "berry")
        if (pantryName.endsWith("ies") && requiredName.endsWith("y")) {
            String singular = pantryName.substring(0, pantryName.length() - 3) + "y";
            if (singular.equals(requiredName)) return true;
        }
        if (requiredName.endsWith("ies") && pantryName.endsWith("y")) {
            String singular = requiredName.substring(0, requiredName.length() - 3) + "y";
            if (singular.equals(pantryName)) return true;
        }

        // Handle "ves" vs "f" (e.g., "loaves" vs "loaf")
        if (pantryName.endsWith("ves")) {
            String singular = pantryName.substring(0, pantryName.length() - 3) + "f";
            if (singular.equals(requiredName)) return true;
        }
        if (requiredName.endsWith("ves")) {
            String singular = requiredName.substring(0, requiredName.length() - 3) + "f";
            if (singular.equals(pantryName)) return true;
        }

        return false;
    }

    /**
     * Check if recipe is "almost there" (missing exactly 1 ingredient)
     */
    public static boolean isAlmostThere(Recipe recipe, List<PantryItem> pantry) {
        List<String> required = recipe.getIngredientList();
        int missing = 0;

        for (String reqIngredient : required) {
            boolean found = false;
            for (PantryItem item : pantry) {
                if (namesMatch(item.getName().toLowerCase().trim(), reqIngredient.toLowerCase().trim())) {
                    found = true;
                    break;
                }
            }
            if (!found) missing++;
        }

        return missing == 1;
    }
}