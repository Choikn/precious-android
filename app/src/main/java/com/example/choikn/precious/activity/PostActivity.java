package com.example.choikn.precious.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.choikn.precious.R;

import org.w3c.dom.Text;

/**
 * Created by ChoiKN on 2016-07-17.
 */
public class PostActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        TextView ti = (TextView)findViewById(R.id.ti);
        ti.setText("게시글");
        ti.setPaintFlags(ti.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Cocogoose_trial.otf");
        ti.setTypeface(face);

        TabHost tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();

        // Tab1 Setting
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Tab1");
        tabSpec1.setIndicator("관심게시물"); // Tab Subject
        tabSpec1.setContent(R.id.tab_view1); // Tab Content
        tabHost.addTab(tabSpec1);

        // Tab2 Setting
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Tab2");
        tabSpec2.setIndicator("내 게시물"); // Tab Subject
        tabSpec2.setContent(R.id.tab_view2); // Tab Content
        tabHost.addTab(tabSpec2);

        //for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
        //    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFC107"));
        //}

        // show First Tab Content
        tabHost.setCurrentTab(0);
    }

}
