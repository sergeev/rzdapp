package com.example.opencvapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {
    Intent i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void toVokzal(View vie) {
        i = new Intent(MainActivity2.this, MainActivity3.class);
        startActivity(i);
    }

    public void toQRCode(View vie) {
        j = new Intent(MainActivity2.this, Camera2Capture.class);
        startActivity(j);
    }

}