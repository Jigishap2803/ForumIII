package com.example.forum3.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class PreferenceManager {
    public static final String DATE_FIRST_LAUNCH = "date_firstlaunch";
    private Context context;
    private SharedPreferences.Editor editor;
    public final String MY_PREFS_NAME = "myPreference";
    private SharedPreferences prefs;

    public PreferenceManager(Context context) {
        this.context = context;
        this.editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        this.prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);

    }

    private static final String MY_PREFERENCES = "my_preferences";

    public static boolean isFirst(Context context){
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if(first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return first;
    }


    public void putPreferenceBoolValues(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getPreferenceBoolValues(String key) {
        return prefs.getBoolean(key, true);
    }
    public boolean getPrefBoolValues(String key) {
        return prefs.getBoolean(key, false);
    }
    public String putPreferenceValues(String key, String value) {
        editor.putString(key, value);
        editor.commit();
        return key;
    }


    public int putPreferenceIntValues(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
        return value;
    }

    public void putPreferenceListValues(String key, Set<String> value){
        editor.putStringSet(key,value);
        editor.commit();
    }

    /* public void putPreferencefloatValues(String key, float value) {
         editor.putFloat(key, value);
         editor.commit();
     }

     public float getPreferencefloatValues(String key) {
         return prefs.getFloat(key, 0);
     }
 */
    public Set<String> getPreferenceListValues(String key)
    {
        Set<String> set=new HashSet<String>();
        return prefs.getStringSet(key,set);
    }

    public int getPreferenceIntValues(String key) {
        return prefs.getInt(key, 0);
    }

    public String getPreferenceValues(String key) {
        return prefs.getString(key, "");
    }

    public void clearSharedPreferance() {
        prefs.edit().clear().commit();
    }

    public void clearSharedPreferance(String key) {
        prefs.edit().remove(key).commit();
    }


    public void putPreferencDoubleValues(String key, double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    public double getPreferencDoubleValues(String key, double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

}
