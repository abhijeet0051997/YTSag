package com.example.ytsag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ytsag.Adapter.GenreAdapter;
import com.example.ytsag.Adapter.MovieItemAdapter;
import com.example.ytsag.Data.MovieItem;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<MovieItem>>,
        MovieItemAdapter.itemClickListener{

    RecyclerView similarMovies,genreRecycler;
    androidx.appcompat.widget.Toolbar toolbar;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView mMovieTitleView;
    TextView mMovieDetails,mMovieDuration;
    LinearLayout myLinearLayout;
    LoaderManager loaderManager;
    GenreAdapter adapter;
    MovieItemAdapter movieItemAdapterRecycler;
    YouTubePlayerSupportFragment youTubePlayerFragment;


    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final int MOVIE_DETAILS = 2;
    private static final int VIDEO_KEY = 3;
    private static final int SIMILAR_MOVIES = 4;
    String videoKey;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        SharedPreferences sharedPref = this.getSharedPreferences("MY_SHARED_PREFERENCE",Context.MODE_PRIVATE);
        int movie_id = sharedPref.getInt("KEY", 0);




        similarMovies = (RecyclerView)findViewById(R.id.recycler_view);
        youTubePlayerFragment = (YouTubePlayerSupportFragment)getSupportFragmentManager().
                findFragmentById(R.id.youtube_player_fragment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_bar);
        appBarLayout = (AppBarLayout)findViewById(R.id.appBarLayout);
        mMovieTitleView = (TextView)findViewById(R.id.movie_title);
        mMovieDetails = (TextView)findViewById(R.id.movie_details);
        mMovieDuration = (TextView)findViewById(R.id.movie_duration);
        myLinearLayout = (LinearLayout)findViewById(R.id.show_movie_status);
        genreRecycler = (RecyclerView)findViewById(R.id.genre_recycler);


        List<String> genre = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(MOVIE_DETAILS,null,this);
        loaderManager.initLoader(VIDEO_KEY,null,this);

        List<MovieItem> similar_movies = new ArrayList<>();

        adapter = new GenreAdapter(genre,this);
        movieItemAdapterRecycler = new MovieItemAdapter(similar_movies,this,this);
        genreRecycler.setLayoutManager(new GridLayoutManager(this,3));
        genreRecycler.setAdapter(adapter);
        similarMovies.setLayoutManager(new GridLayoutManager(this,3));
        similarMovies.setAdapter(movieItemAdapterRecycler);

    }

    @NonNull
    @Override
    public Loader<List<MovieItem>> onCreateLoader(int id, @Nullable Bundle args) {
       switch (id){
           case MOVIE_DETAILS:
               return new MovieLoader(this, BASE_URL, MOVIE_DETAILS);
           case VIDEO_KEY:
               return new MovieLoader(this, BASE_URL, VIDEO_KEY);
               default:
                   return null;
       }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieItem>> loader, List<MovieItem> data) {
        switch (loader.getId()){
            case MOVIE_DETAILS:
                Log.v("massage",data.get(0).getName()+" "+data.get(0).getDetails()+" "+data.get(0).getDuration());
                mMovieTitleView.setText(data.get(0).getName());
                mMovieDetails.setText(data.get(0).getDetails());
                mMovieDuration.setText(data.get(0).getDuration()+"min");
                adapter.setGenre(data.get(0).getGenre());
                toolbar.setTitle(data.get(0).getName());
                break;
            case VIDEO_KEY:
                videoKey = data.get(0).getVideo_key();
                if(videoKey!= null)
                {
                    youTubePlayerFragment.initialize("AIzaSyDD76n360AILBZ2U5nPLR-5EkpNLY0vpwo",
                            new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer,
                                                                    boolean wasRestored) {
                                    if (!wasRestored) {
                                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                                        youTubePlayer.cueVideo(videoKey);
                                        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                                            @Override
                                            public void onPlaying() {
                                                youTubePlayer.setFullscreen(true);
                                            }

                                            @Override
                                            public void onPaused() {

                                            }

                                            @Override
                                            public void onStopped() {

                                            }

                                            @Override
                                            public void onBuffering(boolean b) {

                                            }

                                            @Override
                                            public void onSeekTo(int i) {

                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });
                }
                Log.v("video_path",videoKey);
                break;
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieItem>> loader) {
        adapter.setGenre(new ArrayList<String>());
    }




    @Override
    public void onitemclick(int movie_id) {

    }
}
