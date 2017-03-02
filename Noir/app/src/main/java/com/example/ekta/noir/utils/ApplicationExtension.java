package com.example.ekta.noir.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Ekta on 26-02-2017.
 */

public class ApplicationExtension extends Application {
    private Context context;
    private static ApplicationExtension sInstance;
    public FirebaseAuth mAuth;
    CallbackManager callbackManager;
    SignInButton gmailSignInButton;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate() {
        super.onCreate();

    }



//    private ApplicationExtension() {
//    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static ApplicationExtension getInstance() {
        if (sInstance == null) {
            sInstance = new ApplicationExtension();
        }
        return sInstance;
    }

}
