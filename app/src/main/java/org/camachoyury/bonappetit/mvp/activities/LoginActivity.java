package org.camachoyury.bonappetit.mvp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import org.camachoyury.bonappetit.R;
import org.camachoyury.bonappetit.WallActivity;
import org.camachoyury.bonappetit.YLog;

/**
 * Created by yury on 3/16/17.
 */

public class LoginActivity  extends AppCompatActivity {


    private LoginButton loginButton;
    CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    public String TAG = "LoginActivity";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //create a facebook CallManager for
        callbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();
        loginButton = (LoginButton) findViewById(R.id.fb_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());
                goMainScreen();
            }

            @Override
            public void onCancel() {
//                UtilsView.showLongMessage(, this, R.string.login_fb_cancel);
            }

            @Override
            public void onError(FacebookException error) {

            }


        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void goMainScreen(){
        Intent intent = new Intent(this, WallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    private void handleFacebookAccessToken(AccessToken accessToken){

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                YLog.debug("TAG","signInWithCredential:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()){

                    UtilsView.showLongMessage(findViewById(R.id.login_layout),LoginActivity.this,R.string.error_login);
                }
            }
        });

    }
}
