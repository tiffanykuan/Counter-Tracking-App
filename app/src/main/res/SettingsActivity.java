package com.example.a40283531_ass1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private EditText etEvent1, etEvent2, etEvent3;
    private Button btnSave;

    private CounterPreferences counterPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        setEditingEnabled(false); // 🔒 locked by default


        // Enable back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // ✅ IMPORTANT: use the layout file name you actually have
            // If your XML file is res/layout/activity_setting.xml, use this:
            setContentView(R.layout.activity_settings);
            // If your XML file is res/layout/activity_settings.xml, use this instead:
            // setContentView(R.layout.activity_settings);

            counterPreferences = new CounterPreferences(this);

            etEvent1 = findViewById(R.id.etEvent1);
            etEvent2 = findViewById(R.id.etEvent2);
            etEvent3 = findViewById(R.id.etEvent3);
            btnSave = findViewById(R.id.btnSave);

            private void setEditingEnabled(boolean enabled) {
                etEvent1.setEnabled(enabled);
                etEvent2.setEnabled(enabled);
                etEvent3.setEnabled(enabled);
                btnSave.setEnabled(enabled);
            }

            // ✅ Use the correct getters from CounterPreferences
            etEvent1.setText(counterPreferences.getCounter1Name());
            etEvent2.setText(counterPreferences.getCounter2Name());
            etEvent3.setText(counterPreferences.getCounter3Name());

            btnSave.setOnClickListener(v -> {
                String name1 = etEvent1.getText().toString().trim();
                String name2 = etEvent2.getText().toString().trim();
                String name3 = etEvent3.getText().toString().trim();

                // Require at least ONE name (matches your redirect rule)
                if (name1.isEmpty() && name2.isEmpty() && name3.isEmpty()) {
                    Toast.makeText(this, "Please enter at least one event name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Use the correct setter
                counterPreferences.setCounterNames(name1, name2, name3);

                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }}
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // go back to previous activity
        return true;
    }
}

