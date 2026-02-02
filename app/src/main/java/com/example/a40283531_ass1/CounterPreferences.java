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
 * - chronological event press log (stored as button numbers 1..3)
 *
 * Supports DataActivity modes:
 * - Event name mode (map 1..3 -> current names)
 * - Event Button # mode (show 1..3)
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

    private static final int MIN_COUNT = 5;
    private static final int MAX_COUNT = 200;
    private static final String KEY_TOTAL_COUNT    = "total_count";

    /**
     * Chronological history (JSON array stored as string).
     * IMPORTANT: We store the BUTTON NUMBER (1..3), not the name.
     * This lets DataActivity switch modes and also keeps history stable even if names change.
     */
    private static final String KEY_EVENT_LOG_JSON = "event_log_json";

    private final SharedPreferences prefs;

    public CounterPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ----------------------------
    // Names (empty default means "not set yet")
    // ----------------------------
    public String getCounter1Name() { return prefs.getString(KEY_COUNTER1_NAME, ""); }
    public String getCounter2Name() { return prefs.getString(KEY_COUNTER2_NAME, ""); }
    public String getCounter3Name() { return prefs.getString(KEY_COUNTER3_NAME, ""); }

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
    public int getCount1() { return prefs.getInt(KEY_COUNTER1_COUNT, 0); }
    public int getCount2() { return prefs.getInt(KEY_COUNTER2_COUNT, 0); }
    public int getCount3() { return prefs.getInt(KEY_COUNTER3_COUNT, 0); }

    // Backward-compatible getters (if other code calls these)
    public int getCounter1Count() { return getCount1(); }
    public int getCounter2Count() { return getCount2(); }
    public int getCounter3Count() { return getCount3(); }

    public int getTotalCount() { return prefs.getInt(KEY_TOTAL_COUNT, 0); }

    /**
     * Increments an individual counter (1..3) AND total count.
     * Also logs the EVENT BUTTON NUMBER chronologically (oldest -> newest).
     */
    public boolean incrementEvent(int eventNum) {
        // Validate event number
        if (eventNum < 1 || eventNum > 3) return false;

        // Enforce max on TOTAL (change this if you want max per-counter instead)
        if (getTotalCount() >= MAX_COUNT) {
            return false; // maximum reached
        }

        SharedPreferences.Editor editor = prefs.edit();

        if (eventNum == 1) {
            editor.putInt(KEY_COUNTER1_COUNT, getCount1() + 1);
        } else if (eventNum == 2) {
            editor.putInt(KEY_COUNTER2_COUNT, getCount2() + 1);
        } else { // eventNum == 3
            editor.putInt(KEY_COUNTER3_COUNT, getCount3() + 1);
        }

        // Update total
        editor.putInt(KEY_TOTAL_COUNT, getTotalCount() + 1);

        // Log history (button number 1..3)
        addEventNumberToLogInternal(editor, eventNum);

        editor.apply();
        return true;
    }


    // ----------------------------
    // Event history (chronological)
    // ----------------------------

    /**
     * Returns event history as button numbers (1..3), oldest -> newest.
     * This is what DataActivity should use.
     */
    public ArrayList<Integer> getEventHistory() {
        String json = prefs.getString(KEY_EVENT_LOG_JSON, "[]");
        ArrayList<Integer> result = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                // Accept either int or string (for safety / backward compatibility)
                int val;
                try {
                    val = arr.getInt(i);
                } catch (JSONException e) {
                    String s = arr.optString(i, "");
                    val = parseEventNumberFromString(s);
                }

                if (val >= 1 && val <= 3) result.add(val);
            }
        } catch (JSONException ignored) {
            // If corrupted, return empty list
        }

        return result;
    }

    /**
     * Backward-compatible API (if old code expects List<String>).
     * Returns the history mapped to CURRENT names (empty names will be skipped).
     */
    public List<String> getEventLog() {
        ArrayList<Integer> nums = getEventHistory();
        List<String> out = new ArrayList<>();

        for (int n : nums) {
            String name = getNameForEvent(n);
            if (!name.isEmpty()) out.add(name);
        }

        return out;
    }

    /**
     * Converts an event number (1..3) to the current name (or "" if none).
     */
    public String getNameForEvent(int eventNum) {
        switch (eventNum) {
            case 1: return safeTrim(getCounter1Name());
            case 2: return safeTrim(getCounter2Name());
            case 3: return safeTrim(getCounter3Name());
            default: return "";
        }
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

    /**
     * Adds eventNum (1..3) to the log using the SAME editor (atomic save).
     */
    private void addEventNumberToLogInternal(SharedPreferences.Editor editor, int eventNum) {
        String json = prefs.getString(KEY_EVENT_LOG_JSON, "[]");

        try {
            JSONArray arr = new JSONArray(json);
            arr.put(eventNum);
            editor.putString(KEY_EVENT_LOG_JSON, arr.toString());
        } catch (JSONException ignored) {
            JSONArray arr = new JSONArray();
            arr.put(eventNum);
            editor.putString(KEY_EVENT_LOG_JSON, arr.toString());
        }
    }

    /**
     * Backward-compat: If your old log stored strings like "Event A" / names,
     * there is no safe way to map those back to 1..3.
     * But if it stored "1"/"2"/"3" as strings, we can recover.
     */
    private int parseEventNumberFromString(String s) {
        if (s == null) return -1;
        s = s.trim();
        if (s.equals("1")) return 1;
        if (s.equals("2")) return 2;
        if (s.equals("3")) return 3;
        return -1;
    }
}
