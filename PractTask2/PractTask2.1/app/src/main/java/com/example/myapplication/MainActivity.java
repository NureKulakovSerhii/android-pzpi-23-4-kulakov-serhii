package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button changetext = findViewById(R.id.buttonChangeText);
        Button shownotification = findViewById(R.id.buttonShowNotification);
        TextView textview = findViewById(R.id.textView);
        EditText editText = findViewById(R.id.editText);
        ImageView imageView = findViewById(R.id.imageView);

        changetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                textview.setText(input.isEmpty() ? "Текст не введено" : input);
            }
        });
        shownotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Повідомлення відображено!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}