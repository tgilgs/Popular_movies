package com.example.tomg.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.tomg.popular_movies.utilities.JsonMovieUtils;
import com.example.tomg.popular_movies.utilities.NetworkUtils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ImageGridAdapter.ItemClickListener{

    Context mainActivity;
    ImageGridAdapter gridAdapter;
    ArrayList<Movie> movieData;
    private RecyclerView rvGridView;
    GridLayoutManager layoutManager;

    //search for popular movies by default
    private String searchType = "popular";

    private int pageNumber;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    boolean rv_created = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        loadMovieData(searchType);
        movieData = new ArrayList<Movie>();
        pageNumber=1;

        //prepare recyclerview for data
        rvGridView = (RecyclerView) findViewById(R.id.rv_img_grid);
        int numberOfCol = 2;
        layoutManager = new GridLayoutManager(this, numberOfCol);
        rvGridView.setLayoutManager(layoutManager);
        rvGridView.setHasFixedSize(true);


    }

    public void loadMovieData(String searchType){
        new fetchMovieJsonTask().execute(searchType);
    }

    //finish setup of recyclerview grid adapter to show movie posters
    public void createGridAdapter(){
        gridAdapter = new ImageGridAdapter(this, movieData);
        gridAdapter.setClickListener(this);
        rvGridView.setAdapter(gridAdapter);

        //start scroll listener to detect when the end of the page has been reached
        //if it has start another async JSON task to get the next page
        rvGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            //TODO fix glitch when scrolling back up
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (!loading){
                        if((totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                            // End is about to be reached, load more views

                            Log.i("Reached end loading: ", String.valueOf(pageNumber+1));

                            // fetch the next page of JSON and add to arrayList
                            loading = true;
                            new fetchMovieJsonTask().execute(searchType, String.valueOf(++pageNumber));

                        }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return(true);
    }

    //Menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSeleted = item.getItemId();

        if( itemSeleted == R.id.action_popular){
            searchType = "popular";
            loadMovieData(searchType);
            createGridAdapter();
            return true;
        }
        else if(itemSeleted == R.id.action_top_rated){
            searchType = "top_rated";
            loadMovieData(searchType);
            createGridAdapter();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {

        Context mainActivity = MainActivity.this;
        Class detailedActivity = MovieDetailActivity.class;
        Intent startMovieDetailActivity = new Intent(mainActivity, detailedActivity);

        startMovieDetailActivity.putExtra("movieTitle", movieData.get(position).title);
        startMovieDetailActivity.putExtra("movieOverview", movieData.get(position).overview);
        startMovieDetailActivity.putExtra("movieDate", movieData.get(position).releaseDate);
        startMovieDetailActivity.putExtra("movieBackdrop", movieData.get(position).backdrop);
        startMovieDetailActivity.putExtra("movieVoteAverage", movieData.get(position).voteAverage);

        startActivity(startMovieDetailActivity);
    }

    public class fetchMovieJsonTask extends AsyncTask<String, Void, Movie[]> {

        //params[0] = search type "popular" or "top_rated
        //params[1] = page number

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0){
                return null;
            }
            String pageNumber;
            if(params.length == 1){
                pageNumber = "1";
            } else {
                pageNumber = params[1];
            }

            URL requestUrl = NetworkUtils.buildDbQueryUrl(params[0], pageNumber);

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                Movie[] movieDataArray = JsonMovieUtils.getMovieDataFromJson(jsonMoviesResponse);

                //if searching for page 1 clear the array
                if(pageNumber == "1"){
                    movieData.clear();
                    for(int i=0; i<movieDataArray.length; ++i){
                        movieData.add(i, movieDataArray[i]);
                    }
                } else {
                    //else add results to the array list
                    int arraysize = movieData.size();
                    int oldsize = arraysize;
                    for(Movie i : movieDataArray){
                        movieData.add(arraysize++, i);
                    }
                }
                return movieDataArray;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Movie[] movieDataArray) {
            if(movieDataArray != null){
                loading = false;

                //create the grid adapter once the poster Urls have been built
                if(!rv_created) {
                    createGridAdapter();
                    rv_created=true;
                }
            }
        }
    }
}