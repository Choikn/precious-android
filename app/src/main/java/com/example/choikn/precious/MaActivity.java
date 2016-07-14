package com.example.choikn.precious;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.graphview.LineGraphView;
import com.handstudio.android.hzgrapherlib.vo.GraphNameBox;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraph;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraphVO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaActivity extends Activity {

    private ViewGroup layoutGraphView;
    private TextView name, email;
    private ImageButton menu, search;
    private DrawerLayout dlMain;
    private LinearLayout layoutDrawer;
    private static NetworkService networkService;
    private ListView list;
    private ActivityManager am = ActivityManager.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        am.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

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
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

        //list
        list = (ListView) findViewById(R.id.list);
        final ArrayList<String> items = new ArrayList<>();
        items.add("1");
        items.add("2");
        items.add("3");
        items.add("4");

        CustomAdapter adapter = new CustomAdapter(this, 0, items);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 3){
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });

        layoutGraphView = (ViewGroup) findViewById(R.id.layoutGraphView);

        setLineGraph();

        networkService = AppController.getNetworkService(this);

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

    private void setLineGraph() {
        //all setting
        LineGraphVO vo = makeLineGraphAllSetting();

        //default setting
//		LineGraphVO vo = makeLineGraphDefaultSetting();

        layoutGraphView.addView(new LineGraphView(this, vo));
    }

    /**
     * make simple line graph
     *
     * @return
     */


    private LineGraphVO makeLineGraphAllSetting() {
        //BASIC LAYOUT SETTING
        //padding
        int paddingBottom = LineGraphVO.DEFAULT_PADDING;
        int paddingTop = LineGraphVO.DEFAULT_PADDING;
        int paddingLeft = LineGraphVO.DEFAULT_PADDING;
        int paddingRight = LineGraphVO.DEFAULT_PADDING;

        //graph margin
        int marginTop = LineGraphVO.DEFAULT_MARGIN_TOP;
        int marginRight = LineGraphVO.DEFAULT_MARGIN_RIGHT;

        //max value
        int maxValue = LineGraphVO.DEFAULT_MAX_VALUE;

        //increment
        int increment = LineGraphVO.DEFAULT_INCREMENT;

        //GRAPH SETTING
        String[] legendArr = {"1", "2", "3", "4", "5"};
        float[] graph1 = {500, 100, 300, 200, 100};

        List<LineGraph> arrGraph = new ArrayList<LineGraph>();

        arrGraph.add(new LineGraph("android", R.drawable.line_graph, graph1, R.drawable.one));
        LineGraphVO vo = new LineGraphVO(
                paddingBottom, paddingTop, paddingLeft, paddingRight,
                marginTop, marginRight, maxValue, increment, legendArr, arrGraph, R.drawable.bigcard);

//		vo.setDrawRegion(true);

        //use icon
//		arrGraph.add(new Graph(0xaa66ff33, graph1, R.drawable.icon1));
//		arrGraph.add(new Graph(0xaa00ffff, graph2, R.drawable.icon2));
//		arrGraph.add(new Graph(0xaaff0066, graph3, R.drawable.icon3));

//		LineGraphVO vo = new LineGraphVO(
//				paddingBottom, paddingTop, paddingLeft, paddingRight,
//				marginTop, marginRight, maxValue, increment, legendArr, arrGraph, R.drawable.bg);
        return vo;
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
                imageView.setBackgroundResource(R.drawable.a_2);
            else if ("3".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_3);
            else if ("4".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_4);

            return v;
        }

    }


}

