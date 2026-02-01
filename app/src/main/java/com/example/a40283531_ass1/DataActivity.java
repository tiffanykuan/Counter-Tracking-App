package com.example.a40283531_ass1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        TextView tvPlaceholder = findViewById(R.id.tvPlaceholder);
        tvPlaceholder.setText("DataActivity\n(Counts will be shown here)");
    }
}
