package com.pl.dell.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseActivity extends BaseActivity implements View.OnClickListener {
    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<NewsSource> list;
    DatabaseReference myRef;
    DbHelper dbHelper;
    boolean[] check;
    int count = 0;
    boolean click = false;
    ArrayList<NewsSource> news = new ArrayList<>();
    ArrayList<String> selectedItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();



        button = (Button) findViewById(R.id.submit);
        listView = (ListView) findViewById(R.id.sources);
        fetchSource();
        list = new ArrayList<>();
        dbHelper = new DbHelper(this);

        check = new boolean[selectedItems.size()];
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, dbHelper.readAllSources());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        check = new boolean[selectedItems.size()];



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkSelected();

                if (selectedItems.size() <= 0) {
                    new AlertDialog.Builder(
                            ChooseActivity.this)

                            // Setting Dialog Title
                            .setTitle("Alert Dialog")

                            // Setting Dialog Message
                            .setMessage("Please select")

                            // Setting Icon to Dialog
                            // alertDialog.setIcon(R.drawable.tick);
                            .setPositiveButton("ok", null)
                            .show();

                } else {
                    String[] outputStrArr = new String[selectedItems.size()];

                    for (int k = 0; k < selectedItems.size(); k++) {
                        for (int j = 0; j < selectedItems.size(); j++) {
                            outputStrArr[j] = selectedItems.get(k);
                        }
                    }
                    int j = 0;
                    int k = 0;
                    int size = selectedItems.size();
                    while (size > 0) {
                        outputStrArr[j] = selectedItems.get(k);
                        j++;
                        k++;
                        size--;
                    }

                    Intent intent = new Intent(getApplicationContext(),
                            MainActivity.class);

                    Bundle b = new Bundle();
                    b.putStringArray("selectedItems", outputStrArr);
                    intent.putExtras(b);
                    startActivity(intent);


                }
            }
        });




    }

      public void fetchSource() {
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

                            news.add(item);

                            dbHelper.insertSources(item);
                        }
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
        AppController.getInstance().addToRequestQueue(stringRequest, "req_fetch");

    }



    public void checkSelected() {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        selectedItems.clear();


        for (int i = 0; i < checked.size(); i++)

        {
            int pos = checked.keyAt(i);

            if (checked.valueAt(i)) {

                selectedItems.add(adapter.getItem(pos));

            }
        }


    }

    @Override
    public void onClick(View v) {

    }
}


