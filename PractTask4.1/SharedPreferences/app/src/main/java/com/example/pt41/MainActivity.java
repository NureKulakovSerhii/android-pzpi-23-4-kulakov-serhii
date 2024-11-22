package com.example.pt41;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextAge;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editTextName = findViewById(R.id.textName);
        editTextAge = findViewById(R.id.textAge);
        saveButton = findViewById(R.id.buttonSave);

        loadData();
        saveButton.setOnClickListener(v->{
            saveData();
        });
    }
    private void saveData(){
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();
        if(!name.isEmpty() && !age.isEmpty()) {
            SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("age", age);
            editor.apply();
            Toast.makeText(this, "Дані збережено!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Не всі поля заповнені!", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String age = sharedPreferences.getString("age", "");
        editTextName.setText(name);
        editTextAge.setText(age);
    }
}