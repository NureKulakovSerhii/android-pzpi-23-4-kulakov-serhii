package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewClicker;
    private TextView textViewTimer;
    private Button buttonTap;
    private int clickCount = 0;

    private Handler handler = new Handler();
    private int seconds = 0;
    private boolean isRunning = true;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewClicker = findViewById(R.id.textViewClicker);
        textViewTimer = findViewById(R.id.textViewTimer);
        buttonTap = findViewById(R.id.buttonTap);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            isRunning = savedInstanceState.getBoolean("isRunning");
            clickCount = savedInstanceState.getInt("clickCount");
        }
        textViewClicker.setText("Натиснуто разів: " + clickCount);
        buttonTap.setOnClickListener(v -> {
            clickCount++;
            textViewClicker.setText("Натиснуто разів: " + clickCount);
        });

        runTimer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("isRunning", isRunning);
        outState.putInt("clickCount", clickCount);
    }

    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isRunning && !isPaused) {
                    int minutes = seconds / 60;
                    int secs = seconds % 60;
                    String time = String.format("%02d:%02d", minutes, secs);
                    textViewTimer.setText(time);
                    seconds++;
                } else if (!isRunning && !isPaused) {
                    int minutes = seconds / 60;
                    int secs = seconds % 60;
                    String time = String.format("%02d:%02d", minutes, secs);
                    textViewTimer.setText(time);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(() -> {
            isRunning = true;
        }, 3000);
    }
}
