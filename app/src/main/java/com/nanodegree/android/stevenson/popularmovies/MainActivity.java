package com.nanodegree.android.stevenson.popularmovies;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nanodegree.android.stevenson.popularmovies.models.Movie;
import com.nanodegree.android.stevenson.popularmovies.rest.MoviesService;
import com.nanodegree.android.stevenson.popularmovies.rest.ServiceFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mMoviesGrid;
    private MoviesGridAdapter mMoviesGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGrid = (RecyclerView) findViewById(R.id.movies_rv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mMoviesGrid.setLayoutManager(gridLayoutManager);



        MoviesService moviesService = ServiceFactory.getService(MoviesService.class);

        final Call<List<Movie>> request = moviesService.getTopRatedMovies();

        request.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    int size = response.body().size();
                    Log.e(TAG, "onResponse: " + size);

                    mMoviesGridAdapter = new MoviesGridAdapter(response.body());
                    mMoviesGrid.setAdapter(mMoviesGridAdapter);
                } else {
                    Log.e(TAG, "onResponse: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, "onFailure: error retrieving movie data", t);
            }
        });
    }
}
