package com.nolgong.httptestapp;

import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hyunwoo on 2016. 8. 18..
 */
public class NetworkClient {
    private static NetworkClient ourInstance = null;
    private Retrofit retrofit;
    private HttpTestService service;
    private String currentIp = "0000";

    private NetworkClient(String serverAddress) {
        Log.e("debug", "NetworkClient Constructor : " + serverAddress);
        currentIp = serverAddress;
        retrofit = new Retrofit.Builder()
                .baseUrl(serverAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(HttpTestService.class);
    }

    public static NetworkClient getInstance(String serverAddress) {
        ourInstance = new NetworkClient(serverAddress);
        return ourInstance;
    }

    public static NetworkClient getInstance(){
        return ourInstance;
    }

    public void test(Callback<JsonObject> cb){
        Call<JsonObject> call = service.test();
        call.enqueue(cb);
    }

    public String getCurrentIp() {
        return currentIp;
    }
}
