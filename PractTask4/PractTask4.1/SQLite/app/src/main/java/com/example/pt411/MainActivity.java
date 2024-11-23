package com.example.pt411;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextAge;
    private Button saveButton;
    private ListView users;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        saveButton = findViewById(R.id.buttonAdd);
        users = findViewById(R.id.listViewUsers);
        dbHelper = new DBHelper(this);

        saveButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String ageStr = editTextAge.getText().toString();

            if (!name.isEmpty() && !ageStr.isEmpty()) {
                int age = Integer.parseInt(ageStr);
                dbHelper.addUser(name, age);
                Toast.makeText(this, "Записано!", Toast.LENGTH_SHORT).show();
                loadUsers();
            } else {
                Toast.makeText(this, "Не всі поля заповнені!", Toast.LENGTH_SHORT).show();
            }
        });
        loadUsers();
    }
    private void loadUsers() {
        Cursor cursor = dbHelper.getAllUsers();

        String[] from = {DBHelper.COLUMN_NAME, DBHelper.COLUMN_AGE};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                from,
                to,
                0
        );

        users.setAdapter(adapter);
    }
}