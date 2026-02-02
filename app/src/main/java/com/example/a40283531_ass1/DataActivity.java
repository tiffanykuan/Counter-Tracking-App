package com.example.a40283531_ass1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    private TextView tvEvent1Count, tvEvent2Count, tvEvent3Count, tvTotal;
    private ListView lvHistory;

    private CounterPreferences prefs;

    // true = Event name mode (default)
    // false = Event Button # mode (1..3)
    private boolean showEventNames = true;

    // History stored as button numbers: 1,2,3 (oldest -> newest)
    private ArrayList<Integer> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Toolbar toolbar = findViewById(R.id.dataToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // up navigation
            getSupportActionBar().setTitle("Data");
        }

        prefs = new CounterPreferences(this);

        tvEvent1Count = findViewById(R.id.tvEvent1Count);
        tvEvent2Count = findViewById(R.id.tvEvent2Count);
        tvEvent3Count = findViewById(R.id.tvEvent3Count);
        tvTotal = findViewById(R.id.tvTotal);
        lvHistory = findViewById(R.id.lvHistory);

        // Load history (oldest -> newest)
        history = prefs.getEventHistory(); // implement this to return ArrayList<Integer>

        refreshUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // In case user changed names in Settings, reload and refresh
        history = prefs.getEventHistory();
        refreshUI();
    }

    private void refreshUI() {
        // counts
        int c1 = prefs.getCount1();
        int c2 = prefs.getCount2();
        int c3 = prefs.getCount3();
        int total = c1 + c2 + c3;

        // labels depend on mode
        String label1 = showEventNames ? safeName(prefs.getCounter1Name(), "Event A") : "Counter 1";
        String label2 = showEventNames ? safeName(prefs.getCounter2Name(), "Event B") : "Counter 2";
        String label3 = showEventNames ? safeName(prefs.getCounter3Name(), "Event C") : "Counter 3";

        tvEvent1Count.setText(label1 + ": " + c1 + " events");
        tvEvent2Count.setText(label2 + ": " + c2 + " events");
        tvEvent3Count.setText(label3 + ": " + c3 + " events");
        tvTotal.setText("Total: " + total);

        // history list text
        ArrayList<String> historyText = new ArrayList<>();
        if (history != null) {
            for (int btnNum : history) {
                if (showEventNames) {
                    historyText.add(buttonNumToName(btnNum));
                } else {
                    historyText.add(String.valueOf(btnNum));
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                historyText
        );
        lvHistory.setAdapter(adapter);
    }

    private String buttonNumToName(int btnNum) {
        switch (btnNum) {
            case 1: return safeName(prefs.getCounter1Name(), "Event A");
            case 2: return safeName(prefs.getCounter2Name(), "Event B");
            case 3: return safeName(prefs.getCounter3Name(), "Event C");
            default: return "Unknown";
        }
    }

    private String safeName(String name, String fallback) {
        if (name == null) return fallback;
        String trimmed = name.trim();
        return trimmed.isEmpty() ? fallback : trimmed;
    }

    // Toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_menu, menu);
        return true;
    }

    // Up nav + toggle action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish(); // goes back to MainActivity
            return true;
        }

        if (id == R.id.action_toggle_event_names) {
            showEventNames = !showEventNames;
            refreshUI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
