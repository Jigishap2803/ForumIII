package com.example.forum3.Preference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    Context context;
    private SharedPreferences.Editor editor;
    public final String PREFS_NAME = "PREFERENCE";
    private SharedPreferences reader;

    public SharedPref(Context context) {
        this.context = context;
        this.editor = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE).edit();
        this.reader = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
    }
    public void putPrefBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getPrefBoolValue(String key) {
        return reader.getBoolean(key, true);
    }

    public String putPrefValues(String key, String value) {
        editor.putString(key, value);
        editor.commit();
        return key;
    }
    public String getPrefValues(String key) {
        return reader.getString(key, "");
    }

    public void putPrefIntValues(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }
    public int getPrefIntValues(String key) {
        return reader.getInt(key, 0);
    }
}
