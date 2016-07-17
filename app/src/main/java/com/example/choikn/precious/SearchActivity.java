package com.example.choikn.precious;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChoiKN on 2016-06-28.
 */
public class SearchActivity extends Activity {

    private ImageButton delete,search;
    private EditText searchbox;
    private ActivityManager am = ActivityManager.getInstance();
    private static NetworkService networkService;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        am.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        delete = (ImageButton)findViewById(R.id.delete);
        search = (ImageButton)findViewById(R.id.search);
        searchbox = (EditText)findViewById(R.id.searchbox);

        list = (ListView)findViewById(R.id.search_list);
        final SearchListAdapter adapter = new SearchListAdapter();
        list.setAdapter(adapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbox.setText(null);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searching = searchbox.getText().toString();
                Call<List<Product>> search = networkService.products(searching);
                search.enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if(response.isSuccessful()){
                            List<Product> product_temp = response.body();
                            for(Product product : product_temp){
                                adapter.addItem(product.getName(), product.getPrice(), ResourcesCompat.getDrawable(getResources(), R.drawable.heart2, null));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {

                    }
                });
            }
        });

    }

}
