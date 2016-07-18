package com.example.choikn.precious.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.choikn.precious.R;
import com.example.choikn.precious.listAdapter.SearchListAdapter;
import com.example.choikn.precious.listAdapter.SearchList_items;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.server.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChoiKN on 2016-07-17.
 */
public class FavoriteActivity extends Activity {

    private NetworkService networkService;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        networkService = AppController.getNetworkService(this);

        TextView ti = (TextView) findViewById(R.id.ti);
        ti.setText("관심품목");
        ti.setPaintFlags(ti.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Cocogoose_trial.otf");
        ti.setTypeface(face);

        final SearchListAdapter adapter = new SearchListAdapter();
        list = (ListView)findViewById(R.id.list);
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

        Call<List<Product>> favorite = networkService.favorite_products();
        favorite.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> product_temp = response.body();
                    for (final Product product : product_temp) {
                        adapter.addItem(product.getId(), true, product.getPhoto(), product.getName(), Integer.toString(product.getAvgPrice()));
                        list.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
    }
}
