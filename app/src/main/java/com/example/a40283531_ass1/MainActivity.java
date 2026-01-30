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
    //User Interface
    private Button settingbutton, bevent1, bevent2, bevent3;
    private TextView totalcountview;
    private Button showcount;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing UI elements
        settingbutton = findViewById(R.id.settingbutton);
        bevent1 = findViewById(R.id.bevent1);
        bevent2 = findViewById(R.id.bevent2);
        bevent3 = findViewById(R.id.bevent3);
        showcount = findViewById(R.id.showcount);




    }






}

}
