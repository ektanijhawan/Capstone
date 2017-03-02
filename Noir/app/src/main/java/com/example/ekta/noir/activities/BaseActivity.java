package com.example.ekta.noir.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Ekta on 26-02-2017.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Context getContext()
    {
        return this;
    }

    public boolean signOut() {


        try {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Log.d("sign out", "facebook");
        } catch (Exception e) {

            // Firebase sign out
        }
        return true;

    }
}
