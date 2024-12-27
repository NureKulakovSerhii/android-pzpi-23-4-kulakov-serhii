package com.example.lb4.classes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Time;
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
    private NotesDatabaseHelper databaseHelper;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.CustomActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        databaseHelper = new NotesDatabaseHelper(this);

        int currentFontSize = Fonts.getFontSize(this);
        updateFontSize(currentFontSize);

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
        if (intent != null) {
            String noteTitle = intent.getStringExtra("note_title");
            String noteDescription = intent.getStringExtra("note_description");
            int notePriority = intent.getIntExtra("note_priority", 1);
            long dueDateMillis = intent.getLongExtra("note_date",1);
            int dueHour = intent.getIntExtra("note_hour", 12);
            String notePhotoPath = intent.getStringExtra("note_photo_path");

            editTextTitle.setText(noteTitle);
            editTextDescription.setText(noteDescription);
            spinnerPriority.setSelection(notePriority - 1);
            calendar.setTimeInMillis(dueDateMillis);
            updateDateButtonText();
            calendar.set(Calendar.HOUR_OF_DAY, dueHour);
            updateTimeButtonText();

            if (notePhotoPath != null && !notePhotoPath.isEmpty()) {
                try {
                    File imageFile = new File(notePhotoPath);
                    if (imageFile.exists()) {
                        Uri imageUri = Uri.fromFile(imageFile);
                        selectedImage.setImageURI(imageUri);
                        selectedImageUri = imageUri;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Не вдалося завантажити зображення", Toast.LENGTH_SHORT).show();
                }
            }
        }

        calendar = Calendar.getInstance();

        applyTheme();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
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
        Date createDate = new Date();
        Date dueDate = calendar.getTime();
        int dueHour = calendar.get(Calendar.HOUR_OF_DAY);
        String photoPath = null;

        if (selectedImageUri != null) {
            photoPath = copyImageToLocalStorage(selectedImageUri);
            if (photoPath == null) {
                Toast.makeText(this, "Не вдалося зберегти зображення", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();
        if (intent.hasExtra("note_id")) {
            int noteId = intent.getIntExtra("note_id", -1);
            long createDateMillis = intent.getLongExtra("note_create_date", System.currentTimeMillis());
            createDate = new Date(createDateMillis);

            Note note = new Note(noteId, title, description, priority, createDate, dueDate, dueHour, photoPath);
            databaseHelper.updateNote(note, noteId);
            Toast.makeText(this, "Нотатка оновлена", Toast.LENGTH_SHORT).show();
        } else {
            Note note = new Note(title, description, priority, createDate, dueDate, dueHour, photoPath);
            long id = databaseHelper.addNote(note);
            note.SetId((int) id);
            Toast.makeText(this, "Нотатка збережена", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    private void updateFontSize(int fontSize) {
        updateTextViews(findViewById(android.R.id.content), fontSize);
    }

    private void updateTextViews(View view, int fontSize) {
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(fontSize);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                updateTextViews(viewGroup.getChildAt(i), fontSize);
            }
        }
    }
    private boolean isDarkThemeEnabled() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE);
        return sharedPreferences.getBoolean("dark_theme", false);
    }
    private void applyTheme() {
        boolean isDarkTheme = isDarkThemeEnabled();

        if (isDarkTheme) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.black));
            findViewById(R.id.editTextTitle).setBackgroundColor(getResources().getColor(R.color.gray)); // Фон EditText
            findViewById(R.id.editTextDescription).setBackgroundColor(getResources().getColor(R.color.gray)); // Фон EditText
            findViewById(R.id.spinnerPriority).setBackgroundColor(getResources().getColor(R.color.gray)); // Фон Spinner
            findViewById(R.id.buttonDueDate).setBackgroundColor(getResources().getColor(R.color.dark_colorAccent)); // Фон кнопки
            findViewById(R.id.buttonDueTime).setBackgroundColor(getResources().getColor(R.color.dark_colorAccent));
            findViewById(R.id.buttonSelectImage).setBackgroundColor(getResources().getColor(R.color.dark_colorAccent)); // Фон кнопки
            findViewById(R.id.buttonSave).setBackgroundColor(getResources().getColor(R.color.dark_colorAccent)); // Фон кнопки
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue))); // Колір ActionBar
        } else {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white)); // Фон застосунку
            findViewById(R.id.editTextTitle).setBackgroundColor(getResources().getColor(R.color.white)); // Фон EditText
            findViewById(R.id.editTextDescription).setBackgroundColor(getResources().getColor(R.color.white)); // Фон EditText
            findViewById(R.id.spinnerPriority).setBackgroundColor(getResources().getColor(R.color.white)); // Фон Spinner
            findViewById(R.id.buttonDueDate).setBackgroundColor(getResources().getColor(R.color.colorAccent)); // Фон кнопки
            findViewById(R.id.buttonDueTime).setBackgroundColor(getResources().getColor(R.color.colorAccent)); // Фон кнопки
            findViewById(R.id.buttonSelectImage).setBackgroundColor(getResources().getColor(R.color.colorAccent)); // Фон кнопки
            findViewById(R.id.buttonSave).setBackgroundColor(getResources().getColor(R.color.colorAccent)); // Фон кнопки
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary))); // Колір ActionBar
        }
    }

        private String copyImageToLocalStorage(Uri uri) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                if (inputStream == null) {
                    return null;
                }
                File localFile = new File(getFilesDir(), "image_" + System.currentTimeMillis() + ".jpg");
                FileOutputStream outputStream = new FileOutputStream(localFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
                return localFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }