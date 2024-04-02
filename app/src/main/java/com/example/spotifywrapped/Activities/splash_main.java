package com.example.spotifywrapped.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.window.SplashScreen;
import android.window.SplashScreenView;

import com.example.spotifywrapped.R;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Duration;

public class splash_main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_load);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash_main.this, MainActivity.class));
                finish();
            }
        }, 2500);

    }
}
