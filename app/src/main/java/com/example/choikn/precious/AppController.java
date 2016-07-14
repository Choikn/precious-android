package com.example.choikn.precious;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Network;

import com.example.choikn.precious.utils.AddCookiesInterceptor;
import com.example.choikn.precious.utils.ReceivedCookiesInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
