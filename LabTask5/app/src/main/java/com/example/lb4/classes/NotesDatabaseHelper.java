package com.example.lb4.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_CREATED_DATE = "created_date";
    private static final String COLUMN_DUE_DATE = "due_date";
    private static final String COLUMN_DUE_HOUR = "due_hour";
    private static final String COLUMN_PHOTO_PATH = "photo_path";

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NOTES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRIORITY + " INTEGER, " +
                COLUMN_CREATED_DATE + " INTEGER, " +
                COLUMN_DUE_DATE + " INTEGER, " +
                COLUMN_DUE_HOUR + " INTEGER, " +
                COLUMN_PHOTO_PATH + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.GetTitle());
        values.put(COLUMN_DESCRIPTION, note.GeDescription());
        values.put(COLUMN_PRIORITY, note.GetPriority());
        values.put(COLUMN_CREATED_DATE, note.GetDate().getTime());
        values.put(COLUMN_DUE_DATE, note.GetDueDate().getTime());
        values.put(COLUMN_DUE_HOUR, note.GetDueHour());
        values.put(COLUMN_PHOTO_PATH, note.GetPhotoPath());

        long id = db.insert(TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public int updateNote(Note note, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.GetTitle());
        values.put(COLUMN_DESCRIPTION, note.GeDescription());
        values.put(COLUMN_PRIORITY, note.GetPriority());
        values.put(COLUMN_CREATED_DATE, note.GetDate().getTime());
        values.put(COLUMN_DUE_DATE, note.GetDueDate().getTime());
        values.put(COLUMN_DUE_HOUR, note.GetDueHour());
        values.put(COLUMN_PHOTO_PATH, note.GetPhotoPath());
        int rows = db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, COLUMN_CREATED_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY)),
                        new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE))),
                        new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE))),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DUE_HOUR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH))
                );
                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notes;
    }
}
