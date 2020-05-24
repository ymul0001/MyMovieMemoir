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

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder> {
    private List<WatchList> watchlistItems;
    private ClickListeners listeners;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView watchListTitleTv;
        public TextView watchListReleaseTv;
        public TextView watchListAddedDateTv;
        public TextView watchListAddedTimeTv;
        public Button viewButton;
        public Button deleteButton;

        public ViewHolder(View itemView, final ClickListeners listeners) {
            super(itemView);
            watchListTitleTv= itemView.findViewById(R.id.watch_title);
            watchListReleaseTv = itemView.findViewById(R.id.watch_release);
            watchListAddedDateTv = itemView.findViewById(R.id.watch_adddate);
            watchListAddedTimeTv = itemView.findViewById(R.id.watch_addtime);
            viewButton = itemView.findViewById(R.id.view_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeners.onDeleteButtonClick(getAdapterPosition());
                }
            });
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeners.onViewButtonClick(getAdapterPosition());
                }
            });
        }

        public void setCustomClick(ClickListeners newListeners){
            listeners = newListeners;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface ClickListeners{
        void onDeleteButtonClick(int position);
        void onViewButtonClick(int position);
    }

    public WatchlistAdapter (List<WatchList> watchlistItems, ClickListeners listeners){
        this.watchlistItems = watchlistItems;
        this.listeners = listeners;
    }


    public int getItemCount(){
        return watchlistItems.size();
    }


    public WatchlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                       int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieSearchView = inflater.inflate(R.layout.watchlist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(movieSearchView, listeners);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull final WatchlistAdapter.ViewHolder viewHolder,
                                 int position) {
        final WatchList item = watchlistItems.get(position);

        // viewholder binding with its data at the specified position
        viewHolder.watchListTitleTv.setText(item.getMovieName());
        viewHolder.watchListReleaseTv.setText("Release date: " + item.getReleaseDate());
        viewHolder.watchListAddedDateTv.setText("Add date: " + item.getAddedDate());
        viewHolder.watchListAddedTimeTv.setText(" , " + item.getAddedTime());

    }
}
