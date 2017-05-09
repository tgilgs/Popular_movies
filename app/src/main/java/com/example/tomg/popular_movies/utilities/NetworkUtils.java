package com.example.tomg.popular_movies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by me123 on 5/5/17.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    //Insert own API key here to search tmdb database
    //*****************************************************************
    private static final String _API_KEY = "REDACTED";
    //*****************************************************************

    private static final String API_KEY = "api_key=";
    private static final String DB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String POPULAR_URL = "popular";
    private static final String TOP_RATED_URL = "top_rated";
    private static final String PAGE_NUMBER = "&page=";

    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMG_SIZE = "w500//";

    public static URL buildDbQueryUrl(String queryType, String pageNumber){


        String dbURL = DB_BASE_URL + queryType + "?" + API_KEY + _API_KEY + PAGE_NUMBER + pageNumber;
        Uri builtUri = Uri.parse(dbURL).buildUpon().build();

        URL returnUrl = null;
        try {
            returnUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "built URI " + returnUrl);

        return returnUrl;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
