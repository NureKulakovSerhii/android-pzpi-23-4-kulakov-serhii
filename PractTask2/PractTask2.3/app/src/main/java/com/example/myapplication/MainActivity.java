package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Lifecycle";
    private int counter = 0;
    private int timerSeconds = 0;
    private EditText editText;
    private TextView tvCounter, tvTimer;
    private Handler handler;
    private Runnable timerRunnable;
    private boolean isTimerRunning = false;
    private Button button, btnIncrement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.btnGoToSecondActivity);
        tvCounter = findViewById(R.id.tvCounter);
        tvTimer = findViewById(R.id.tvTimer);
        btnIncrement = findViewById(R.id.btnIncrement);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button clicked to go to SecondActivity");
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        Log.d(TAG, "onCreate called");
        handler = new Handler();

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                timerSeconds++;
                tvTimer.setText("Таймер: " + timerSeconds + " с");
                handler.postDelayed(this, 1000);
            }
        };

        btnIncrement.setOnClickListener(v -> {
            counter++;
            tvCounter.setText("Лічильник: " + counter);
        });
        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("counter");
            timerSeconds = savedInstanceState.getInt("timerSeconds");
            tvCounter.setText("Лічильник: " + counter);
            tvTimer.setText("Таймер: " + timerSeconds + " с");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isTimerRunning) {
            handler.postDelayed(timerRunnable, 1000);
            isTimerRunning = true;
        }
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTimerRunning) {
            handler.removeCallbacks(timerRunnable);
            isTimerRunning = false;
        }
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", counter);
        outState.putInt("timerSeconds", timerSeconds);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState called");
    }
}
