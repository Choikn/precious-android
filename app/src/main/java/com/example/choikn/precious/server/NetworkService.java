package com.example.choikn.precious.server;

import java.io.File;
import java.util.List;

import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ChoiKN on 2016-06-26.
 */
public interface NetworkService {
    @Multipart
    @PUT("/api/user/photo")
    Call<User> updatePhoto(@Part("photo") RequestBody photo);

    @POST("/api/session/local/register")
    Call<User> post_user(@Body User user);

    @POST("/api/products/{id}/favorite")
    Call<Product> favorite(@Path("id")Number id);

    @FormUrlEncoded
    @POST("/api/products/{id}/articles")
    Call<Article> write(

    @FormUrlEncoded
    @POST("/api/session/local")
    Call<User> user_check(@Field("username")String username, @Field("password")String password);

    @FormUrlEncoded
    @POST("/api/session/facebook-token")
    Call<User> fb_user_check(@Field("access_token") String token);

    @GET("/api/products")
    Call<List<Product>> products(@Query("query") String query);

    @GET("/api/user/favoriteProducts")
    Call<List<Product>> favorite_products();

    @GET("/api/products/{id}")
    Call<Product> productID(@Path("id")Number id);

    @GET("/api/user")
    Call<User> getNameEmail();

    @FormUrlEncoded
    @POST("/api/user")
    Call<User> setGCMKey(@Field("gcm") String token);

    @DELETE("/api/products/{id}/favorite")
    Call<Product> favorite_dt(@Path("id")Number id);

    @DELETE("/api/session")
    Call<User> logout();

    @GET("/api/notifications")
    Call<List<Notification>> getNotifications();
}
