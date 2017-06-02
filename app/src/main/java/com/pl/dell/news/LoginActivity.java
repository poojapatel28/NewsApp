package com.pl.dell.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.widget.Toast.makeText;

public class LoginActivity extends BaseActivity implements View.OnClickListener {



    DemoProgress p;
    Activity a;

    User u;
        private static final String TAG = "FacebookLogin";

        private TextView mStatusTextView;

        // [START declare_auth]
        private FirebaseAuth mAuth;
        // [END declare_auth]

        private CallbackManager mCallbackManager;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_login);
            p=new DemoProgress(LoginActivity.this);

            // Views
            mStatusTextView = (TextView) findViewById(R.id.status);

            findViewById(R.id.button_facebook_signout).setOnClickListener(this);
            u= new User();

            // [START initialize_auth]
            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            // [END initialize_auth]
            // [START initialize_fblogin]
            // Initialize Facebook Login button
            mCallbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                }
            });
            // [END initialize_fblogin]
        }

        // [START on_start_check_user]
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
        // [END on_start_check_user]

        // [START on_activity_result]
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        // [END on_activity_result]

        // [START auth_with_facebook]
        private void handleFacebookAccessToken(AccessToken token) {
            Log.d(TAG, "handleFacebookAccessToken:" + token);
            // [START_EXCLUDE silent]
            p.showProgress("Wait","Loading");
            // [END_EXCLUDE]

            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                            p.hideProgress();


                            // [END_EXCLUDE]
                        }


                    });
        }
        // [END auth_with_facebook]

        public void signOut() {
            mAuth.signOut();
            LoginManager.getInstance().logOut();

            updateUI(null);
        }

        private void updateUI(FirebaseUser user) {

            if (user != null) {

               u.setName(user.getDisplayName());
               u.setEmail(user.getEmail());
                u.setUrltopic(user.getPhotoUrl().toString());




                mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));

                findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
                findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       startActivity(new Intent(getApplicationContext(),ChooseActivity.class));
                       finish();
                   }
               });
            } else {
                mStatusTextView.setText(R.string.signed_out);


                findViewById(R.id.button_facebook_login).setVisibility(View.VISIBLE);
                findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.button_facebook_signout) {
                signOut();
            }
        }

    public void onBackPressed() {



                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);

                    }

    }
