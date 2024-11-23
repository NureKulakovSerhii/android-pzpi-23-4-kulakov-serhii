package com.example.pt411;

    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    public class DBHelper extends SQLiteOpenHelper {
        private static final String USERS_DB = "Users.db";
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";

        public DBHelper(Context context) {
            super(context, USERS_DB, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_AGE + " INTEGER)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
        public void addUser(String name, int age) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + ", " + COLUMN_AGE + ") VALUES (?, ?)",
                    new Object[]{name, age});
            db.close();
        }
        public Cursor getAllUsers() {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("SELECT id AS _id, name, age FROM " + TABLE_NAME, null);
        }
    }

