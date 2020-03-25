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
                //define background image..
                int resId = getResources().getIdentifier("image_name", "drawable", getPackageName());
                Intent myIntent = new Intent( MainActivity.this, Supportify.class );
                myIntent.putExtra("Title", "Example");
                myIntent.putExtra("Key_1", "Key from emalify");
                myIntent.putExtra("Key_2", "number from emalify");
                myIntent.putExtra("image_id_resource", resId );
                myIntent.putExtra("StatusBar_Color", "#FF4081");
                myIntent.putExtra("ToolBar_Color", "#FF4081");
                startActivity(myIntent);
            }
        });
    }
}
