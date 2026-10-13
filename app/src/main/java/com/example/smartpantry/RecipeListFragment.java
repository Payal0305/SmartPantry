package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoRecipes;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        db = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_recipes);
        tvNoRecipes = view.findViewById(R.id.tv_no_recipes);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadMatchingRecipes();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMatchingRecipes();
    }

    private void loadMatchingRecipes() {
        List<Recipe> allRecipes = db.getAllRecipes();
        List<PantryItem> pantry = db.getAllPantryItems();

        List<Recipe> matched = new ArrayList<>();
        for (Recipe r : allRecipes) {
            if (StrictMatcher.canMakeRecipe(r, pantry)) {
                matched.add(r);
            }
        }

        if (matched.isEmpty()) {
            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoRecipes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            RecipeAdapter adapter = new RecipeAdapter(matched, recipe -> {
                Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                intent.putExtra("recipe_id", recipe.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }
}