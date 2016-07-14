package com.example.choikn.precious;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ChoiKN on 2016-06-26.
 */
public interface NetworkService {
    @POST("/api/session/local/register")
    Call<User> post_user(@Body User user);

    @FormUrlEncoded
    @POST("/api/session/local")
    Call<User> user_check(@Field("username")String username, @Field("password")String password);

    @FormUrlEncoded
    @POST("/api/session/facebook-token")
    Call<User> fb_user_check(@Field("access_token") String token);

    @GET("/api/products")
    Call<List<Product>> products(@Field("query")String query);

    @GET("/api/user")
    Call<User> getNameEmail();

    @DELETE("/api/session")
    Call<User> logout();
}
