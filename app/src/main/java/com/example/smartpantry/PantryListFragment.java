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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class PantryListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private PantryAdapter adapter;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry_list, container, false);

        db = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_pantry);
        tvEmpty = view.findViewById(R.id.tv_empty_pantry);
        FloatingActionButton fab = view.findViewById(R.id.fab_add);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditActivity.class);
            startActivity(intent);
        });

        loadPantry();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPantry();
    }

    private void loadPantry() {
        List<PantryItem> items = db.getAllPantryItems();
        if (items.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new PantryAdapter(items, item -> {
                Intent intent = new Intent(getActivity(), AddEditActivity.class);
                intent.putExtra("pantry_id", item.getId());
                intent.putExtra("pantry_name", item.getName());
                intent.putExtra("pantry_qty", item.getQuantity());
                intent.putExtra("pantry_unit", item.getUnit());
                intent.putExtra("pantry_expiry", item.getExpiryDate());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }
}