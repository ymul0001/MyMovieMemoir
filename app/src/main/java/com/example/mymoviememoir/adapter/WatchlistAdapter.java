package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entities.WatchList;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder>  {
    private List<WatchList> watchlistItems;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView watchListTitleTv;
        public TextView watchListReleaseTv;
        public TextView watchListAddedDateTv;
        public TextView watchListAddedTimeTv;
        public Button viewButton;
        public Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            watchListTitleTv= itemView.findViewById(R.id.watch_title);
            watchListReleaseTv = itemView.findViewById(R.id.watch_release);
            watchListAddedDateTv = itemView.findViewById(R.id.watch_adddate);
            watchListAddedTimeTv = itemView.findViewById(R.id.watch_addtime);
            viewButton = itemView.findViewById(R.id.view_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

    }


    public WatchlistAdapter (List<WatchList> watchlistItems){
        this.watchlistItems = watchlistItems;
    }

   /* //can comment this code
    public void addMovies(List<MovieResult> resultItems) {
        this.movieItems = resultItems;
        notifyDataSetChanged();
    }*/

    public int getItemCount(){
        return watchlistItems.size();
    }

    //build a custom onClickListener for the recycle view
    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

    public WatchlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                       int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieSearchView = inflater.inflate(R.layout.watchlist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(movieSearchView);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull WatchlistAdapter.ViewHolder viewHolder,
                                 int position) {
        final WatchList item = watchlistItems.get(position);

        // viewholder binding with its data at the specified position
        viewHolder.watchListTitleTv.setText(item.getMovieName());
        viewHolder.watchListReleaseTv.setText("Release date: " + item.getReleaseDate());
        viewHolder.watchListAddedDateTv.setText("Add date: " + item.getAddedDate());
        viewHolder.watchListAddedTimeTv.setText(" , " + item.getAddedTime());
    }
}
