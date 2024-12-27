package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView textViewDisplay;
    private StringBuilder currentInput = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textViewDisplay = findViewById(R.id.textViewDisplay);
        int[] buttonIds = {
                R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8,
                R.id.btn9,
        };
        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(this::onButtonClick);
        }
    }
    private void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        currentInput.append(buttonText);
        textViewDisplay.setText(currentInput.toString());
    }
}