package com.example.pt23;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private int clickCount = 0;
    private TextView textViewClicker;
    private TextView textViewTimer;
    private Handler handler = new Handler();
    private int seconds = 0;
    private boolean isRunning = true;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textViewClicker = findViewById(R.id.textViewClicker);
        textViewTimer = findViewById(R.id.textViewTimer);
        Button button = findViewById(R.id.buttonTap);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            isRunning = savedInstanceState.getBoolean("isRunning");
            clickCount = savedInstanceState.getInt("clickCount");
            textViewClicker.setText(savedInstanceState.getString("clickerText"));
        }

        button.setOnClickListener(v -> {
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
        outState.putString("clickerText", textViewClicker.getText().toString());
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
                }
                else if (!isRunning && isPaused) {
                    String time = String.format("Пауза 3 секунди...");
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
        isPaused = true;
        handler.postDelayed(() -> {
            isRunning = true;
            isPaused = false;
        }, 3000);
    }
}