package com.pl.dell.news;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 30-05-2017.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="SOURCE_DB";

    private static final int DATABASE_VERSION=1;

    private static final String TABLE_NAME="sources";
    private static final String KEY_ID="id";
    private static final String KEY_name="name";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_TABLE_MESSAGES=" CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ( "+ KEY_ID +" VARCHAR(50) , " + KEY_name + " TEXT  " + " ) ";
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_MESSAGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);

    }

    public void insertSources(NewsSource newsSource) {
        SQLiteDatabase db = this.getWritableDatabase();


                ContentValues values = new ContentValues();
                values.put(KEY_ID, newsSource.getId());
                values.put(KEY_name, newsSource.getSourceName());
                db.insert(TABLE_NAME, null, values);

                Log.d("DBHelper", "Insert Messages Query Executed");


    }

    public ArrayList<String> readAllSources(){

        ArrayList<String> temp = new ArrayList<>();

        String selectQuery=" SELECT * FROM "+ TABLE_NAME;

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor c=db.rawQuery(selectQuery,null);
        if(c.moveToFirst()){
            do{
                NewsSource newsSource=new NewsSource();
                newsSource.setId(c.getString(c.getColumnIndex(KEY_ID)));
                newsSource.setSourceName(c.getString(c.getColumnIndex(KEY_name)));


                temp.add(newsSource.getId());

            }
            while(c.moveToNext());



        }


        Log.d("DBHelper","Fetching message size: "+temp.size());
        return temp;

    }






}
