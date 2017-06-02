package com.pl.dell.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by DELL on 27-05-2017.
 */
public class DemoProgress {

    Activity activity;

    ProgressDialog progressDialog;

    public DemoProgress(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        this.activity=activity;
    }

    void showProgress(String title, String message){
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    void hideProgress(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

}
