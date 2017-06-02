package com.pl.dell.news;


 import android.content.Intent;
 import android.support.v7.app.AppCompatActivity;
 import android.os.Bundle;

 public class Loading extends AppCompatActivity
 {

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_loading);

 Thread timer = new Thread(){
 public void run() {
 try {
 sleep(5000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }finally {
 Intent openStartingPoint = new Intent(Loading.this,MainActivity.class);
 startActivity(openStartingPoint);
 }
 }


 };
 timer.start();
 }

 @Override
 protected void onPause(){
 super.onPause();
 //finish();
 }
 }

