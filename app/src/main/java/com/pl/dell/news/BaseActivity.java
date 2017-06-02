package com.pl.dell.news;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pl.dell.news.network.NetworkHelper;

public class BaseActivity extends ActionBarActivity {

    public ProgressDialog progressDialog;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference baseRef = database.getReference();

    NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        progressDialog=new ProgressDialog(this);
        networkHelper = new NetworkHelper(this);
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
