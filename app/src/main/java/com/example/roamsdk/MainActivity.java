package com.example.roamsdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roamtechsdk.Supportify;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.roamtechsdk.Supportify;

public class MainActivity extends AppCompatActivity {
    Button fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent( MainActivity.this, Supportify.class );
                myIntent.putExtra("Title", "Example");
                myIntent.putExtra("Key_1", "nyzxtb0rj5ugo89");
                myIntent.putExtra("Key_2", "70795");
                myIntent.putExtra("StatusBar_Color", "#FF4081");
                myIntent.putExtra("ToolBar_Color", "#FF4081");
                startActivity(myIntent);
            }
        });
    }
}
