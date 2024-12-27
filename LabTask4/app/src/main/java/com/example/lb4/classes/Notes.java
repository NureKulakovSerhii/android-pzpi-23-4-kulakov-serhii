package com.example.lb4.classes;
import com.example.lb4.classes.Note;

import java.util.ArrayList;
import java.util.List;
public class Notes {
    private static List<Note> notes = new ArrayList<>();
    public static List<Note> getNotes(){
      return notes;
    };
    private static void AddNote(Note note){
        notes.add(note);
    }
    public static void updateNote(Note note, int index){
        notes.set(index, note);
    }
    public static void deleteNote(Note note, int index){
        notes.remove(index);
    }
}
