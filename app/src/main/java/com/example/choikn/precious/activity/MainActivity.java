package com.example.choikn.precious.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.choikn.precious.ActivityManager;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.R;
import com.example.choikn.precious.server.User;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView logo;
    private EditText et_username, et_password;
    private ImageButton login, facebook_login;
    private Button signup;
    private RelativeLayout hide;
    private Animation up, appear;
    private static NetworkService networkService;
    private CallbackManager callbackManager;
    protected AccessTokenTracker accessTokenTracker;
    private ActivityManager am = ActivityManager.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        am.addActivity(this);
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SplashActivity.class));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        logo = (ImageView) findViewById(R.id.loginlogo);
        up = AnimationUtils.loadAnimation(this, R.anim.up);
        hide = (RelativeLayout) findViewById(R.id.hide);
        appear = AnimationUtils.loadAnimation(this, R.anim.appear);

        logo.setAnimation(up);
        hide.setAnimation(appear);

        et_username = (EditText) findViewById(R.id.username);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        et_username.setTypeface(face);
        et_username.setText("rnjssmd34");

        et_password = (EditText) findViewById(R.id.password);
        et_password.setText("rnjs1598");
        et_password.setTypeface(face);

        signup = (Button) findViewById(R.id.signup);
        Typeface face2 = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
        signup.setTypeface(face2);
        signup.setOnClickListener(this);

        login = (ImageButton) findViewById(R.id.login);
        login.setOnClickListener(this);
        facebook_login = (ImageButton) findViewById(R.id.facebook_login);
        facebook_login.setOnClickListener(this);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                requestLogin(currentAccessToken);
            }
        };

        accessTokenTracker.startTracking();

        networkService = AppController.getNetworkService(this);

        initialize();
    }

    protected void requestLogin(AccessToken token) {
        if (token == null) return;
        // Request login on the application server here.
        Log.e("MainActivity", "Logging in..");
        Log.e("MainActivity", token.getToken());
        networkService.fb_user_check(token.getToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.e("MainActivity", response.message());
                    return;
                }
                // Logged in!
                Log.e("MainActivity", "Logged in!");
                // Again, proceed to main screen
                User.current = response.body();
                startActivity(new Intent(getApplicationContext(), MaActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requesCode, int resultCode, Intent data) {
        super.onActivityResult(requesCode, resultCode, data);
        callbackManager.onActivityResult(requesCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }


    public void onClick(View view) {
        if (view == signup) {
            Intent act = new Intent(this, SignupActivity.class);
            startActivity(act);
        } else if (view == login) {
            final Intent act = new Intent(this, MaActivity.class);
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();
            Call<User> usernamepw = networkService.user_check(username, password);
            usernamepw.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        startActivity(act);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), "비밀번호 또는 아이디가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                }
            });
        } else if (view == facebook_login) {
            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("TAG", loginResult.getAccessToken().getToken());
                    Log.d("TAG", loginResult.getAccessToken().getUserId());
                }

                @Override
                public void onCancel() {
                    Log.e("onCancel", "취소됨");
                }

                @Override
                public void onError(FacebookException error) {
                    error.printStackTrace();
                }
            });
        }
    }


    private void setAnimation(Animation animation) {

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        logo.startAnimation(animation);
        hide.startAnimation(animation);
    }

    private void initialize() {
        InitializationRunnable init = new InitializationRunnable();
        new Thread(init).start();
    }

    class InitializationRunnable implements Runnable {
        public void run() {
        }
    }

}
