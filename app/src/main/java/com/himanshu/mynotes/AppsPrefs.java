package com.himanshu.mynotes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.himanshu.mynotes.model.NoteColor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kalpesh on 14/07/20.
 */
public class AppsPrefs {

    private SharedPreferences mSharedPreference;
    private static AppsPrefs sInstance;

    private AppsPrefs(Context context) {
        mSharedPreference = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
    }

    public static AppsPrefs getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppsPrefs(context);
        }
        return sInstance;
    }

    public void saveColorsList(List<NoteColor> colors) {
        Gson gson = new Gson();
        String json = gson.toJson(colors);
        mSharedPreference.edit().putString("colorsList", json).apply();
    }

    public List<NoteColor> getColorsList() {
        String json = mSharedPreference.getString("colorsList", null);
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<NoteColor>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}
