package com.example.opencvapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
Intent i;
CountDownTimer timer;
Animation animation;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView2);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate);

        timer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {
            imageView.startAnimation(animation);
            }

            @Override
            public void onFinish() {
                i = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(i);
            }
        }.start();
    }
}