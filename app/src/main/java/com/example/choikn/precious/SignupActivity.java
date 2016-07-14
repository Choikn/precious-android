package com.example.choikn.precious;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupActivity extends Activity {

    private ImageButton signup;
    private EditText et_name, et_email, et_id, et_password, et_pwcheck;
    private static NetworkService networkService;
    private TextView aaa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        setTitle("회원가입");

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_signup, null);
        actionBar.setCustomView(mCustomView);

        init();


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
        et_name = (EditText) findViewById(R.id.name);
        et_name.setTypeface(face);
        et_email = (EditText) findViewById(R.id.email);
        et_email.setTypeface(face);
        et_id = (EditText) findViewById(R.id.id);
        et_id.setTypeface(face);
        et_password = (EditText) findViewById(R.id.password);
        et_password.setTypeface(face);
        et_pwcheck = (EditText) findViewById(R.id.pwcheck);
        et_pwcheck.setTypeface(face);

        networkService = AppController.getNetworkService(this);

        signup = (ImageButton) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //서버에 보낼 객체에 저장시킬 값들
                String name = et_name.getText().toString();
                String username = et_id.getText().toString();
                String password = et_password.getText().toString();
                String email = et_email.getText().toString();

                User user = new User();
                user.setUsername(username);
                user.setName(name);
                user.setPassword(password);
                user.setEmail(email);

                Call<User> thumbnailCall = networkService.post_user(user);
                thumbnailCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user_temp = response.body();
                            finish();
                            Toast.makeText(getBaseContext(), "회원가입되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            int statusCode = response.code();
                            Log.i("MyTag", "응답코드 : " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                    }

                });
            }
        });

    }

    private void init() {
        et_name = (EditText) findViewById(R.id.name);
        et_email = (EditText) findViewById(R.id.email);
        et_id = (EditText) findViewById(R.id.id);
        et_password = (EditText) findViewById(R.id.password);
        et_pwcheck = (EditText) findViewById(R.id.pwcheck);
        signup = (ImageButton) findViewById(R.id.login);
    }

}
