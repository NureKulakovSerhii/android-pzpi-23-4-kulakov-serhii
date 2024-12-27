package com.example.lb4.classes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.Manifest;

import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lb4.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNote extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinnerPriority;
    private Button buttonDueDate;
    private Button buttonDueTime;
    private Button buttonSelectImage;
    private Button buttonSave;
    private ImageView selectedImage;

    private Calendar calendar;
    private Uri selectedImageUri;
    private int notePosition = -1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        buttonDueDate = findViewById(R.id.buttonDueDate);
        buttonDueTime = findViewById(R.id.buttonDueTime);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSave = findViewById(R.id.buttonSave);
        selectedImage = findViewById(R.id.imageViewSelected);

        calendar = Calendar.getInstance();



        Intent intent = getIntent();
        if (intent.hasExtra("note_position")) {
            notePosition = intent.getIntExtra("note_position", -1);
            if (notePosition != -1) {
                Note note = Notes.getNotes().get(notePosition);
                editTextTitle.setText(note.GetTitle());
                editTextDescription.setText(note.GeDescription());
                spinnerPriority.setSelection(note.GetPriority() - 1);
                calendar.setTime(note.GetDueDate());
                calendar.set(Calendar.HOUR_OF_DAY, note.GetDueHour());
                updateTimeButtonText();
                selectedImageUri = Uri.parse(note.GetPhotoPath());
            }
        }

        buttonDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        buttonDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButtonText();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        updateTimeButtonText();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void updateDateButtonText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        buttonDueDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeButtonText() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        buttonDueTime.setText(sdf.format(calendar.getTime()));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            selectedImage.setImageURI(selectedImageUri);
            this.selectedImageUri = selectedImageUri;

            Toast.makeText(this, "Картинка вибрана", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = spinnerPriority.getSelectedItemPosition() + 1;
        Date dueDate = calendar.getTime(); // Дата призначення
        int dueHour = calendar.get(Calendar.HOUR_OF_DAY); // Час призначення
        String photoPath = selectedImageUri != null ? selectedImageUri.toString() : "";

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (notePosition != -1) {
            Note note = Notes.getNotes().get(notePosition);
            note.SetTitle(title);
            note.SetDescription(description);
            note.SetPriority(priority);
            note.SetDueDate(dueDate);
            note.SetDueHour(dueHour);
            note.SetPhotoPath(photoPath);
        } else {
            Note note = new Note(title, description, priority, new Date(), dueDate, dueHour, photoPath);
            Notes.getNotes().add(note);
        }

        Toast.makeText(this, "Нотатка збережена", Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Дозвіл на читання зображень надано", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Дозвіл на читання зображень не надано", Toast.LENGTH_SHORT).show();
            }
        }
    }
}