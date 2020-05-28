package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entities.FullMemoir;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemoirAdapter extends RecyclerView.Adapter<MemoirAdapter.ViewHolder> {
    private List<FullMemoir> memoirItems;
    private ClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView memoirTitleTv;
        public TextView memoirReleaseTv;
        public TextView memoirWatchedTv;
        public TextView cinemaPostcodeTv;
        public RatingBar memoirRating;
        public TextView memoirCommentsTv;
        public ImageView memoirImageIv;
        public Button viewButton;

        //view holder's constructor for the memoir adapter
        public ViewHolder(View itemView, final ClickListener listener) {
            super(itemView);
            memoirTitleTv = itemView.findViewById(R.id.memoir_title);
            memoirReleaseTv = itemView.findViewById(R.id.memoir_release_date);
            memoirWatchedTv = itemView.findViewById(R.id.memoir_watch_date);
            cinemaPostcodeTv = itemView.findViewById(R.id.memoir_post_code);
            memoirRating = itemView.findViewById(R.id.memoir_rating);
            memoirCommentsTv = itemView.findViewById(R.id.memoir_comments);
            memoirImageIv = itemView.findViewById(R.id.memoir_image);
            viewButton = itemView.findViewById(R.id.memoir_view_button);

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onViewButtonClick(getAdapterPosition());
                }
            });
        }

        public void setCustomClick(ClickListener newListener){
            listener = newListener;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface ClickListener{
        void onViewButtonClick(int position);
    }

    public MemoirAdapter (List<FullMemoir> memoirItems, ClickListener listener){
        this.memoirItems = memoirItems;
        this.listener = listener;
    }


    public int getItemCount(){
        return memoirItems.size();
    }


    public MemoirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View memoirSearchView = inflater.inflate(R.layout.memoir_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(memoirSearchView, listener);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull final MemoirAdapter.ViewHolder viewHolder,
                                 int position) {
        final FullMemoir item = memoirItems.get(position);
        // viewholder binding with its data at the specified position
        viewHolder.memoirTitleTv.setText(item.getMemoirName());
        viewHolder.memoirReleaseTv.setText(item.getReleaseDate());
        viewHolder.memoirWatchedTv.setText(item.getWatchDate());
        viewHolder.cinemaPostcodeTv.setText(String.valueOf(item.getPostCode()));
        viewHolder.memoirRating.setRating(item.getUserRating());
        viewHolder.memoirCommentsTv.setText(item.getComments());
        Picasso.get().load("https://image.tmdb.org/t/p/original" + item.getMemoirImagePath()).into(viewHolder.memoirImageIv);
    }
}
