package com.example.ekta.noir.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ekta.noir.R;
import com.example.ekta.noir.model.UserDetails;
import com.example.ekta.noir.utils.ApplicationExtension;
import com.example.ekta.noir.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.Serializable;

import static com.example.ekta.noir.utils.Constants.EMPTY;
import static com.example.ekta.noir.utils.Constants.USER_EMAIL;
import static com.example.ekta.noir.utils.Constants.USER_NAME;
import static com.example.ekta.noir.utils.Constants.USER_PROFILE_URL;

public class LoginActivity extends BaseActivity {
    LoginButton fbLoginButton;

    // public FirebaseAuth mAuth;
    CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressBar mProgressBar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);
        ApplicationExtension.getInstance().mAuth = FirebaseAuth.getInstance();
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();

                    Utils.setUserProfileSharedPreference(getApplicationContext(), USER_NAME, name);
                    Utils.setUserProfileSharedPreference(getApplicationContext(), USER_EMAIL, email);
                    Utils.setUserProfileSharedPreference(getApplicationContext(), USER_PROFILE_URL, photoUrl.toString());


                } else {
                    // User is signed out

                    Utils.setUserProfileSharedPreference(getApplicationContext(), USER_NAME, EMPTY);
                    Utils.setUserProfileSharedPreference(getApplicationContext(), USER_EMAIL, EMPTY);
                    Utils.setUserProfileSharedPreference(getApplicationContext(), USER_PROFILE_URL, EMPTY);
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }

            }
        };
        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                fbLoginButton.setVisibility(View.INVISIBLE);

                fbLoginButton.setReadPermissions("email", "public_profile");

                // Callback registration
                fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("TAG", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG", "facebook:onCancel");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG", "facebook:onError");

                    }


                });
            }
        });


    }




    public boolean signOut() {

        FirebaseAuth.getInstance().signOut();

            LoginManager.getInstance().logOut();
            //    FirebaseAuth.getInstance().signOut();

            Log.d("sign out", "facebook");

        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        ApplicationExtension.getInstance().mAuth.addAuthStateListener(mAuthListener);
        //  ApplicationExtension.getInstance().mGoogleApiClient.connect();

    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            //   mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }



    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token.getToken());

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        ApplicationExtension.getInstance().mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user_logged_in", true);
                        startActivity(intent);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        getActivity().finish();
    }




}