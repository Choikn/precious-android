package com.example.choikn.precious.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choikn.precious.listAdapter.ArticlesAdapter;
import com.example.choikn.precious.listAdapter.Articles_items;
import com.example.choikn.precious.listAdapter.SearchListAdapter;
import com.example.choikn.precious.listAdapter.SearchList_items;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.Article;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.R;
import com.example.choikn.precious.server.Product;
import com.example.choikn.precious.server.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChoiKN on 2016-07-15.
 */
public class ProductActivity extends Activity {

    private ImageButton menu, search;
    private ImageView image;
    private ListView list, list_articles;
    private DrawerLayout dlMain;
    private LinearLayout layoutDrawer;
    private NetworkService networkService;
    private TextView name, email, minprice, maxprice, avgprice, a,b,c;
    private ArticlesAdapter adapter2;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/Cocogoose_trial.otf");
        Typeface face2 = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
        Typeface face3 = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Medium.otf");
        Typeface face4 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        networkService = AppController.getNetworkService(this);

        image = (ImageView)findViewById(R.id.image);
        minprice = (TextView)findViewById(R.id.minprice);
        maxprice = (TextView)findViewById(R.id.maxprice);
        avgprice = (TextView)findViewById(R.id.avgprice);

        a = (TextView) findViewById(R.id.a);
        a.setTypeface(face3);
        b = (TextView) findViewById(R.id.b);
        b.setTypeface(face3);
        c = (TextView) findViewById(R.id.c);
        c.setTypeface(face3);

        layoutDrawer = (LinearLayout) findViewById(R.id.layoutDrawer);
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);

        menu = (ImageButton) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlMain.openDrawer(layoutDrawer);
            }
        });
        search = (ImageButton) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), PostWriteActivity.class);
                intent2.putExtra("id", id);
                startActivityForResult(intent2, 0);
            }
        });

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

        list = (ListView) findViewById(R.id.list);
        final ArrayList<String> items = new ArrayList<>();
        items.add("1");
        items.add("2");
        items.add("3");

        CustomAdapter adapter = new CustomAdapter(this, 0, items);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2){
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });

        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");

        list_articles = (ListView)findViewById(R.id.list_articles);
        adapter2 = new ArticlesAdapter();
        Call<List<Article>> articles = networkService.getarticles(id);
        articles.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    List<Article> article_tmp = response.body();
                    for (final Article article : article_tmp) {
                        adapter2.addItem(article.getId(), article.isFavorite(), article.getPhoto(), article.getName(), Integer.toString(article.getPrice()));
                    }
                    list_articles.setAdapter(adapter2);
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {

            }
        });

        list_articles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Articles_items item = (Articles_items) parent.getItemAtPosition(position);

                int itemId = item.getpId();

                Intent intent = new Intent(getApplicationContext(), ProductpostActivity.class);
                intent.putExtra("id", itemId);
                startActivityForResult(intent, 0);
            }
        });

        Call<Product> product = networkService.productID(id);
        product.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product_tmp = response.body();
                    new DownloadImageTask(image).execute(product_tmp.getPhoto());
                    minprice.setText(String.valueOf(product_tmp.getMinPrice()));
                    maxprice.setText(String.valueOf(product_tmp.getMaxPrice()));
                    avgprice.setText(String.valueOf(product_tmp.getAvgPrice()));
                } else {
                    int statusCode = response.code();
                    Log.i("MyTag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });

        Call<User> thumbnailCall = networkService.getNameEmail();
        thumbnailCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user_tmp = response.body();
                    Log.e("MyTag", user_tmp.getName());
                    name.setText(user_tmp.getName());
                    email.setText(user_tmp.getEmail());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Call<List<Article>> articles = networkService.getarticles(id);
        articles.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    adapter2.reset();
                    List<Article> article_tmp = response.body();
                    for (final Article article : article_tmp) {
                        adapter2.addItem(article.getId(), article.isFavorite(), article.getPhoto(), article.getName(), Integer.toString(article.getPrice()));
                    }
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {

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

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list, null);
            }

            // ImageView 인스턴스
            ImageView imageView = (ImageView) v.findViewById(R.id.imageview);

            // 리스트뷰의 아이템에 이미지를 변경한다.
            if ("1".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_1);
            else if ("2".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_3);
            else if ("3".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_4);

            return v;
        }

    }

}
