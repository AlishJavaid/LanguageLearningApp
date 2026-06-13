package com.example.languagelearningapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_XP = "user_xp";
    private static final String KEY_STREAK = "user_streak";
    private static final String KEY_LAST_DATE = "last_date";

    private SharedPreferences prefs;

    public PreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setDarkMode(boolean isEnabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, isEnabled).apply();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public void addXp(int amount) {
        int currentXp = prefs.getInt(KEY_XP, 0);
        prefs.edit().putInt(KEY_XP, currentXp + amount).apply();
    }

    public int getXp() {
        return prefs.getInt(KEY_XP, 0);
    }

    public int getStreak() {
        return prefs.getInt(KEY_STREAK, 0);
    }
    
    public void updateStreak() {
        // Simple streak logic: check last date and increment or reset
        // For simplicity in this example, we just return a mock value if not fully implemented
    }
}
