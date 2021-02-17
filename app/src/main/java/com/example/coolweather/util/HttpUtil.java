package com.example.coolweather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    public static void sendHttpRequest(final String address,final okhttp3.Callback callback){

    }
    public static void sendOkHttpRequest(final String address, final okhttp3.Callback callback){

        Log.d("TAG", "sendOkHttpRequest: address = "+address);
//        Log.d("TAG", "run: address = "+address);

        OkHttpClient client = new OkHttpClient() ;
        Request request = new Request.Builder().url(address).build() ;
        client.newCall(request).enqueue(callback);

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String a = "http://10.0.2.2/get_data.xml" ;
                OkHttpClient client = new OkHttpClient() ;
                Request request = new Request.Builder().url(address).build() ;
                Response response = null ;
                try {
                    response = client.newCall(request).execute() ;
                    String responseData = response.body().string() ;
                    Log.d("TAG", "sendOkHttpRequest:response "+responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start() ;
        */

        Log.d("TAG", "sendOkHttpRequest: end ....");
        /*
        */
    }


}
