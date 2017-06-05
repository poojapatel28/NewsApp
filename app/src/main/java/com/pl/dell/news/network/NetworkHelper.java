package com.pl.dell.news.network;

import android.app.Activity;
import android.widget.Toast;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by DELL on 02-06-2017.
 */
public class NetworkHelper {
    OkHttpClient client = new OkHttpClient();
    Activity activity;

    public NetworkHelper(Activity activity) {
        this.activity = activity;
    }

    public void nGet(String url, final OnComplete complete) throws IOException {
        OkHttpClient client = new OkHttpClient();
        getOkhttp(client, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                complete.onSuccess("",false);
            }
            String r;

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                r=response.body().string();
                try {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            complete.onSuccess(r, response.code() == 200);
                        }
                    });
                }
                    catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(activity,"no content for this source type",Toast.LENGTH_SHORT).show();
                        complete.onSuccess("", false);
                    }
                }
        });

    }

    private Call postOkhttp(OkHttpClient client, String url, RequestBody formBody, Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    private Call getOkhttp(OkHttpClient client, String url, Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    public interface OnComplete{
        void onSuccess(String res,Boolean succ);
    }
}
