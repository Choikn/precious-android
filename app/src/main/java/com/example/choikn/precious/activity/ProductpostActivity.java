package com.example.choikn.precious.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choikn.precious.R;
import com.example.choikn.precious.listAdapter.Articles_items;
import com.example.choikn.precious.listAdapter.SearchList_items;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.Article;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.server.Product;

import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChoiKN on 2016-07-18.
 */
public class ProductpostActivity extends Activity {

    private static NetworkService networkService;
    private TextView title, a, price, content;
    private ImageView image;
    private ImageButton favorite;
    private View contact;

    Article article;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpost);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/Cocogoose_trial.otf");
        Typeface face2 = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
        Typeface face3 = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Medium.otf");
        Typeface face4 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        title = (TextView)findViewById(R.id.title);
        title.setTypeface(face3);
        a = (TextView)findViewById(R.id.a);
        a.setTypeface(face3);
        price = (TextView)findViewById(R.id.price);
        price.setTypeface(face2);
        content = (TextView)findViewById(R.id.content);
        content.setTypeface(face2);
        image = (ImageView)findViewById(R.id.image);

        contact = findViewById(R.id.contact);

        favorite = (ImageButton)findViewById(R.id.favorite);

        Intent intent = getIntent();
        int id = intent.getExtras().getInt("id");

        networkService = AppController.getNetworkService(this);

        Call<Article> articleid = networkService.articleID(id);
        articleid.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.isSuccessful()) {
                    article = response.body();
                    Article article_tmp = response.body();
                    title.setText(article_tmp.getName());
                    price.setText(article_tmp.getPrice() + "원");
                    content.setText(article_tmp.getDescription());
                    if (article_tmp.getPhoto().length() >= 2) {
                        Log.e("MyTag", article_tmp.getPhoto().substring(2, article_tmp.getPhoto().length() - 2));
                        new DownloadImageTask(image).execute("http://precious.kkiro.kr/" + article_tmp.getPhoto().substring(2, article_tmp.getPhoto().length() - 2));
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Articles_items obj = (Articles_items) v.getTag();
                if (obj.isFavorite() == false) {
                    Call<Article> article = networkService.at_favorite(obj.getpId());
                    article.enqueue(new Callback<Article>() {
                        @Override
                        public void onResponse(Call<Article> call, Response<Article> response) {
                            if (response.isSuccessful()) {
                                favorite.setImageResource(R.drawable.heart_1);
                                obj.setFavorite(false);
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
                }else{
                    Call<Article> article = networkService.at_favorite_dt(obj.getpId());
                    article.enqueue(new Callback<Article>() {
                        @Override
                        public void onResponse(Call<Article> call, Response<Article> response) {
                            if (response.isSuccessful()) {
                                favorite.setImageResource(R.drawable.heart_2);
                                obj.setFavorite(false);
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
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + article.getUser().getPhone()));
                startActivity(sendIntent);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
