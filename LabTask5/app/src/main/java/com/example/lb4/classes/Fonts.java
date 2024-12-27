package com.example.lb4.classes;

import android.content.Context;
import android.content.SharedPreferences;
public class Fonts {
    private static final String PREF_NAME = "FontSizePrefs";
    private static final String KEY_FONT_SIZE = "font_size";

    public static void saveFontSize(Context context, int fontSize) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_FONT_SIZE, fontSize);
        editor.apply();
    }

    public static int getFontSize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_FONT_SIZE, 14);
    }
}
