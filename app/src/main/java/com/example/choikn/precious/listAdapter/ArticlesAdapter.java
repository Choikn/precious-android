package com.example.choikn.precious.listAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.choikn.precious.R;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.server.Product;

import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesAdapter extends BaseAdapter {
    private static NetworkService networkService;
    private ArrayList<Articles_items> listViewItemList = new ArrayList<Articles_items>() ;

    // ListViewAdapter의 생성자
    public ArticlesAdapter() {

    }

    public void reset() {
        this.listViewItemList.clear();
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
            convertView = inflater.inflate(R.layout.articles_list, null);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView photo = (ImageView) convertView.findViewById(R.id.photo);
        TextView name = (TextView) convertView.findViewById(R.id.name) ;
        TextView price = (TextView) convertView.findViewById(R.id.price);
        ImageButton favorite = (ImageButton) convertView.findViewById(R.id.favorite);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Articles_items listViewItem = listViewItemList.get(position);

        favorite.setTag(listViewItem);

        // 아이템 내 각 위젯에 데이터 반영
        new DownloadImageTask(photo).execute(listViewItem.getPhoto());
        name.setText(listViewItem.getName());
        price.setText(listViewItem.getContent());

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
    public void addItem(int id, boolean favorite, String photo, String name, String content) {
        Articles_items item = new Articles_items();

        item.setpId(id);
        item.setFavorite(favorite);
        item.setPhoto(photo);
        item.setName(name);
        item.setContent(content + "원");

        listViewItemList.add(item);
    }
}
