package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "ingredient_name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    // Recipes table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "_id";
    public static final String COL_RECIPE_NAME = "recipe_name";
    public static final String COL_RECIPE_INGREDIENTS = "ingredients";
    public static final String COL_RECIPE_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create pantry table
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_QUANTITY + " REAL NOT NULL, "
                + COL_UNIT + " TEXT, "
                + COL_EXPIRY + " TEXT" + ")";
        db.execSQL(createPantry);

        // Create recipes table
        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + "("
                + COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RECIPE_NAME + " TEXT NOT NULL, "
                + COL_RECIPE_INGREDIENTS + " TEXT NOT NULL, "
                + COL_RECIPE_STEPS + " TEXT" + ")";
        db.execSQL(createRecipes);

        // Pre-load 18 recipes
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // ==================== PANTRY CRUD ====================

    public long addPantryItem(String name, double quantity, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name.trim().toLowerCase());
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_EXPIRY, expiry);
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " ORDER BY " + COL_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                PantryItem item = new PantryItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPIRY))
                );
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int updatePantryItem(int id, String name, double quantity, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name.trim().toLowerCase());
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_EXPIRY, expiry);
        int rows = db.update(TABLE_PANTRY, values, COL_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public void deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ==================== RECIPE METHODS ====================

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);
        if (cursor.moveToFirst()) {
            do {
                Recipe r = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_INGREDIENTS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_STEPS))
                );
                list.add(r);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES + " WHERE " + COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(id)});
        Recipe r = null;
        if (cursor.moveToFirst()) {
            r = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_INGREDIENTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_STEPS))
            );
        }
        cursor.close();
        db.close();
        return r;
    }

    // ==================== SEED RECIPES ====================

    private void seedRecipes(SQLiteDatabase db) {
        String[][] recipes = {
                // name | ingredients (comma-separated) | steps
                {"Scrambled Eggs", "eggs, butter, salt, pepper", "1. Crack eggs into bowl. 2. Whisk with salt and pepper. 3. Melt butter in pan. 4. Pour eggs and stir until cooked."},
                {"Tomato Pasta", "pasta, tomatoes, garlic, olive oil, salt", "1. Boil pasta. 2. Sauté garlic in oil. 3. Add chopped tomatoes. 4. Mix with pasta and serve."},
                {"Cheese Toast", "bread, cheese, butter", "1. Butter the bread. 2. Add cheese on top. 3. Toast until melted."},
                {"Vegetable Stir Fry", "rice, carrots, broccoli, soy sauce, oil", "1. Cook rice. 2. Stir-fry vegetables in oil. 3. Add soy sauce and mix with rice."},
                {"Pancakes", "flour, eggs, milk, sugar, butter", "1. Mix flour, eggs, milk, sugar. 2. Melt butter in pan. 3. Pour batter and cook both sides."},
                {"Garlic Bread", "bread, garlic, butter, parsley", "1. Mix crushed garlic and butter. 2. Spread on bread. 3. Bake until golden."},
                {"Omelette", "eggs, cheese, milk, salt, pepper", "1. Whisk eggs with milk. 2. Pour into hot pan. 3. Add cheese, fold and serve."},
                {"Tomato Soup", "tomatoes, onion, garlic, salt, cream", "1. Sauté onion and garlic. 2. Add tomatoes and water. 3. Blend and add cream."},
                {"Fried Rice", "rice, eggs, soy sauce, oil, onion", "1. Cook rice. 2. Scramble eggs. 3. Stir-fry onion, add rice, eggs, soy sauce."},
                {"Grilled Cheese", "bread, cheese, butter", "1. Butter bread. 2. Add cheese between slices. 3. Grill until golden."},
                {"Fruit Salad", "apple, banana, orange, honey", "1. Chop all fruit. 2. Mix in bowl. 3. Drizzle honey on top."},
                {"Mashed Potatoes", "potatoes, butter, milk, salt", "1. Boil potatoes. 2. Mash with butter and milk. 3. Season with salt."},
                {"Pasta Salad", "pasta, tomatoes, cheese, olive oil, basil", "1. Cook pasta. 2. Chop tomatoes and cheese. 3. Mix with oil and basil."},
                {"Quesadilla", "tortilla, cheese, chicken, salsa", "1. Place cheese and chicken on tortilla. 2. Fold and cook in pan. 3. Serve with salsa."},
                {"Banana Smoothie", "banana, milk, honey, yogurt", "1. Put all ingredients in blender. 2. Blend until smooth. 3. Serve chilled."},
                {"Bruschetta", "bread, tomatoes, garlic, olive oil, basil", "1. Toast bread. 2. Rub with garlic. 3. Top with chopped tomatoes, oil, basil."},
                {"Egg Fried Noodles", "noodles, eggs, soy sauce, oil, onion", "1. Cook noodles. 2. Scramble eggs. 3. Stir-fry onion, noodles, eggs, soy sauce."},
                {"Cheese Pasta", "pasta, cheese, milk, butter", "1. Boil pasta. 2. Melt butter, add milk and cheese. 3. Mix with pasta."}
        };

        for (String[] recipe : recipes) {
            ContentValues values = new ContentValues();
            values.put(COL_RECIPE_NAME, recipe[0]);
            values.put(COL_RECIPE_INGREDIENTS, recipe[1]);
            values.put(COL_RECIPE_STEPS, recipe[2]);
            db.insert(TABLE_RECIPES, null, values);
        }
    }
}