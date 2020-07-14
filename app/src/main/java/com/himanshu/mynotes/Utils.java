package com.himanshu.mynotes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kalpesh on 14/07/20.
 */
class Utils {

    private SharedPreferences mSharedPreference;
    private static Utils sInstance;

    private Utils(Context context) {
        mSharedPreference = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
    }

    public static Utils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Utils(context);
        }
        return sInstance;
    }

    public void saveColorsList(List<String> colors) {
        mSharedPreference.edit().putStringSet("colorsList", new HashSet<>(colors)).apply();
    }

    public List<String> getColorsList() {
        Set<String> stringSet = mSharedPreference.getStringSet("colorsList", null);
        if (stringSet == null) {
            return null;
        }
        return new ArrayList<>(stringSet);
    }

}
