package com.example.choikn.precious.utils;

import android.content.SharedPreferences;


import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.Request;

import java.io.IOException;
import java.util.HashSet;
import java.util.prefs.Preferences;

/**
 * This Interceptor add all received Cookies to the app DefaultPreferences.
 * Your implementation on how to save the Cookies on the Preferences MAY VARY.
 * <p>
 * Created by tsuharesu on 4/1/15.
 */
public class ReceivedCookiesInterceptor implements Interceptor {

    protected SharedPreferences preference;

    public ReceivedCookiesInterceptor(SharedPreferences preference) {
        this.preference = preference;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            preference.edit()
                    .putStringSet("cookies", cookies)
                    .apply();
        }

        return originalResponse;
    }
}