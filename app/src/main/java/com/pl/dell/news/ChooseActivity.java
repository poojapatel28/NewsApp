package com.pl.dell.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseActivity extends BaseActivity implements View.OnClickListener {
    FloatingActionButton button;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<NewsSource> list;
    ArrayList<String> data;
    DatabaseReference myRef;
    DbHelper dbHelper;

    FirebaseAuth mAuth;
    FirebaseUser usr;
    ArrayList<String> news = new ArrayList<>();
    ArrayList<String> selectedItems = new ArrayList<String>();
    ArrayList<String> result = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference();
        usr = mAuth.getCurrentUser();


        button = (FloatingActionButton) findViewById(R.id.submit);
        listView = (ListView) findViewById(R.id.sources);


        dbHelper = new DbHelper(this);
        list = new ArrayList<>();
        data = dbHelper.readAllSources();


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, data);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Toast.makeText(getApplicationContext(),getData().get(0),Toast.LENGTH_SHORT);
        if (!getIntent().hasExtra("main")) {

            showProgress("Please Wait", "Loading");
            getData();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //showProgress("Please Wait","Loading List");
                    freshCall();
                }
            });
        }


        listView.setAdapter(adapter);


        button.setOnClickListener(new View.OnClickListener()

                                  {
                                      public void onClick(View v) {
                                          selectedItems.clear();
                                          checkSelected();


                                          Intent intent = new Intent(getApplicationContext(),
                                                  MainActivity.class);

                                          startActivity(intent);


                                      }
                                  }

        );

    }

    public void fetchSource() {
        showProgress("Wait Loading", "Loading List");
        // adapter.notifyDataSetChanged();

        Log.d("MainActivity", "fetch method");
        String url = "https://newsapi.org/v1/sources?category=&language=en&country=";

        Log.d("MainActivity", "URL " + url);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MainActivity", "Response " + response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        // Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();


                        Log.d("Hi", "Done1");

                        JSONArray newsSource = jsonObject.optJSONArray("sources");

                        Log.d("MainActivity.this", "Total movies: " + newsSource.length());
                        for (int i = 0; i < newsSource.length(); i++) {

                            Log.d("Hi", "Done3");
                            NewsSource item = new NewsSource();
                            JSONObject currentRow = newsSource.optJSONObject(i);
                            String s_name = currentRow.optString("name");
                            item.setSourceName(s_name);
                            String id = currentRow.optString("id");
                            item.setId(id);


                            if (item.getId().equals("the-next-web")) {

                            } else { news.add(item.getId());
                                dbHelper.insertSources(item);
                            }

                        }

                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               data = dbHelper.readAllSources();
                               //adapter.notifyDataSetChanged();
                               adapter = new ArrayAdapter<String>(ChooseActivity.this,
                                       android.R.layout.simple_list_item_multiple_choice, data);
                               listView.setAdapter(adapter);
                               hideProgress();


                           }
                       });


                        Log.d("pooja", "data decoded");


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("MainActivity.this", "errrrorrr");
                        Log.d("Adapter", "err");
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        hideProgress();
        AppController.getInstance().addToRequestQueue(stringRequest, "req_fetch");
    }

    public void checkSelected() {

        SparseBooleanArray checked = listView.getCheckedItemPositions();
        selectedItems.clear();

        if (checked.size() == 0) {
            Toast.makeText(getApplicationContext(), "No Change", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < checked.size(); i++) {
                int pos = checked.keyAt(i);

                if (checked.valueAt(i)) {
                    selectedItems.add(adapter.getItem(pos));
                }

                UserPrefrence usrpref = new UserPrefrence(usr.getUid(), selectedItems);
                baseRef.child("user_pref").child(usr.getUid()).setValue(usrpref).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }


                });
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    public ArrayList<String> getData() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ArrayList<String> post = (ArrayList<String>) dataSnapshot.getValue();

                if (post != null) {
                    if (post.size() > 0) {

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        //hideProgress();
                        finish();
                        hideProgress();
                    } else {
                        hideProgress();
                        freshCall();
                    }
                } else {
                    hideProgress();
                    freshCall();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("loadPost:onCancelled", databaseError.toException().toString());

            }
        };
        baseRef.child("user_pref")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("mpref")
                .addValueEventListener(postListener);


        return result;

    }

    void freshCall() {
        if (dbHelper.readAllSources().size() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgress("Please Wait","Loading List");
                    fetchSource();
                }
            });

            //adapter.notifyDataSetChanged();
        }
    }
}


