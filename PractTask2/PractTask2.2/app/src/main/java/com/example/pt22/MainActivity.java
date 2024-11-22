package com.example.pt22;

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

    private TextView textview;
    private StringBuilder input = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textview = findViewById(R.id.textView);

        int [] buttons = { R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                           R.id.button5, R.id.button6,R.id.button7, R.id.button8,R.id.button9, R.id.buttonNull,
                           R.id.buttonPlus, R.id.buttonDivide, R.id.buttonMinus, R.id.buttonMultiply, R.id.buttonEqual
        };

        for (int id: buttons) {
            Button button = findViewById(id);
            button.setOnClickListener(this::onButtonClick);
            }
        }
        private void onButtonClick(View view){
            Button button = (Button) view;
            String text = button.getText().toString();
            input.append(text);
            textview.setText(input);
        }
    }
