package com.example.smartpantry;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipeById(recipeId);

        if (recipe != null) {
            TextView tvName = findViewById(R.id.tv_detail_name);
            TextView tvIngredients = findViewById(R.id.tv_detail_ingredients);
            TextView tvSteps = findViewById(R.id.tv_detail_steps);

            tvName.setText(recipe.getName());
            tvIngredients.setText(recipe.getIngredientsRaw());
            tvSteps.setText(recipe.getSteps());
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(recipe != null ? recipe.getName() : "Recipe");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}