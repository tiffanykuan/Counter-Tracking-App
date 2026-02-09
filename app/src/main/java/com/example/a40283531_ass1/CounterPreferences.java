//Tiffany Kuan
//40283531
//COEN 390 - Programming Assignment 1

package com.example.a40283531_ass1;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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

    // minimum and maximim limit
    private static final int MIN_COUNT = 5;
    private static final int MAX_COUNT = 200;

    private static final String KEY_TOTAL_COUNT    = "total_count";

    private static final String KEY_COUNTER_LIMIT = "counter_limit";

    private static final String KEY_EVENT_LOG_JSON = "event_log_json";

    private final SharedPreferences prefs;

    public CounterPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getCounterLimit() {
        // Default to MIN_COUNT if never set
        int val = prefs.getInt(KEY_COUNTER_LIMIT, MIN_COUNT);
        return clamp(val, MIN_COUNT, MAX_COUNT);
    }

    public void setCounterLimit(int limit) {
        int clamped = clamp(limit, MIN_COUNT, MAX_COUNT);
        prefs.edit().putInt(KEY_COUNTER_LIMIT, clamped).apply();
    }

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

    public boolean hasAnyCounterNames() {
        return !getCounter1Name().isEmpty()
                || !getCounter2Name().isEmpty()
                || !getCounter3Name().isEmpty();
    }

    public int getCount1() { return prefs.getInt(KEY_COUNTER1_COUNT, 0); }
    public int getCount2() { return prefs.getInt(KEY_COUNTER2_COUNT, 0); }
    public int getCount3() { return prefs.getInt(KEY_COUNTER3_COUNT, 0); }

    public int getCounter1Count() { return getCount1(); }
    public int getCounter2Count() { return getCount2(); }
    public int getCounter3Count() { return getCount3(); }
    public int getTotalCount() { return prefs.getInt(KEY_TOTAL_COUNT, 0); }

    public boolean incrementEvent(int eventNum) {
       //Validate an event number
        if (eventNum < 1 || eventNum > 3) return false;

        if (getTotalCount() >= getCounterLimit()) {
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

 //Event in chronological order
    public ArrayList<Integer> getEventHistory() {
        String json = prefs.getString(KEY_EVENT_LOG_JSON, "[]");
        ArrayList<Integer> result = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
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

        }

        return result;
    }

    public List<String> getEventLog() {
        ArrayList<Integer> nums = getEventHistory();
        List<String> out = new ArrayList<>();

        for (int n : nums) {
            String name = getNameForEvent(n);
            if (!name.isEmpty()) out.add(name);
        }

        return out;
    }

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

    private static String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

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

    private int parseEventNumberFromString(String s) {
        if (s == null) return -1;
        s = s.trim();
        if (s.equals("1")) return 1;
        if (s.equals("2")) return 2;
        if (s.equals("3")) return 3;
        return -1;
    }
}
