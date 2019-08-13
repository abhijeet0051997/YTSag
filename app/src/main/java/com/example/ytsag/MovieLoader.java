package com.example.ytsag;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.ytsag.Data.MovieItem;
import com.example.ytsag.NetworkUtils.QueryUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MovieLoader extends AsyncTaskLoader<List<MovieItem>> {
    String mUrl;
    int mode;
    String requestUrl;
    Context context;

    public static final String API_KEY = "508900098e9a09f45c6a9843408a1fc1";
    private static final int MOVIE_DETAILS = 2;
    private static final int VIDEO_KEY = 3;
    private static final int CAST_CREW = 4;


    public MovieLoader(Context context, String baseUrl, int mode) {
        super(context);
        this.context = context;
        mUrl = baseUrl;
        this.mode = mode;
        Log.v("message", "loaderStarted");

    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<MovieItem> loadInBackground() {

        if(mUrl == null)
            return null;
        Log.v("message", "loadeinBack");



        List<MovieItem> movies = QueryUtils.fetchMovies(getRequestUrl(),mode);
        Log.v("details_url",mUrl);
        return movies;
    }

    private String getRequestUrl() {
        SharedPreferences sharedPref = context.getSharedPreferences("MY_SHARED_PREFERENCE", Context.MODE_PRIVATE);
        int movie_id = sharedPref.getInt("KEY", 0);

        switch (mode) {
            case MOVIE_DETAILS:
                requestUrl = mUrl + movie_id + "?api_key=" + API_KEY;
                return requestUrl;
            case VIDEO_KEY:
                requestUrl = mUrl + movie_id + "/videos?api_key=" + API_KEY;
                return requestUrl;
            case CAST_CREW:
                requestUrl = mUrl+ movie_id + "/credits?api_key=" + API_KEY;
                return requestUrl;
            default:
                return mUrl;
        }

    }
}
