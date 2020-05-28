package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.activities.MovieDetails;
import com.example.mymoviememoir.fragments.SearchFragment;
import com.example.mymoviememoir.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>  {
    private RecyclerViewClickListener listener;
    private List<MovieResult> movieItems;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView movieIv;
        public TextView movieTitleTv;
        public TextView movieReleaseTv;

        //view holder constructor for the search adapter
        public ViewHolder(View itemView) {
            super(itemView);
            movieIv = itemView.findViewById(R.id.moviedb_image);
            movieTitleTv = itemView.findViewById(R.id.moviedb_title);
            movieReleaseTv =itemView.findViewById(R.id.moviedb_releasedate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }


    public SearchAdapter(List<MovieResult> movieResults, RecyclerViewClickListener listener){
        this.movieItems = movieResults;
        this.listener = listener;
    }

    public void addMovies(List<MovieResult> resultItems) {
        this.movieItems = resultItems;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return movieItems.size();
    }

    //build a custom onClickListener for the recycle view
    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat inputDf = new SimpleDateFormat("yyyy-MM-dd");

        // viewholder binding with its data at the specified position
        ImageView ivMovie = viewHolder.movieIv;
        TextView tvMovieTitle = viewHolder.movieTitleTv;
        TextView tvMovieRelease = viewHolder.movieReleaseTv;
        Picasso.get().load(item.getMovieImagePath()).into(ivMovie);
        tvMovieTitle.setText(item.getMovieTitle());
        try {
            tvMovieRelease.setText("Release year: " + sdf.format(inputDf.parse(item.getMovieReleaseDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
