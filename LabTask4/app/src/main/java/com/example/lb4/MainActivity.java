package com.example.lb4;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lb4.classes.AddNote;
import com.example.lb4.classes.Note;
import com.example.lb4.classes.NoteAdapter;
import com.example.lb4.classes.Notes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {
    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> originalNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        SearchView searchView = findViewById(R.id.searchView);
        Spinner spinnerPriorityFilter = findViewById(R.id.spinnerPriorityFilter);


        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        originalNotes = new ArrayList<>(Notes.getNotes());
        noteAdapter = new NoteAdapter(Notes.getNotes(), this);
        notesRecyclerView.setAdapter(noteAdapter);

        Notes.getNotes().add(new Note("Погодувати кота", "Корм на вікні", 1, new Date(), new Date(), 12, ""));
        Notes.getNotes().add(new Note("Виконати лабораторну роботу", "Умови на dl.nure", 2, new Date(), new Date(), 15, ""));
        Notes.getNotes().add(new Note("Вимкнути духовку", "Вимкнути духовку", 3, new Date(), new Date(), 20, ""));

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
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = noteAdapter.getLongClickedPosition();
        if (position != -1) {
            switch (item.getItemId()) {
                case 1:
                    editNote(position);
                    return true;
                case 2:
                    deleteNote(position);
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }
    private void filterNotesByText(String query) {
        List<Note> filteredNotes = new ArrayList<>();
        if (query.isEmpty()) {
            filteredNotes.addAll(originalNotes);
        } else {
            for (Note note : originalNotes) {
                if (note.GetTitle().toLowerCase().contains(query.toLowerCase()) ||
                        note.GeDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredNotes.add(note);
                }
            }
        }
        noteAdapter.updateNotes(filteredNotes);
    }
    private void filterNotesByPriority(int position) {
        List<Note> filteredNotes = new ArrayList<>();
        if (position == 0) {
            filteredNotes.addAll(originalNotes);
        } else {
            int priority = position;
            for (Note note : originalNotes) {
                if (note.GetPriority() == priority) {
                    filteredNotes.add(note);
                }
            }
        }
        noteAdapter.updateNotes(filteredNotes);
    }
    private void editNote(int position) {
        Intent intent = new Intent(this, AddNote.class);
        intent.putExtra("note_position", position); // Передаємо позицію нотатки
        startActivity(intent);
    }

    private void deleteNote(int position) {
        Notes.getNotes().remove(position);
        noteAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onEditClick(int position) {
        editNote(position);
    }

    @Override
    public void onDeleteClick(int position) {
        deleteNote(position);
    }
    protected void onResume() {
        super.onResume();
        originalNotes.clear();
        originalNotes.addAll(Notes.getNotes());
        noteAdapter.notifyDataSetChanged();
    }
}