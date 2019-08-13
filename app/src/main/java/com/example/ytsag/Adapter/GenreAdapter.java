package com.example.ytsag.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ytsag.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {

    List<String> genre;
    Context context;

    public GenreAdapter(List<String> genre, Context context) {
        this.genre = genre;
        this.context = context;
    }

    public void setGenre(List<String> arr){
        genre = arr;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.genre_item,parent,false);
        return new GenreHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreHolder holder, int position) {
           holder.mGenreView.setText(genre.get(position));
           holder.mGenreView.setBackgroundResource(setColor(genre.get(position)));
        }

    private int setColor(String s) {
        switch (s){
            case "Animation":
                return R.color.animation;
            case "Thriller":
                return R.color.thriller;
            case "Horror":
                return R.color.horror;
            case "War":
                return R.color.war;
            case "Music":
                return R.color.music;
            case "Mystery":
                return R.color.mystery;
            case "Documentary":
                return R.color.documentary;
            case "Crime":
                return R.color.crime;
            case "Comedy":
                return R.color.comedy;
            case "Action":
                return R.color.action;
            case "Adventure":
                return R.color.adventure;
            case "Family":
                return R.color.family;
            case "Fantasy":
                return R.color.fantasy;
            case "Science Fiction":
                return R.color.science_fiction;
                default:
                    return R.color.crime;
        }
    }


    @Override
    public int getItemCount() {
        return genre.size();
    }

    public class GenreHolder extends RecyclerView.ViewHolder {
        TextView mGenreView;
        public GenreHolder(@NonNull View itemView) {
            super(itemView);
            mGenreView = (TextView)itemView.findViewById(R.id.genre);
        }
    }
}
