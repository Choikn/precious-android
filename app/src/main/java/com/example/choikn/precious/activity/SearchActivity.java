package com.example.choikn.precious.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.choikn.precious.ActivityManager;
import com.example.choikn.precious.listAdapter.SearchList_items;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.server.Product;
import com.example.choikn.precious.R;
import com.example.choikn.precious.listAdapter.SearchListAdapter;
import com.example.choikn.precious.server.User;

import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChoiKN on 2016-06-28.
 */
public class SearchActivity extends Activity {

    private ImageButton delete, search;
    private ImageView photo, favorite;
    private EditText searchbox;
    private ActivityManager am = ActivityManager.getInstance();
    private NetworkService networkService;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        am.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        networkService = AppController.getNetworkService(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        delete = (ImageButton) findViewById(R.id.delete);
        search = (ImageButton) findViewById(R.id.search);
        searchbox = (EditText) findViewById(R.id.searchbox);
        favorite = (ImageView) findViewById(R.id.favorite);
        photo = (ImageView) findViewById(R.id.photo);

        list = (ListView) findViewById(R.id.search_list);
        list.setFocusable(false);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchList_items item = (SearchList_items) parent.getItemAtPosition(position);

                int itemId = item.getpId();

                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("id", itemId);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbox.setText(null);
            }
        });

        searchbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchActivity.this.doSearch();
                    return true;
                }
                return false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.doSearch();
            }
        });

    }

    public void doSearch() {
        final SearchListAdapter adapter = new SearchListAdapter();
        String searching = searchbox.getText().toString();
        Call<List<Product>> search = networkService.products(searching);
        search.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> product_temp = response.body();
                    for (final Product product : product_temp) {
                        adapter.addItem(product.getId(), product.isFavorite(), product.getPhoto(), product.getName(), Integer.toString(product.getAvgPrice()));
                    }
                    list.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
    }
}