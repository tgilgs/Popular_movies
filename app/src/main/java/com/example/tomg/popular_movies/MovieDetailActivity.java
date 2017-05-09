package com.example.tomg.popular_movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {


    private TextView movieTitle, movieOverview, movieDate, movieRating;
    private ImageView moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTitle = (TextView) findViewById(R.id.movie_title_detail_tv);
        movieOverview = (TextView) findViewById(R.id.overview_detail_tv);
        movieDate = (TextView) findViewById(R.id.release_date_detail_tv);
        movieRating = (TextView) findViewById(R.id.user_rating_tv);
        moviePoster = (ImageView) findViewById(R.id.movie_poster_detail_img);

        Intent parentIntent = getIntent();

        if(parentIntent.hasExtra("movieTitle")){
            movieTitle.setText(parentIntent.getStringExtra("movieTitle"));
        }
        if(parentIntent.hasExtra("movieOverview")){
            movieOverview.setText(parentIntent.getStringExtra("movieOverview"));
        }
        if(parentIntent.hasExtra("movieVoteAverage")){
            movieRating.setText(parentIntent.getStringExtra("movieVoteAverage"));
        }
        if(parentIntent.hasExtra("movieDate")){
            movieDate.setText(parentIntent.getStringExtra("movieDate"));
        }
        if(parentIntent.hasExtra("movieBackdrop")){
            Picasso.with(this).load(parentIntent.getStringExtra("movieBackdrop")).into(moviePoster);
        }

    }
}
