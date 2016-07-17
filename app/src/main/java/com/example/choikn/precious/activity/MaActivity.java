package com.example.choikn.precious.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.example.choikn.precious.ActivityManager;
import com.example.choikn.precious.BackPressCloseSystem;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.R;
import com.example.choikn.precious.server.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class MaActivity extends Activity {

    private CircleImageView profile;
    private TextView name, email;
    private ImageButton menu, search;
    private DrawerLayout dlMain;
    private LinearLayout layoutDrawer;
    private static NetworkService networkService;
    private ListView list;
    private ActivityManager am = ActivityManager.getInstance();
    private boolean isOpen = false;
    private BackPressCloseSystem backPressCloseSystem;
    private static final int PICK_FROM_GALLERY = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        am.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        backPressCloseSystem = new BackPressCloseSystem(this);

        layoutDrawer = (LinearLayout) findViewById(R.id.layoutDrawer);
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);

        menu = (ImageButton) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlMain.openDrawer(layoutDrawer);
                isOpen = true;
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

        profile = (CircleImageView) findViewById(R.id.profile_image);
        registerForContextMenu(profile);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

        //list
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
                if(position == 0){
                    dlMain.closeDrawer(layoutDrawer);
                    Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                    startActivity(intent);
                }
                if(position == 1){
                    dlMain.closeDrawer(layoutDrawer);
                    Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                    startActivity(intent);
                }
                if(position == 2){
                    dlMain.closeDrawer(layoutDrawer);
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Gallery 호출
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // 잘라내기 셋팅
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);
                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_GALLERY);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == PICK_FROM_GALLERY) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                profile.setImageBitmap(photo);
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(isOpen == true)
        {
            dlMain.closeDrawer(layoutDrawer);
            isOpen = false;
        }
        else
        {
            backPressCloseSystem.onBackPressed();
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

