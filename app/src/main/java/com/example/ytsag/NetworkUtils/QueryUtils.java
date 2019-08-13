package com.example.ytsag.NetworkUtils;

import android.util.Log;

import com.example.ytsag.Data.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static  final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final int MOVIE_DETAILS = 2;
    private static final int VIDEO_KEY = 3;
    private static final int CAST_CREW = 4;

    private QueryUtils(){}

    public static List<MovieItem> fetchMovies(String requestUrl, int mode){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

       switch (mode){
           case 0:
               List<MovieItem> movies = extractMovies(jsonResponse);
               return movies;
           case MOVIE_DETAILS:
               List<MovieItem> details = extractDetails(jsonResponse);
               return details;
           case VIDEO_KEY:
               List<MovieItem> key = getKey(jsonResponse);
               return key;
           case CAST_CREW:
               List<MovieItem> cast = getCast(jsonResponse);
               return cast;
               default:
                   return null;
       }

    }

    private static List<MovieItem> getCast(String jsonResponse) {
        List<MovieItem> cast = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray castArray = root.getJSONArray("cast");
            JSONArray crewArray = root.getJSONArray("crew");

            for(int i = 0 ; i < castArray.length() ; i++)
            {
                JSONObject currentObj = crewArray.getJSONObject(i);
                String castName = currentObj.getString( "character");
                String castImagePath = currentObj.getString("profile_path");

                MovieItem item = new MovieItem(castName,castImagePath);
                cast.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cast;
    }

    private static List<MovieItem> getKey(String jsonResponse) {
        List<MovieItem> keys = new ArrayList<>();
        try {
            JSONObject root =  new JSONObject(jsonResponse);
            JSONArray videoArray = root.getJSONArray("results");
            for(int i = 0 ; i < videoArray.length() ; i++){
                JSONObject currentObject = videoArray.getJSONObject(i);
                String key = currentObject.getString("key");

                MovieItem item = new MovieItem(key);
                keys.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keys;
    }

    private static List<MovieItem> extractDetails(String jsonResponse) {

        List<MovieItem> details = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);

            String name = root.getString("title");
            String overview = root.getString("overview");
            int duration = root.getInt("runtime");
            JSONArray genre = root.getJSONArray("genres");
            List<String> genres = new ArrayList<>();
            for(int i = 0 ; i < genre.length() ; i++){
                JSONObject current_genre = genre.getJSONObject(i);
                genres.add( current_genre.getString("name") );
            }
            Log.v("message",name + overview + duration + genres);
            MovieItem movieDetails = new MovieItem(name,overview,duration,genres);
            details.add(movieDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return details;
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  url;
    }

    private static List<MovieItem> extractMovies(String jsonResponse) {

        List<MovieItem> movies = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray movieArray = root.getJSONArray("results");

            for(int i = 0 ; i < movieArray.length() ; i++){
                JSONObject currentMovieObject = movieArray.getJSONObject(i);

                String title = currentMovieObject.getString("title");
                String date = currentMovieObject.getString("release_date");
                String imagePath = IMAGE_BASE_URL+currentMovieObject.getString("poster_path");
                int id = currentMovieObject.getInt("id");

                String arr[] = date.split("-");


                Log.v("message",title+" "+arr[0]+" "+imagePath);

                MovieItem movie = new MovieItem(title,arr[0],imagePath,id);
                movies.add(movie);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    private static String makeHttpConnection(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStreamReader = null;

        urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(15000);
        urlConnection.setReadTimeout(10000);
        urlConnection.connect();

        if(urlConnection.getResponseCode() == 200){
            inputStreamReader = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStreamReader);
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStreamReader) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStreamReader!=null){
           InputStreamReader inputStreamReader1 = new InputStreamReader(inputStreamReader, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader1);
            String line = reader.readLine();

            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

}
