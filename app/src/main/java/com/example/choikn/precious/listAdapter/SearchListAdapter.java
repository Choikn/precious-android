package com.example.choikn.precious.listAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choikn.precious.R;
import com.example.choikn.precious.activity.SearchActivity;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.server.Product;
import com.example.choikn.precious.utils.AddCookiesInterceptor;
import com.example.choikn.precious.utils.ReceivedCookiesInterceptor;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.choikn.precious.server.AppController.*;

/**
 * Created by ChoiKN on 2016-07-14.
 */
public class SearchListAdapter extends BaseAdapter{
    private static NetworkService networkService;
    private ArrayList<SearchList_items> listViewItemList = new ArrayList<SearchList_items>() ;

    // ListViewAdapter의 생성자
    public SearchListAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_list, null);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView photo = (ImageView) convertView.findViewById(R.id.photo);
        TextView name = (TextView) convertView.findViewById(R.id.name) ;
        TextView price = (TextView) convertView.findViewById(R.id.price);
        final ImageButton favorite = (ImageButton) convertView.findViewById(R.id.favorite);


        favorite.setFocusable(false);

        networkService = AppController.getNetworkService(context);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SearchList_items obj = (SearchList_items) v.getTag();
                if (obj.isFavorite() == false) {
                    Call<Product> product = networkService.favorite(obj.getpId());
                    product.enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            if (response.isSuccessful()) {
                                favorite.setImageResource(R.drawable.heart);
                                obj.setFavorite(true);
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
            }else{
                    Call<Product> product2 = networkService.favorite_dt(obj.getpId());
                    product2.enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            if (response.isSuccessful()) {
                                favorite.setImageResource(R.drawable.heart2);
                                obj.setFavorite(false);
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
                }
            }
        });


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SearchList_items listViewItem = listViewItemList.get(position);

        favorite.setTag(listViewItem);

        // 아이템 내 각 위젯에 데이터 반영
        new DownloadImageTask(photo).execute(listViewItem.getPhoto());
        name.setText(listViewItem.getTitleStr());
        price.setText(listViewItem.getDescStr());
        favorite.setImageResource(listViewItem.isFavorite() ? R.drawable.heart : R.drawable.heart2);

        return convertView;
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

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int id, boolean favorite, String photo, String title, String desc) {
        SearchList_items item = new SearchList_items();

        item.setFavorite(favorite);
        item.setpId(id);
        item.setPhoto(photo);
        item.setTitleStr(title);
        item.setDescStr(desc + "원");

        listViewItemList.add(item);
    }
}
