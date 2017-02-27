package com.example.ekta.noir.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Ekta on 26-02-2017.
 */

public class Utils {
    public static void setUserProfileSharedPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences= context.getSharedPreferences("profile_shared_preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString(key,value);
        editor.apply();
        Log.d("set:key",key);
        Log.d("set:value",value+" ");
    }
    public static String getUserProfileSharedPrefs(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("profile_shared_preferences", Context.MODE_PRIVATE);
        Log.d("get:key",key);
        Log.d("get:value",sharedPref.getString(key," "));
        return sharedPref.getString(key, " ");
    }

}
