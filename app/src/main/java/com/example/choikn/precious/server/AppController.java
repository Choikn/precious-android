package com.example.choikn.precious.server;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.choikn.precious.utils.AddCookiesInterceptor;
import com.example.choikn.precious.utils.ReceivedCookiesInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController {

    private static NetworkService networkService;
    public static NetworkService getNetworkService(Context context) {
        AppController.buildNetworkService(context);
        return networkService;
    }

    public static void buildNetworkService(Context context){
        synchronized (AppController.class){
            if(networkService == null){
                SharedPreferences preferences = context.getSharedPreferences("precious_cookies", Context.MODE_PRIVATE);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(new AddCookiesInterceptor(preferences))
                        .addInterceptor(new ReceivedCookiesInterceptor(preferences))
                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://precious.kkiro.kr")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                networkService = retrofit.create(NetworkService.class);
            }
        }
    }
}
