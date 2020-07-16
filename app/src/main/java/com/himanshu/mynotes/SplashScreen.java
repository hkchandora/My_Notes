package com.himanshu.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.splash_bar);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            progressBar.setVisibility(View.VISIBLE);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(getApplicationContext(), MainDashBoardActivity.class));
                        finish();
                    }
                }
            };
            thread.start();
        } else {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            };
            thread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}