package com.example.smartpantry;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etExpiry;
    private Spinner spinnerUnit;
    private DatabaseHelper db;
    private int editId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.et_name);
        etQuantity = findViewById(R.id.et_quantity);
        etExpiry = findViewById(R.id.et_expiry);
        spinnerUnit = findViewById(R.id.spinner_unit);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnDelete = findViewById(R.id.btn_delete);
        TextView tvTitle = findViewById(R.id.tv_form_title);

        String[] units = {"units", "g", "kg", "ml", "l", "cups", "tbsp", "tsp"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        // Check if editing
        if (getIntent().hasExtra("pantry_id")) {
            editId = getIntent().getIntExtra("pantry_id", -1);
            tvTitle.setText("Edit Ingredient");
            etName.setText(getIntent().getStringExtra("pantry_name"));
            etQuantity.setText(String.valueOf(getIntent().getDoubleExtra("pantry_qty", 1)));
            etExpiry.setText(getIntent().getStringExtra("pantry_expiry"));
            String unit = getIntent().getStringExtra("pantry_unit");
            if (unit != null) {
                int pos = unitAdapter.getPosition(unit);
                if (pos >= 0) spinnerUnit.setSelection(pos);
            }
            btnDelete.setVisibility(Button.VISIBLE);
        }

        btnSave.setOnClickListener(v -> saveItem());

        btnDelete.setOnClickListener(v -> {
            db.deletePantryItem(editId);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // INPUT VALIDATION
        if (name.isEmpty()) {
            etName.setError("Ingredient name is required");
            etName.requestFocus();
            return;
        }

        if (qtyStr.isEmpty()) {
            etQuantity.setError("Quantity is required");
            etQuantity.requestFocus();
            return;
        }

        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            etQuantity.requestFocus();
            return;
        }

        if (qty <= 0) {
            etQuantity.setError("Quantity must be greater than 0");
            etQuantity.requestFocus();
            return;
        }

        String unit = spinnerUnit.getSelectedItem().toString();

        if (editId == -1) {
            db.addPantryItem(name, qty, unit, expiry);
            Toast.makeText(this, "Added to pantry", Toast.LENGTH_SHORT).show();
        } else {
            db.updatePantryItem(editId, name, qty, unit, expiry);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}