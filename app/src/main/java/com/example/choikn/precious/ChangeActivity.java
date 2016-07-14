package com.example.choikn.precious;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class ChangeActivity extends Activity{

    private Button email;
    private Button facebook;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_change);
        callbackManager = CallbackManager.Factory.create();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        email = (Button)findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act = new Intent(ChangeActivity.this, SignupActivity.class);
                startActivity(act);
            }
        });
        facebook = (Button)findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(ChangeActivity.this, Arrays.asList("public_profile", "email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Intent act = new Intent(ChangeActivity.this, MaActivity.class);
                        startActivity(act);
                    }

                    @Override
                    public void onCancel() {
                        Log.e("onCancel", "onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
            }
        });

    }

    @Override
    protected  void onActivityResult(int requesCode, int resultCode, Intent data){
        super.onActivityResult(requesCode, resultCode, data);
        callbackManager.onActivityResult(requesCode, resultCode, data);
    }

}
