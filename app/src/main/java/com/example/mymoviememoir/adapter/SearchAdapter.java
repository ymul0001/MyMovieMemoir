package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>  {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieIv;
        public TextView movieTitleTv;
        public TextView movieReleaseTv;


        public ViewHolder(View itemView) {
            super(itemView);
            movieIv = itemView.findViewById(R.id.moviedb_image);
            movieTitleTv = itemView.findViewById(R.id.moviedb_title);
            movieReleaseTv =itemView.findViewById(R.id.moviedb_releasedate);
        }
    }

    private List<MovieResult> movieItems;
    public SearchAdapter(List<MovieResult> movieResults){
        this.movieItems = movieResults;
    }

    public void addMovies(List<MovieResult> resultItems) {
        this.movieItems = resultItems;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return movieItems.size();
    }

    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieSearchView = inflater.inflate(R.layout.moviedb_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(movieSearchView);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder,
                                 int position) {
        final MovieResult item = movieItems.get(position);
        // viewholder binding with its data at the specified position
        ImageView ivMovie = viewHolder.movieIv;
        TextView tvMovieTitle = viewHolder.movieTitleTv;
        TextView tvMovieRelease = viewHolder.movieReleaseTv;
        Picasso.get().load(item.getMovieImagePath()).into(ivMovie);
        tvMovieTitle.setText(item.getMovieTitle());
        tvMovieRelease.setText("Release date " + item.getMovieReleaseDate());
    }
}
