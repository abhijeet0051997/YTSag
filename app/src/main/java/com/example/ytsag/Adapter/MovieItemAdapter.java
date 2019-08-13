package com.example.ytsag.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.ytsag.Data.MovieItem;
import com.example.ytsag.R;

import java.util.ArrayList;
import java.util.List;


public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.MovieViewHolder> {

    private List<MovieItem> data = new ArrayList<>();
    private Context context;
    itemClickListener listener;


    public MovieItemAdapter(List<MovieItem> data, Context context , itemClickListener listener ) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }


    public interface itemClickListener{
        void onitemclick(int movie_id);
    }

    public void updateUi(List<MovieItem> newData){
        data = newData;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.movie_item,viewGroup,false);

        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder viewHolder, int i)
    {
        MovieItem current = data.get(i);
        viewHolder.nameTextView.setText(current.getName());
        Glide.with(context)
                .load(current.getImageUrl())
                .into(viewHolder.imageView);
        viewHolder.yearTextView.setText(String.valueOf(current.getDate()));
    }

    @Override
    public int getItemCount() {
        try {
            return data.size();
        }catch (Exception e)
        {
        return 0;
        }

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView, yearTextView;
        ImageView imageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =(ImageView) itemView.findViewById(R.id.movie_image_view);
            nameTextView = (TextView) itemView.findViewById(R.id.movie_title);
            yearTextView = (TextView) itemView.findViewById(R.id.movie_year);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onitemclick(data.get(pos).getId());
            Log.v("message",String.valueOf(data.get(pos).getId()));
        }
    }
}
