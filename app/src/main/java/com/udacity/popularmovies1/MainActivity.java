package com.udacity.popularmovies1;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.udacity.popularmovies1.adapter.MoviesAdapter;
import com.udacity.popularmovies1.api.Client;
import com.udacity.popularmovies1.api.Service;
import com.udacity.popularmovies1.models.Movie;
import com.udacity.popularmovies1.models.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MoviesAdapter mMoviesAdapter;
    private List<Movie> mMoviesList;

    public static final String MOST_POPULAR ="Most Popular";
    public static final String TOP_RATED ="Highest Rated";

    public static final String LOG_TAG = MoviesAdapter.class.getName();
    @BindView(R.id.main_content)SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.movie_rv)RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)ProgressBar mLoadindIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setupSharedPreferences();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ButterKnife.bind(this);

        initViews(sharedPreferences);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_orange_dark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews(sharedPreferences);
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        //Register the listerner
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public Activity getActivity(){
        Context context =this;
        while (context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    private void initViews(SharedPreferences sortpref){
        mLoadindIndicator.setVisibility(View.VISIBLE);

        mMoviesList = new ArrayList<>();
        mMoviesAdapter = new MoviesAdapter(this, mMoviesList);

        if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesAdapter.notifyDataSetChanged();

//        loadJSON(sortpref.getString("sort", MOST_POPULAR));
        loadJSON(sortpref);
        sortpref.registerOnSharedPreferenceChangeListener(this);

    }

    private  void loadJSON(SharedPreferences sortpref){
        try {
            if (BuildConfig.THE_DBMOVIES_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(), "You need de API Key from www.themoviedb.org", Toast.LENGTH_SHORT).show();
               mLoadindIndicator.setVisibility(View.INVISIBLE);
                return;
            }
            Client mClient = new Client();
            Service apiService= Client.getClient().create(Service.class);
            Call<MoviesResponse> call = null;
            if (sortpref.getString("sort", MOST_POPULAR).equals(MOST_POPULAR)){
                call= apiService.getPopularMovies(BuildConfig.THE_DBMOVIES_API_TOKEN);
            }else if(sortpref.getString("sort", MOST_POPULAR).equals(TOP_RATED)){
                call= apiService.getTopRatedMovies(BuildConfig.THE_DBMOVIES_API_TOKEN);
            }
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> mMovies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), mMovies));
                    mRecyclerView.smoothScrollToPosition(0);
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                   mLoadindIndicator.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Eror",t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fecthing data", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Log.d("Error ", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //Add menu to  the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //when the settings menu item is pressed, open setting activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            initViews(sharedPreferences);
    }
}
