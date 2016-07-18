package com.example.choikn.precious.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choikn.precious.R;
import com.example.choikn.precious.server.AppController;
import com.example.choikn.precious.server.Article;
import com.example.choikn.precious.server.NetworkService;
import com.example.choikn.precious.server.Product;
import com.example.choikn.precious.server.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostWriteActivity extends Activity {

    private EditText title, price, content;
    private ImageView picture;
    private ImageButton jaksung;
    private File imageFile;
    private static NetworkService networkService;
    private static final int PICK_FROM_GALLERY = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postwrite);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(255, 168, 0));
        }

        networkService = AppController.getNetworkService(this);

        TextView ti = (TextView) findViewById(R.id.ti);
        ti.setText("게시글 작성");
        ti.setPaintFlags(ti.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Medium.otf");
        ti.setTypeface(face);

        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        content = (EditText) findViewById(R.id.content);

        picture = (ImageView) findViewById(R.id.picture);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Gallery 호출
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                try {
                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_GALLERY);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });

        jaksung = (ImageButton) findViewById(R.id.jaksung);

        jaksung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = getIntent();
                int id = intent2.getExtras().getInt("id");
                String etitle = title.getText().toString();
                String econtent = content.getText().toString();
                int eprice;
                try {
                     eprice = Integer.parseInt(price.getText().toString());
                } catch (Exception e) {
                    return;
                }
                MultipartBody.Part body = null;
                if (imageFile != null) {
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
                    body = MultipartBody.Part.createFormData("photos", imageFile.getName(), requestFile);
                }
                RequestBody title = RequestBody.create(MediaType.parse("text/plain"), etitle);

                RequestBody content = RequestBody.create(MediaType.parse("text/plain"), econtent);
                Call<Article> write = networkService.write(id, title, eprice, content, body);
                write.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        if (response.isSuccessful()) {
                            finish();
                        } else {
                            try {
                                Log.e("myTag", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            int statusCode = response.code();
                            Log.i("MyTag", "응답코드 : " + statusCode);
                        }
                    }
                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {
                        Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                    }
                });
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == PICK_FROM_GALLERY) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Uri selectedImageURI = data.getData();
                imageFile = new File(getPath(selectedImageURI));
                try {
                    FileInputStream stream = new FileInputStream(imageFile);
                    Bitmap photo = BitmapFactory.decodeStream(stream);
                    picture.setImageBitmap(photo);
                } catch (Exception e) {
                    Log.e("MyTag", e.getMessage());
                }
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


}
