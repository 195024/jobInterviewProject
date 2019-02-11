package com.example.test.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.test.util.Constants.IMAGE_BASE_URL;
import static com.example.test.util.Constants.IMAGE_FILE_SIZE;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private List<Movie> movies;

    public void setData(List<Movie> movies) {
        this.movies = movies;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle;

        DataViewHolder(View v) {
            super(v);
            ivPoster = v.findViewById(R.id.imageview_poster);
            tvTitle = v.findViewById(R.id.textview_title);
        }
    }

    public RecyclerViewAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
        vh = new DataViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String url = IMAGE_BASE_URL + IMAGE_FILE_SIZE + movie.getPosterPath();

        Picasso.get().load(url).into(((DataViewHolder) holder).ivPoster);
        ((DataViewHolder) holder).tvTitle.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if(movies != null)
            return movies.size();
        else
            return -1;
    }

}