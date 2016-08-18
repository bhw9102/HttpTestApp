package com.nolgong.httptestapp;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hyunwoo on 2016. 8. 18..
 */
public interface HttpTestService {

    @GET("/test")
    Call<JsonObject> test();
}
