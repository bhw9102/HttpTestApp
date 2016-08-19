package com.nolgong.httptestapp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hyunwoo on 2016. 8. 18..
 */
public class Timer extends Thread {
    private static Timer ourInstance = new Timer();
    private static Handler handler;

    public static final int SEND_REFRESH_VIEW = 0;
    private int ONE_SECOND_BY_MILLISECOND = 1000;
    private int ONE_SECOND = 1;
    private int ONE_COUNT = 1;

    private boolean isThreading = false;
    private boolean isCount = false;
    private long elapsedTime = 0;


    private int requestCount = 0;
    private int responseCount = 0;
    private int failCount = 0;

    public int getResponseCount() {
        return responseCount;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public static Timer getInstance(Handler instanceHandler){
        handler = instanceHandler;
        return ourInstance;
    }

    public static Timer getInstance() {
        return ourInstance;
    }
    private Timer() {}

    public boolean isCount() {
        return isCount;
    }

    public void stopTimer() {
        this.isCount = false;
    }
    public void runTimer() {
        this.isCount = true;
    }

    @Override
    public synchronized void start() {
        super.start();
        this.isThreading = true;
    }
    @Override
    public void run(){
        while(this.isThreading) {
            while (this.isCount) {
                Log.v("test", "elapse time : " + elapsedTime);
                elapsedTime = elapsedTime + ONE_SECOND;
                testRequest();
                requestCount = requestCount + ONE_COUNT;

                Message msg = new Message();
                msg.what = SEND_REFRESH_VIEW;

                handler.sendMessage(msg);
                try {
                    Thread.sleep(ONE_SECOND_BY_MILLISECOND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void testRequest(){
        NetworkClient.getInstance().test(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                switch (response.code()){
                    case 200:
                        Log.v("test", response.body().get("result").toString());
                        responseCount = responseCount + ONE_COUNT;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("debug", "test fail");
                failCount = failCount + ONE_COUNT;
            }
        });
    }
}
