package com.example.roamsdk;

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

                Supportify.start(MainActivity.this);
            }
        });
    }
}
