package com.example.pt4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EditText nameInput, ageInput, textInput;
    private TextView userInfoTextView, userListTextView, fileContentTextView;
    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        textInput = findViewById(R.id.textInput);
        Button saveButtonSharedPrefs = findViewById(R.id.saveButtonSharedPrefs);
        Button saveButtonSQLite = findViewById(R.id.saveButtonSQLite);
        Button viewButtonSQLite = findViewById(R.id.viewButtonSQLite);
        Button saveButtonFile = findViewById(R.id.saveButtonFile);
        Button loadButtonFile = findViewById(R.id.loadButtonFile);

        userInfoTextView = findViewById(R.id.userInfoTextView);
        userListTextView = findViewById(R.id.userListTextView);
        fileContentTextView = findViewById(R.id.fileContentTextView);
        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        saveButtonSharedPrefs.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            int age = Integer.parseInt(ageInput.getText().toString());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putInt("age", age);
            editor.apply();

            userInfoTextView.setText("Ім'я: " + name + ", Вік: " + age);
        });
        saveButtonSQLite.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            int age = Integer.parseInt(ageInput.getText().toString());
            dbHelper.addUser(name, age);
            nameInput.setText("");
            ageInput.setText("");
        });
        viewButtonSQLite.setOnClickListener(v ->{
            displayUsers();
        });
        saveButtonFile.setOnClickListener(v -> {
            String text = textInput.getText().toString();
            saveToFile(text);
            fileContentTextView.setText("Текст збережено у файл.");
        });

        loadButtonFile.setOnClickListener(v -> {
            String fileContent = readFromFile();
            fileContentTextView.setText("Текст з файлу:\n" + fileContent);
        });
    }
    private void displayUsers() {
        List<String> users = dbHelper.getAllUsers();
        StringBuilder usersList = new StringBuilder();
        for (String user : users) {
            usersList.append(user).append("\n");
        }
        userListTextView.setText("Список користувачів (SQLite):\n" + usersList.toString());
    }

    private void saveToFile(String data) {
        try {
            FileOutputStream fos = openFileOutput("user_data.txt", MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile() {
        StringBuilder content = new StringBuilder();
        try {
            FileInputStream fis = openFileInput("user_data.txt");
            int ch;
            while ((ch = fis.read()) != -1) {
                content.append((char) ch);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}