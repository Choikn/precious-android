package com.example.choikn.precious.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> origin/test

import com.example.choikn.precious.R;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.Article;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.server.Product;
import com.example.choikn.precious.server.User;

<<<<<<< HEAD
=======
import java.io.IOException;

>>>>>>> origin/test
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostWriteActivity extends Activity {

    private EditText title, price, content;
    private ImageView choice, picture;
    private ImageButton jaksung;
    private static NetworkService networkService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postwrite);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        networkService = AppController.getNetworkService(this);

        TextView ti = (TextView) findViewById(R.id.ti);
        ti.setText("게시글 작성");
        ti.setPaintFlags(ti.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
<<<<<<< HEAD
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Cocogoose_trial.otf");
=======
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Medium.otf");
>>>>>>> origin/test
        ti.setTypeface(face);

        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        content = (EditText) findViewById(R.id.content);

        choice = (ImageView) findViewById(R.id.choice);
        picture = (ImageView) findViewById(R.id.picture);

        jaksung = (ImageButton) findViewById(R.id.jaksung);

<<<<<<< HEAD
        Intent intent = getIntent();

        jaksung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody etitle = RequestBody.create(MediaType.parse("text/plain"), title.getText().toString());
                RequestBody eprice = RequestBody.create(MediaType.parse("text/plain"), );
                RequestBody econtent = RequestBody.create(MediaType.parse("text/plain"), content.getText().toString());
                Call<Article> write = networkService.write(etitle, eprice, econtent, id);
                write.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        if (response.isSuccessful()) {

=======
        jaksung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = getIntent();
                int id = intent2.getExtras().getInt("id");
                String etitle = title.getText().toString();
                String econtent = content.getText().toString();
                int eprice = Integer.parseInt(price.getText().toString());
                Call<Article> write = networkService.write(id, etitle, eprice, econtent);
                write.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        try {
                            Log.e("myTag", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (response.isSuccessful()) {
                            finish();
>>>>>>> origin/test
                        } else {
                            int statusCode = response.code();
                            Log.i("MyTag", "응답코드 : " + statusCode);
                        }
                    }
                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {
                        Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                    }
                });
            }
        });

    }
}
