package com.example.ytsag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.ytsag.Adapter.MovieItemAdapter;
import com.example.ytsag.Data.MovieItem;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<MovieItem>> , MovieItemAdapter.itemClickListener {

    private RecyclerView recyclerView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private static final int MOVIE_LIST = 0;

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "508900098e9a09f45c6a9843408a1fc1";
    private static String requestUrl;
    private MovieItemAdapter adapter;
    LoaderManager loaderManager;
    DrawerLayout drawerLayout;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref= this.getSharedPreferences("MY_SHARED_PREFERENCE", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_movie_list);
        recyclerView = findViewById(R.id.recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        List<MovieItem> list = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new MovieItemAdapter(list, this,  this);
        recyclerView.setAdapter(adapter);
        setSupportActionBar(toolbar);
        requestUrl = BASE_URL+"now_playing?api_key="+API_KEY;
        Log.v("message",requestUrl);

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1,null,this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                requestUrl ="";
                switch(menuItem.getItemId()){
                    case R.id.now_playing:
                       requestUrl = BASE_URL+"now_playing?api_key="+API_KEY;
                       loaderManager.restartLoader(1,null,MovieListActivity.this);
                       break;
                    case R.id.popular:
                        requestUrl = BASE_URL+"popular?api_key="+API_KEY;
                        loaderManager.restartLoader(1,null,MovieListActivity.this);
                        break;
                    case R.id.upcoming:
                        requestUrl = BASE_URL+"upcoming?api_key="+API_KEY;
                        loaderManager.restartLoader(1,null,MovieListActivity.this);
                        break;
                    case R.id.top_rated:
                        requestUrl = BASE_URL+"top_rated?api_key="+API_KEY;
                        loaderManager.restartLoader(1,null,MovieListActivity.this);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            drawerLayout.openDrawer(GravityCompat.START);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<MovieItem>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v("message", "loaderCreated");
        return new MovieLoader(this,requestUrl,MOVIE_LIST);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieItem>> loader, List<MovieItem> data) {
        adapter.updateUi(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieItem>> loader) {
        adapter.updateUi(new ArrayList<MovieItem>());
    }

    @Override
    public void onitemclick(int movie_id) {

        Intent movieDetails_Intent = new Intent(MovieListActivity.this , MovieDetailsActivity.class);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("KEY", movie_id);
        editor.commit();
        startActivity(movieDetails_Intent);
        Log.v("message","item clicked");
    }
}
