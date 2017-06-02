package com.pl.dell.news;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends ActionBarActivity {

    public ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        progressDialog=new ProgressDialog(this);
    }

       public  void showProgress(String title, String message){
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public  void showProgress(){
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public void hideProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog.isShowing())
                progressDialog.dismiss();
            }
        });
    }



}
