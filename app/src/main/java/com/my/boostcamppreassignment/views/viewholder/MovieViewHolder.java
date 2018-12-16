package com.my.boostcamppreassignment.views.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.my.boostcamppreassignment.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView moviePoster;
    public TextView movieTitle;
    public RatingBar movieRating;
    public TextView movieOpenYear;
    public TextView movieDirector;
    public TextView movieActors;

    public MovieViewHolder(View itemView) {
        super(itemView);

        moviePoster = itemView.findViewById(R.id.movie_poster);
        movieTitle = itemView.findViewById(R.id.movie_title);
        movieRating = itemView.findViewById(R.id.movie_rating);
        movieOpenYear = itemView.findViewById(R.id.movie_open_year);
        movieDirector = itemView.findViewById(R.id.movie_director);
        movieActors = itemView.findViewById(R.id.movie_actors);
    }
}
