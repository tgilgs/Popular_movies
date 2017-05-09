package com.example.tomg.popular_movies.utilities;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.tomg.popular_movies.Movie;

/**
 * Utilities to convert JSON from API call to usable data
 */

public class JsonMovieUtils {


    /**
     * This method parses JSON from a web response and returns an array of Movie
     * containing data from various movies.
     * <p/>
     *
     * @param moviesStr JSON response from server
     *
     * @return Array of class Movie
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */

    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_IMG_SIZE = "w500//";
    private static final String BACKDROP_IMG_SIZE = "w1000//";

    public static Movie[] getMovieDataFromJson(String moviesStr)
            throws JSONException {


        final String PAGE = "&page=";
        final String RESULTS = "results";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_TITLE = "title";
        final String MOVIE_BACKDROP = "backdrop_path";
        final String MOVIE_POPULARITY = "popularity";
        final String MOVIE_VOTE = "vote_average";

        Movie[] parsedMovieData = null;

        JSONObject moviesJson = new JSONObject(moviesStr);

        JSONArray moviesArray = moviesJson.getJSONArray(RESULTS);

        parsedMovieData = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            //temp strings for all data fields
            String posterPath, overview, releaseDate, title, backdropPath, voteAverage;

            Float popularity;

            //Get the JSON object representing a movie
            JSONObject singleMovie = moviesArray.getJSONObject(i);

            posterPath = singleMovie.getString(MOVIE_POSTER_PATH);
            overview = singleMovie.getString(MOVIE_OVERVIEW);
            releaseDate = singleMovie.getString(MOVIE_RELEASE_DATE);
            title = singleMovie.getString(MOVIE_TITLE);
            backdropPath = singleMovie.getString(MOVIE_BACKDROP);

            popularity = Float.parseFloat(singleMovie.getString(MOVIE_POPULARITY));
            voteAverage = singleMovie.getString(MOVIE_VOTE);

            //get full paths for poster and backdrop
            posterPath = IMG_BASE_URL + POSTER_IMG_SIZE + posterPath;
            backdropPath = IMG_BASE_URL + BACKDROP_IMG_SIZE + backdropPath;


            parsedMovieData[i]= new Movie(posterPath, overview, releaseDate, title,
                                            backdropPath, popularity, voteAverage);

        }

        return parsedMovieData;
    }

}
