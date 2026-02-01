package com.example.a40283531_ass1;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * MVC Model (and persistence via SharedPreferences).
 * Stores:
 * - 3 event names
 * - 3 individual counts
 * - total count
 * - chronological event press log
 */
public class CounterPreferences {

    private static final String PREF_NAME = "AppSettings";

    // Names
    private static final String KEY_COUNTER1_NAME = "counter1_name";
    private static final String KEY_COUNTER2_NAME = "counter2_name";
    private static final String KEY_COUNTER3_NAME = "counter3_name";

    // Counts
    private static final String KEY_COUNTER1_COUNT = "counter1_count";
    private static final String KEY_COUNTER2_COUNT = "counter2_count";
    private static final String KEY_COUNTER3_COUNT = "counter3_count";
    private static final String KEY_TOTAL_COUNT    = "total_count";

    // Chronological history (JSON array stored as string)
    private static final String KEY_EVENT_LOG_JSON = "event_log_json";

    private final SharedPreferences prefs;

    public CounterPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ----------------------------
    // Names (empty default means "not set yet")
    // ----------------------------
    public String getCounter1Name() {
        return prefs.getString(KEY_COUNTER1_NAME, "");
    }

    public String getCounter2Name() {
        return prefs.getString(KEY_COUNTER2_NAME, "");
    }

    public String getCounter3Name() {
        return prefs.getString(KEY_COUNTER3_NAME, "");
    }

    public void setCounter1Name(String name) {
        prefs.edit().putString(KEY_COUNTER1_NAME, safeTrim(name)).apply();
    }

    public void setCounter2Name(String name) {
        prefs.edit().putString(KEY_COUNTER2_NAME, safeTrim(name)).apply();
    }

    public void setCounter3Name(String name) {
        prefs.edit().putString(KEY_COUNTER3_NAME, safeTrim(name)).apply();
    }

    public void setCounterNames(String n1, String n2, String n3) {
        prefs.edit()
                .putString(KEY_COUNTER1_NAME, safeTrim(n1))
                .putString(KEY_COUNTER2_NAME, safeTrim(n2))
                .putString(KEY_COUNTER3_NAME, safeTrim(n3))
                .apply();
    }

    /** Returns true if at least one event name exists */
    public boolean hasAnyCounterNames() {
        return !getCounter1Name().isEmpty()
                || !getCounter2Name().isEmpty()
                || !getCounter3Name().isEmpty();
    }

    // ----------------------------
    // Counts
    // ----------------------------
    public int getCounter1Count() {
        return prefs.getInt(KEY_COUNTER1_COUNT, 0);
    }

    public int getCounter2Count() {
        return prefs.getInt(KEY_COUNTER2_COUNT, 0);
    }

    public int getCounter3Count() {
        return prefs.getInt(KEY_COUNTER3_COUNT, 0);
    }

    public int getTotalCount() {
        return prefs.getInt(KEY_TOTAL_COUNT, 0);
    }

    /** Increments an individual counter (1..3) AND total count. Also logs the event name chronologically. */
    public void incrementEvent(int eventNum) {
        SharedPreferences.Editor editor = prefs.edit();

        if (eventNum == 1) {
            editor.putInt(KEY_COUNTER1_COUNT, getCounter1Count() + 1);
            addEventToLogInternal(editor, getCounter1Name());
        } else if (eventNum == 2) {
            editor.putInt(KEY_COUNTER2_COUNT, getCounter2Count() + 1);
            addEventToLogInternal(editor, getCounter2Name());
        } else if (eventNum == 3) {
            editor.putInt(KEY_COUNTER3_COUNT, getCounter3Count() + 1);
            addEventToLogInternal(editor, getCounter3Name());
        } else {
            return; // invalid
        }

        editor.putInt(KEY_TOTAL_COUNT, getTotalCount() + 1);
        editor.apply();
    }

    // ----------------------------
    // Event log (chronological)
    // ----------------------------
    public List<String> getEventLog() {
        String json = prefs.getString(KEY_EVENT_LOG_JSON, "[]");
        List<String> result = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                result.add(arr.optString(i, ""));
            }
        } catch (JSONException ignored) {
            // If corrupted, return empty list
        }

        return result;
    }

    public void resetAll() {
        prefs.edit()
                .putInt(KEY_COUNTER1_COUNT, 0)
                .putInt(KEY_COUNTER2_COUNT, 0)
                .putInt(KEY_COUNTER3_COUNT, 0)
                .putInt(KEY_TOTAL_COUNT, 0)
                .putString(KEY_EVENT_LOG_JSON, "[]")
                .apply();
    }

    // ----------------------------
    // Helpers
    // ----------------------------
    private static String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }

    /** Adds eventName to log using the SAME editor (so increment + log + total save together). */
    private void addEventToLogInternal(SharedPreferences.Editor editor, String eventName) {
        String name = safeTrim(eventName);
        if (name.isEmpty()) return; // don't log blank

        String json = prefs.getString(KEY_EVENT_LOG_JSON, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            arr.put(name);
            editor.putString(KEY_EVENT_LOG_JSON, arr.toString());
        } catch (JSONException ignored) {
            // If something goes wrong, reset log and add this one entry
            JSONArray arr = new JSONArray();
            arr.put(name);
            editor.putString(KEY_EVENT_LOG_JSON, arr.toString());
        }
    }
}
