package com.my.boostcamppreassignment.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.my.boostcamppreassignment.R;
import com.my.boostcamppreassignment.data.MovieDetailData;
import com.my.boostcamppreassignment.views.viewholder.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    Context context;
    List<MovieDetailData> items = null;
    View.OnClickListener onItemClick;

    public MovieAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void getItems(List<MovieDetailData> items){
        this.items = items;
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.row_movie, parent, false);
        itemView.setOnClickListener(onItemClick);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Glide.with(context)
                .load(items.get(position).image)
                .into(holder.moviePoster);
//        holder.movieTitle.setText(items.get(position).title);
        holder.movieTitle.setText(Html.fromHtml(items.get(position).title));
        holder.movieRating.setRating((Float.parseFloat(items.get(position).userRating)/2.0f));
        holder.movieOpenYear.setText(items.get(position).pubDate);
        holder.movieDirector.setText(Html.fromHtml(items.get(position).director));
        holder.movieActors.setText(Html.fromHtml(items.get(position).actor));

    }

    public void setOnItemClick(View.OnClickListener l){
        onItemClick = l;
    }
}
