package com.example.pt43;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "user_data.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EditText inputText = findViewById(R.id.inputText);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonRead = findViewById(R.id.buttonRead);
        TextView outputText = findViewById(R.id.outputText);

        buttonSave.setOnClickListener(v -> {
            String textToSave = inputText.getText().toString();
            if (!textToSave.isEmpty()){
                writeToFile(textToSave);
                inputText.setText(" ");
            }
            else{
                Toast.makeText(this, "Поле порожнє!", Toast.LENGTH_LONG).show();
            }
        });
        buttonRead.setOnClickListener(v ->{
            String fileData = readFromFile();
            outputText.setText(fileData.isEmpty() ? "Файл порожній" : fileData);
        });
    }
    private void writeToFile(String data) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            fos.write(data.getBytes());
            Toast.makeText(this, "Дані збережено!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Помилка!", Toast.LENGTH_SHORT).show();
        }
    }
    private String readFromFile() {
        StringBuilder data = new StringBuilder();
        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            int byteRead;
            while ((byteRead = fis.read()) != -1) {
                data.append((char) byteRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Помилка зчитування з файлу!", Toast.LENGTH_SHORT).show();
        }
        return data.toString();
    }
}