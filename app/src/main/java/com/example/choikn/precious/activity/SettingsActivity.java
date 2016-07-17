package com.example.choikn.precious.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.choikn.precious.ActivityManager;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.R;
import com.example.choikn.precious.server.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends Activity {

    private ListView list, list2;
    private static NetworkService networkService;
    private ImageButton menu, search;
    private TextView name, email;
    private DrawerLayout dlMain;
    private LinearLayout layoutDrawer;
    private ActivityManager am = ActivityManager.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        am.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        layoutDrawer = (LinearLayout) findViewById(R.id.layoutDrawer);
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

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

        list = (ListView) findViewById(R.id.list2);
        final ArrayList<String> items = new ArrayList<>();
        items.add("1");
        items.add("2");
        items.add("3");

        list2 = (ListView) findViewById(R.id.list);
        final ArrayList<String> items2 = new ArrayList<>();
        items2.add("4");
        items2.add("5");
        items2.add("6");
        items2.add("7");

        networkService = AppController.getNetworkService(this);

        CustomAdapter adapter = new CustomAdapter(this, 0, items);
        CustomAdapter adapter2 = new CustomAdapter(this, 0, items2);
        list.setAdapter(adapter);
        list2.setAdapter(adapter2);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    final Intent act = new Intent(getApplicationContext(), MainActivity.class);
                    Call<User> Logout = networkService.logout();
                    Logout.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                am.finishAllActivity();
                                startActivity(act);
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
                imageView.setBackgroundResource(R.drawable.b_1);
            else if ("2".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.b_2);
            else if ("3".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.b_3);
            else if ("4".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_1);
            else if ("5".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_2);
            else if ("6".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_3);
            else if ("7".equals(items.get(position)))
                imageView.setBackgroundResource(R.drawable.a_4);

            return v;
        }
    }

}
