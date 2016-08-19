package com.nolgong.httptestapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

public class HttpTestActivity extends AppCompatActivity implements Handler.Callback{

    private Handler handler;
    private Timer timer;
    public Button startButton;
    public Button stopButton;
    public TextView responseView;
    public TextView requestView;
    public TextView percentView;
    public Button addressSetButton;
    public EditText addressEdit;
    public TextView failView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);
        this.handler = new Handler(this);
        timer = Timer.getInstance(handler);
        timer.start();
        setNetworkClient();

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTest();
            }
        });
        stopButton = (Button) findViewById(R.id.endButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTest();
            }
        });
        addressSetButton = (Button) findViewById(R.id.addressEditButton);
        addressSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddress();
            }
        });
        addressEdit = (EditText) findViewById(R.id.addressEdit);
        responseView = (TextView) findViewById(R.id.response);
        requestView = (TextView) findViewById(R.id.request);
        percentView = (TextView) findViewById(R.id.httpPercent);
        failView = (TextView) findViewById(R.id.fail);
    }


    private void startTest(){
        if(!timer.isCount()){
            timer.runTimer();
        }
    }

    private void stopTest(){
        if(timer.isCount()){
            timer.stopTimer();
        }
    }

    private void setAddress(){
        String serverAddress = "http://";
        serverAddress = serverAddress + addressEdit.getText();
        setNetworkClient(serverAddress);
    }
    private void setNetworkClient(String serverAddress){
        Log.e("debug", "Address : " + serverAddress);
        NetworkClient.getInstance(serverAddress);
    }
    private void setNetworkClient(){
        String serverAddress = "";
        try {
            serverAddress = getProperty();
        } catch (IOException e){
            Log.e("debug", "set network" + e);
        }
        NetworkClient.getInstance(serverAddress);
    }

    private String getProperty() throws IOException {
        Properties properties = new Properties();
        properties.load(getResources().openRawResource(R.raw.config));
        String property = properties.getProperty("serverAddress");
        return property;
    }
    @Override
    public boolean handleMessage(Message msg){
        if(msg.what==timer.SEND_REFRESH_VIEW){
            int requestCount = timer.getRequestCount();
            int responseCount = timer.getResponseCount();
            double requestDouble = new Double(requestCount);
            double responseDouble = new Double(responseCount);
            double percent = (responseDouble/requestDouble) * 100;
            requestView.setText(String.valueOf(requestCount));
            responseView.setText(String.valueOf(responseCount));
            percentView.setText(String.valueOf(Math.round(percent)));
            failView.setText(String.valueOf(timer.getFailCount()));
        }
        return false;
    }
}
