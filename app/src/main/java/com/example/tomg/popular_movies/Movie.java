package com.example.tomg.popular_movies;

/**
 * Class to hold various data about movies
 */

public class Movie {
    private String posterPath;
    String overview;
    String releaseDate;
    String title;
    String backdrop;
    Float popularity;
    String voteAverage;

    public Movie(String posterPath, String overview, String releaseDate, String title, String backdrop,
        Float popularity, String voteAverage){
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate =releaseDate;
        this.title = title;
        this.backdrop = backdrop;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
    }

    public String getPosterPath(){
        return posterPath;
    }

}
