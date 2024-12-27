package com.example.lb4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lb4.classes.AddNote;
import com.example.lb4.classes.Fonts;
import com.example.lb4.classes.Note;
import com.example.lb4.classes.NoteAdapter;
import com.example.lb4.classes.NotesDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {
    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> originalNotes;
    private NotesDatabaseHelper databaseHelper;
    private List<Note> currentFilteredNotes;
    private SharedPreferences sharedPreferences;
    private ActivityResultLauncher<Intent> addNoteLauncher;

    private static final String PREF_DARK_THEME = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE);
        setTheme(R.style.CustomActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        databaseHelper = new NotesDatabaseHelper(this);
        originalNotes = databaseHelper.getAllNotes();
        currentFilteredNotes = new ArrayList<>();


        addNoteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            originalNotes.clear();
                            originalNotes.addAll(databaseHelper.getAllNotes());
                            noteAdapter.updateNotes(originalNotes);
                            noteAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );
        SearchView searchView = findViewById(R.id.searchView);
        Spinner spinnerPriorityFilter = findViewById(R.id.spinnerPriorityFilter);
        Switch switcher = findViewById(R.id.themeSwitch);
        Spinner fontSizeSpinner = findViewById(R.id.fontSizeSpinner);

        int currentFontSize = Fonts.getFontSize(this);
        switch (currentFontSize) {
            case 12:
                fontSizeSpinner.setSelection(0);
                break;
            case 14:
                fontSizeSpinner.setSelection(1);
                break;
            case 18:
                fontSizeSpinner.setSelection(2);
                break;
        }

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        originalNotes = databaseHelper.getAllNotes();
        noteAdapter = new NoteAdapter(originalNotes, this, false);
        notesRecyclerView.setAdapter(noteAdapter);

        noteAdapter.notifyDataSetChanged();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotesByText(newText);
                return true;
            }
        });
        spinnerPriorityFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterNotesByPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                noteAdapter.updateNotes(originalNotes);
            }
        });
        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNote.class);
                addNoteLauncher.launch(intent);
            }
        });
        switcher.setChecked(false);
        switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleTheme(isChecked);
        });

        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedFontSize = 14;
                switch (position) {
                    case 0:
                        selectedFontSize = 12;
                        break;
                    case 1:
                        selectedFontSize = 14;
                        break;
                    case 2:
                        selectedFontSize = 18;
                        break;
                }
                Fonts.saveFontSize(MainActivity.this, selectedFontSize);
                updateFontSize(selectedFontSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateFontSize(currentFontSize);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = noteAdapter.getLongClickedPosition();
        if (position < 0 || position >= originalNotes.size()) {
            return super.onContextItemSelected(item);
        }

        Note selectedNote = originalNotes.get(position);
        switch (item.getItemId()) {
            case 1:
                editNote(selectedNote);
                return true;
            case 2:
                deleteNoteFromDatabase(selectedNote.GetId(), position);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            originalNotes.clear();
            originalNotes.addAll(databaseHelper.getAllNotes());
            noteAdapter.updateNotes(originalNotes);
            noteAdapter.notifyDataSetChanged();
        }
    }

    private void deleteNoteFromDatabase(int id, int position) {
        databaseHelper.deleteNote(id);
        originalNotes.remove(position);
        noteAdapter.notifyItemRemoved(position);
    }
    private void filterNotesByText(String query) {
        originalNotes = databaseHelper.getAllNotes();
        currentFilteredNotes.clear();
        if (query.isEmpty()) {
            currentFilteredNotes.addAll(originalNotes);
        } else {
            for (Note note : originalNotes) {
                if (note.GetTitle().toLowerCase().contains(query.toLowerCase()) ||
                        note.GeDescription().toLowerCase().contains(query.toLowerCase())) {
                    currentFilteredNotes.add(note);
                }
            }
        }
        noteAdapter.updateNotes(currentFilteredNotes);
    }
    private void filterNotesByPriority(int position) {
        originalNotes = databaseHelper.getAllNotes();
        currentFilteredNotes.clear();
        if (position == 0) {
            currentFilteredNotes.addAll(originalNotes);
        } else {
            int priority = position;
            for (Note note : originalNotes) {
                if (note.GetPriority() == priority) {
                    currentFilteredNotes.add(note);
                }
            }
        }
        noteAdapter.updateNotes(currentFilteredNotes);
    }
    private void editNote(Note note) {
        Intent intent = new Intent(this, AddNote.class);
        intent.putExtra("note_id", note.GetId());
        intent.putExtra("note_title", note.GetTitle());
        intent.putExtra("note_description", note.GeDescription());
        intent.putExtra("note_priority", note.GetPriority());
        intent.putExtra("note_create_date", note.GetDate().getTime());
        intent.putExtra("note_due_date", note.GetDueDate().getTime());
        intent.putExtra("note_due_hour", note.GetDueHour());
        intent.putExtra("note_photo_path", note.GetPhotoPath());

        addNoteLauncher.launch(intent);
    }

    private void deleteNote(Note note, int position) {
        databaseHelper.deleteNote(note.GetId());
        originalNotes.remove(position);
        noteAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onEditClick(int position) {
        Note selectedNote = noteAdapter.getNoteAtPosition(position);

        Intent intent = new Intent(MainActivity.this, AddNote.class);
        intent.putExtra("note_id", selectedNote.GetId());
        intent.putExtra("note_title", selectedNote.GetTitle());
        intent.putExtra("note_description", selectedNote.GeDescription());
        intent.putExtra("note_priority", selectedNote.GetPriority());
        intent.putExtra("note_date", selectedNote.GetDueDate().getTime());
        intent.putExtra("note_time", selectedNote.GetDueHour());
        intent.putExtra("note_photo_path", selectedNote.GetPhotoPath());

        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        Note note = originalNotes.get(position);
        deleteNote(note, position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        originalNotes.clear();
        originalNotes.addAll(databaseHelper.getAllNotes());
        noteAdapter.updateNotes(originalNotes);
        noteAdapter.notifyDataSetChanged();
    }
    private boolean isDarkThemeEnabled() {
        return sharedPreferences.getBoolean(PREF_DARK_THEME, false);
    }

    private void toggleTheme(boolean isDarkTheme) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_DARK_THEME, isDarkTheme);
        editor.apply();
        noteAdapter.updateTheme(isDarkTheme);
        applyTheme();
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

    private void applyTheme() {
        boolean isDarkTheme = isDarkThemeEnabled();

        if (isDarkTheme) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.dark_colorPrimaryDark));
            findViewById(R.id.notesRecyclerView).setBackgroundColor(getResources().getColor(R.color.dark_colorPrimaryDark));
            findViewById(R.id.addNoteButton).setBackgroundColor(getResources().getColor(R.color.dark_blue));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue)));
        } else {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));
            findViewById(R.id.notesRecyclerView).setBackgroundColor(getResources().getColor(R.color.white));
            findViewById(R.id.addNoteButton).setBackgroundColor(getResources().getColor(R.color.colorAccent));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        }
    }
}