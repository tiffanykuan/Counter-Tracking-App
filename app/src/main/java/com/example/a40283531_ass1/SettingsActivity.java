//Tiffany Kuan
//40283531
//COEN 390 - Programming Assignment 1

package com.example.a40283531_ass1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private EditText etEvent1, etEvent2, etEvent3;
    private EditText etCounterLimit;
    private Button btnSave;

    private CounterPreferences counterPreferences;

    // Must tap ⋮ -> Edit Settings to enable edits
    private boolean isEditingEnabled = false;

    private static final int MIN_LIMIT = 5;
    private static final int MAX_LIMIT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        counterPreferences = new CounterPreferences(this);

        etEvent1 = findViewById(R.id.etEvent1);
        etEvent2 = findViewById(R.id.etEvent2);
        etEvent3 = findViewById(R.id.etEvent3);
        etCounterLimit = findViewById(R.id.etCounterLimit);
        btnSave  = findViewById(R.id.btnSave);

        // Load saved names
        etEvent1.setText(counterPreferences.getCounter1Name());
        etEvent2.setText(counterPreferences.getCounter2Name());
        etEvent3.setText(counterPreferences.getCounter3Name());


        etCounterLimit.setText(String.valueOf(counterPreferences.getCounterLimit()));


        // locked by default
        setEditingEnabled(false);

        // Save button
        btnSave.setOnClickListener(v -> {
            if (!isEditingEnabled) {
                Toast.makeText(this, "Tap ⋮ → Edit Settings to make changes", Toast.LENGTH_SHORT).show();
                return;
            }

            String name1 = etEvent1.getText().toString().trim();
            String name2 = etEvent2.getText().toString().trim();
            String name3 = etEvent3.getText().toString().trim();

            if (name1.isEmpty() && name2.isEmpty() && name3.isEmpty()) {
                Toast.makeText(this, "Please enter at least one event name.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Alphabetical + spaces only (no numbers/symbols), max 20 chars
            if (!isValidEventName(name1) || !isValidEventName(name2) || !isValidEventName(name3)) {
                Toast.makeText(this, "Names must use letters and spaces only (max 20).", Toast.LENGTH_SHORT).show();
                return;
            }

            // No duplicates (ignoring empty)
            if (isDuplicateNonEmpty(name1, name2) || isDuplicateNonEmpty(name1, name3) || isDuplicateNonEmpty(name2, name3)) {
                Toast.makeText(this, "Event names must be different.", Toast.LENGTH_SHORT).show();
                return;
            }

            // validate limit (5..200)
            String limitStr = etCounterLimit.getText().toString().trim();
            if (limitStr.isEmpty()) {
                Toast.makeText(this, "Please enter a max total count (5–200).", Toast.LENGTH_SHORT).show();
                return;
            }

            int limit;
            try {
                limit = Integer.parseInt(limitStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Limit must be a number.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (limit < MIN_LIMIT || limit > MAX_LIMIT) {
                Toast.makeText(this, "Limit must be between 5 and 200.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save + go back
            counterPreferences.setCounterNames(name1, name2, name3);
            counterPreferences.setCounterLimit(limit);

            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private boolean isValidEventName(String s) {
        if (s.isEmpty()) return true;
        if (s.length() > 20) return false;
        return s.matches("[A-Za-z ]+");
    }

    private boolean isDuplicateNonEmpty(String a, String b) {
        if (a.isEmpty() || b.isEmpty()) return false;
        return a.equalsIgnoreCase(b);
    }

    private void clearHintIfHasText(EditText et) {
        if (et.getText() != null && et.getText().toString().trim().length() > 0) {
            et.setHint(null);
        }
    }

    // Lock/unlock the limit field exactly like the name fields
    private void setEditingEnabled(boolean enabled) {
        isEditingEnabled = enabled;

        etEvent1.setEnabled(enabled);
        etEvent2.setEnabled(enabled);
        etEvent3.setEnabled(enabled);
        etCounterLimit.setEnabled(enabled);
        btnSave.setEnabled(enabled);

        float alpha = enabled ? 1f : 0.6f;
        etEvent1.setAlpha(alpha);
        etEvent2.setAlpha(alpha);
        etEvent3.setAlpha(alpha);
        etCounterLimit.setAlpha(alpha);
        btnSave.setAlpha(alpha);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_edit) {
            setEditingEnabled(true);
            Toast.makeText(this, "Editing enabled", Toast.LENGTH_SHORT).show();
            etEvent1.requestFocus();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
