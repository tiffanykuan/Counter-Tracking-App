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
    private EditText etCounterNumber; // user input (5..200)
    private Button btnSave;

    private CounterPreferences counterPreferences;

    // Must tap ⋮ -> Edit Settings to enable edits
    private boolean isEditingEnabled = false;

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

        // Init preferences + views
        counterPreferences = new CounterPreferences(this);

        etEvent1 = findViewById(R.id.etEvent1);
        etEvent2 = findViewById(R.id.etEvent2);
        etEvent3 = findViewById(R.id.etEvent3);
        etCounterNumber = findViewById(R.id.etCounterNumber);
        btnSave = findViewById(R.id.btnSave);

        // Load saved names
        etEvent1.setText(counterPreferences.getCounter1Name());
        etEvent2.setText(counterPreferences.getCounter2Name());
        etEvent3.setText(counterPreferences.getCounter3Name());

        // Load saved counter limit (default handled in prefs)
        etCounterNumber.setText(String.valueOf(counterPreferences.getCounterLimit()));

        // Optional: remove hint if has text
        clearHintIfHasText(etEvent1);
        clearHintIfHasText(etEvent2);
        clearHintIfHasText(etEvent3);
        clearHintIfHasText(etCounterNumber);

        // 🔒 Locked by default
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

            // Must have at least one
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

            // Validate counter number (5..200)
            int counterNumber = validateCounterInput();
            if (counterNumber == -1) return;

            // Save
            counterPreferences.setCounterNames(name1, name2, name3);
            counterPreferences.setCounterLimit(counterNumber);

            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish(); // return to MainActivity
        });
    }

    // Letters + spaces only; empty allowed (as long as at least one field is filled overall)
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

    // Validate counter input (min 5, max 200)
    private int validateCounterInput() {
        String input = etCounterNumber.getText().toString().trim();

        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter number of counters (5-200).", Toast.LENGTH_SHORT).show();
            return -1;
        }

        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format.", Toast.LENGTH_SHORT).show();
            return -1;
        }

        if (number < 5 || number > 200) {
            Toast.makeText(this, "Number must be between 5 and 200.", Toast.LENGTH_SHORT).show();
            return -1;
        }

        return number;
    }

    // Enable/disable editing UI
    private void setEditingEnabled(boolean enabled) {
        isEditingEnabled = enabled;

        etEvent1.setEnabled(enabled);
        etEvent2.setEnabled(enabled);
        etEvent3.setEnabled(enabled);
        etCounterNumber.setEnabled(enabled);

        // Keep save enabled so it can show the "Tap ⋮" message when locked
        btnSave.setEnabled(true);

        float alpha = enabled ? 1f : 0.6f;
        etEvent1.setAlpha(alpha);
        etEvent2.setAlpha(alpha);
        etEvent3.setAlpha(alpha);
        etCounterNumber.setAlpha(alpha);
        btnSave.setAlpha(1f);
    }

    // Inflate ⋮ menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    // Handle toolbar actions (back + edit)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        // ✅ IMPORTANT: this id must match your menu XML
        // Use ONE of these in your menu file:
        //   android:id="@+id/action_edit_settings"
        // OR
        //   android:id="@+id/action_edit"
        if (id == R.id.action_edit || id == R.id.action_edit) {
            setEditingEnabled(true);
            Toast.makeText(this, "Editing enabled", Toast.LENGTH_SHORT).show();
            etEvent1.requestFocus();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
