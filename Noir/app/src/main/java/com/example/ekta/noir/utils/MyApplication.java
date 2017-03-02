package com.example.ekta.noir.utils;

/**
 * Created by Ekta on 24-02-2017.
 */
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{

    private static MyApplication sInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getsInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}

