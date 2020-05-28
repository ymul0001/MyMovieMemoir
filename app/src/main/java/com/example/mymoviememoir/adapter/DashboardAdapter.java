package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.TopfiveResult;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder>  {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTv;
        public TextView releaseDateTv;
        public TextView ratingTv;

        //dashboard view holder constructors
        public ViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.watch_title);
            releaseDateTv = itemView.findViewById(R.id.watch_release);
            ratingTv =itemView.findViewById(R.id.item_rating);
        }
    }

    private List<TopfiveResult> resultItems;
    public DashboardAdapter(List<TopfiveResult> resultItems){
        this.resultItems = resultItems;
    }

    public void addUnits(List<TopfiveResult> resultItems) {
        this.resultItems = resultItems;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return resultItems.size();
    }
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View topFiveItemsView = inflater.inflate(R.layout.topfive_item, parent, false);
        // construct the viewholder with the new view
        ViewHolder viewHolder = new ViewHolder(topFiveItemsView);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull DashboardAdapter.ViewHolder viewHolder,
                                 int position) {
        final TopfiveResult item = resultItems.get(position);
        // viewholder binding with its data at the specified position
        TextView tvTitle = viewHolder.titleTv;
        tvTitle.setText(item.getMovieName());
        TextView tvReleaseDate = viewHolder.releaseDateTv;
        tvReleaseDate.setText("Release date: " + item.getReleaseDate());
        TextView tvRating =viewHolder.ratingTv;
        tvRating.setText(item.getMovieRating());
    }
}
