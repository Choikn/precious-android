package com.example.choikn.precious.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
<<<<<<< HEAD
=======
import android.util.Log;
import android.widget.ListView;
>>>>>>> origin/test
import android.widget.TabHost;
import android.widget.TextView;

import com.example.choikn.precious.R;
<<<<<<< HEAD

import org.w3c.dom.Text;

=======
import com.example.choikn.precious.listAdapter.ArticlesAdapter;
import com.example.choikn.precious.listAdapter.SearchListAdapter;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.Article;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.server.Product;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

>>>>>>> origin/test
/**
 * Created by ChoiKN on 2016-07-17.
 */
public class PostActivity extends Activity {

<<<<<<< HEAD
=======
    private NetworkService networkService;
    private ListView fv_post, my_post;

>>>>>>> origin/test
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

<<<<<<< HEAD
=======
        networkService = AppController.getNetworkService(this);

>>>>>>> origin/test
        TextView ti = (TextView)findViewById(R.id.ti);
        ti.setText("게시글");
        ti.setPaintFlags(ti.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Cocogoose_trial.otf");
        ti.setTypeface(face);

        TabHost tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();

<<<<<<< HEAD
=======
        final ArticlesAdapter adapter = new ArticlesAdapter();

>>>>>>> origin/test
        // Tab1 Setting
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Tab1");
        tabSpec1.setIndicator("관심게시물"); // Tab Subject
        tabSpec1.setContent(R.id.tab_view1); // Tab Content
        tabHost.addTab(tabSpec1);

<<<<<<< HEAD
=======
        fv_post = (ListView)findViewById(R.id.fv_post);
        final Call<List<Article>> favorite_articles = networkService.favorite_articles();
        favorite_articles.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    List<Article> articles_temp = response.body();
                    for (final Article article : articles_temp) {
                        adapter.addItem(article.getId(), true, article.getPhoto(), article.getName(), Integer.toString(article.getPrice()));
                        fv_post.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });

>>>>>>> origin/test
        // Tab2 Setting
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Tab2");
        tabSpec2.setIndicator("내 게시물"); // Tab Subject
        tabSpec2.setContent(R.id.tab_view2); // Tab Content
        tabHost.addTab(tabSpec2);
<<<<<<< HEAD
=======
        my_post = (ListView)findViewById(R.id.my_post);
>>>>>>> origin/test

        //for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
        //    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFC107"));
        //}

        // show First Tab Content
        tabHost.setCurrentTab(0);
    }

}
