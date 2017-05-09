package com.example.tomg.popular_movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by me123 on 4/28/17.
 */

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ViewHolder> {
    private int mNumberItems;
    ArrayList<Movie> mData;
    private LayoutInflater inflator;
    private ItemClickListener mClickListener;
    private Context mContext;

    public ImageGridAdapter(Context context, ArrayList<Movie> data){
        this.inflator = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.recycler_grid_view, parent, false);

        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imagePath = mData.get(position).getPosterPath();
        Picasso.with(mContext).load(imagePath).into(holder.imgMovie);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgMovie;

        public ViewHolder(View itemView){
            super(itemView);
            imgMovie = (ImageView) itemView.findViewById(R.id.img_movie);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View itemView){
            if(mClickListener != null){
                mClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    public Movie getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }


}
