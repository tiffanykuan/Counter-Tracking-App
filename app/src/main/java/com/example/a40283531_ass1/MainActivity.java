//Tiffany Kuan
//40283531
//COEN 390 - Programming Assignment 1

package com.example.a40283531_ass1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // UI
    private Button settingbutton, bevent1, bevent2, bevent3, showcount;
    private TextView totalcountview;

    // Model
    private CounterPreferences counterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init UI
        settingbutton = findViewById(R.id.settingbutton);
        bevent1 = findViewById(R.id.bevent1);
        bevent2 = findViewById(R.id.bevent2);
        bevent3 = findViewById(R.id.bevent3);
        showcount = findViewById(R.id.showcount);
        totalcountview = findViewById(R.id.totalcountview);

        // Init Model
        counterModel = new CounterPreferences(this);

        // SETTINGS -> SettingsActivity
        settingbutton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class))
        );

        // SHOW MY COUNTS -> DataActivity
        showcount.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, DataActivity.class))
        );

        // Event buttons (counting)
        bevent1.setOnClickListener(v -> handleEventPress(1));
        bevent2.setOnClickListener(v -> handleEventPress(2));
        bevent3.setOnClickListener(v -> handleEventPress(3));

        // If no names saved, redirect to Settings
        redirectIfNoNames();

        // Populate UI
        refreshUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When coming back from SettingsActivity, update labels and count
        refreshUI();
        // Optional: enforce rule again (if user backed out without saving)
        redirectIfNoNames();
    }

    private void handleEventPress(int eventNum) {
        // If no names yet, force settings first
        if (!counterModel.hasAnyCounterNames()) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return;
        }

        // Increment event + total, and log chronologically
        counterModel.incrementEvent(eventNum);

        // Update screen
        refreshUI();
    }

    private void refreshUI() {
        String n1 = counterModel.getCounter1Name();
        String n2 = counterModel.getCounter2Name();
        String n3 = counterModel.getCounter3Name();

        bevent1.setText(n1.isEmpty() ? "EVENT A" : n1);
        bevent2.setText(n2.isEmpty() ? "EVENT B" : n2);
        bevent3.setText(n3.isEmpty() ? "EVENT C" : n3);

        totalcountview.setText("Total Count: " + counterModel.getTotalCount());
    }

    private void redirectIfNoNames() {
        if (!counterModel.hasAnyCounterNames()) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            // finish(); // optional: prevents going back to an unusable main screen
        }
    }
}

