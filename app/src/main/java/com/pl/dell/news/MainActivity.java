package com.pl.dell.news;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class MainActivity extends BaseActivity implements ItemClickListener {

    Toolbar t;
    ArrayList<NewsModel> arrayList = new ArrayList<>();
    RecyclerView mRecyclerview;

    DbHelper dbHelper;
    TextView user, mail;
    ImageView pic;
    NavigationView navigationView;
    TextView all;
    String[] selectedSource;

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;

    RecyclerView.LayoutManager mLayoutManager;
    NewsAdapter adapter;
    ProgressDialog prog;
    ArrayList<NewsSource> news = new ArrayList<>();

    String source;
    String type;


    ListView lv;
    TextView log;
    TextView setting;


    ListView s_list;
    private FirebaseAuth mAuth;
    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = new Toolbar(this);
        t.setTitle("News");

        type = "top";
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        navigationView = (NavigationView) findViewById(R.id.navigation);


        user = (TextView) findViewById(R.id.name);
        mail = (TextView) findViewById(R.id.email);
        pic = (ImageView) findViewById(R.id.imageView);
        prog = new ProgressDialog(MainActivity.this);
        log = (TextView) findViewById(R.id.log);
        lv = (ListView) findViewById(R.id.listview);
        setting = (TextView) findViewById(R.id.setting);
        all = (TextView) findViewById(R.id.all);
        u = new User();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(t);


        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, t,
                R.string.open, R.string.close
        );
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionBarDrawerToggle.syncState();


        user = (TextView) findViewById(R.id.name);
        user.setText(currentUser.getDisplayName());


        mail = (TextView) findViewById(R.id.email);
        mail.setText(currentUser.getEmail());

        pic = (ImageView) findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).load(currentUser.getPhotoUrl()).into(pic);
        Bundle b = getIntent().getExtras();
        selectedSource = b.getStringArray("selectedItems");

        source = selectedSource[0];


        progressDialog = new ProgressDialog(this);
        t.setTitle("News");


        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerview.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager((getApplicationContext()));
        mRecyclerview.setLayoutManager(mLayoutManager);
        adapter = new NewsAdapter(arrayList, getApplicationContext(), MainActivity.this);
        mRecyclerview.setAdapter(adapter);
        adapter.setItemClickListner(this);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());

        dbHelper = new DbHelper(getApplicationContext());
        s_list = (ListView) findViewById(R.id.s_list);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, selectedSource);

        adapter.notifyDataSetChanged();

        s_list.setAdapter(adapter);
        s_list.setOnItemClickListener(new DrawerItemClickListener());



        onResume();
        fetchSource();
        fetchMethod();





        Log.d("pooja", "adapter set");


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();


                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("LogOut")
                        .setMessage("Are you sure you want to Log Out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                LoginManager.getInstance().logOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                                drawerLayout.closeDrawers();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();


            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, ChooseActivity.class));

            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                for (int i = 0; i < selectedSource.length; i++) {
                    source = selectedSource[i];
                    fetchMethod();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);

                    }
                }).setNegativeButton("No", null).show();
    }

    public void fetchMethod() {


        arrayList.clear();
        // adapter.notifyDataSetChanged();

        showProgress("please wait", "loading");

        Log.d("MainActivity", "fetch method");
        String url = "https://newsapi.org/v1/articles?source=" + source + "&sortBy=" + type + "&apiKey=e0d3a70e42de4bb58495637f504e7fa4";

        Log.d("MainActivity", "URL " + url);

        try {

            OkHttpClient client = new OkHttpClient();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .build();

            okhttp3.Response response = client.newCall(request).execute();
            decodeNews(response.body().string());

        } catch (Exception e) {
            e.printStackTrace();
            hideProgress();
        }
    }




    private void decodeNews(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);

        String status = jsonObject.getString("status");

        if (status.equals("ok")) {
            // Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();


            Log.d("Hi", "Done1");

            JSONArray newsArray = jsonObject.optJSONArray("articles");

            Log.d("MainActivity.this", "Total movies: " + newsArray.length());
            for (int i = 0; i < newsArray.length(); i++) {

                Log.d("Hi", "Done3");
                NewsModel item = new NewsModel();
                JSONObject currentRow = newsArray.optJSONObject(i);
                String author = currentRow.optString("author");
                item.setA_name(author);
                String image = currentRow.optString("urlToImage");
                item.setImage(image);
                String title = currentRow.optString("title");
                item.setTitle(title);
                String url = currentRow.optString("url");
                item.setUrl(url);
                String publishedAt = currentRow.optString("publishedAt");
                String token[] = publishedAt.replace("Z", " ").replace("T", " ").split(" ");
                item.setDate(token[0]);
                item.setTime(token[1]);
                Log.d("main acti", token[1]);
                String des = currentRow.optString("description");
                item.setDescription(des);

                arrayList.add(item);


                //   Toast.makeText(getApplicationContext(), " " + author , Toast.LENGTH_SHORT).show();
            }


            mRecyclerview.getRecycledViewPool().clear();

            adapter.notifyDataSetChanged();


            Log.d("pooja", "data decoded");

        }

        hideProgress();

        Log.d("pooja", "data in adapter");

    }




    @Override
    public void onItemClick(View v, int pos) {
        NewsModel model = arrayList.get(pos);
        String url = model.getUrl();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.string.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
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


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {


        source = selectedSource[position];
        fetchMethod();
        drawerLayout.closeDrawers();

    }


}





