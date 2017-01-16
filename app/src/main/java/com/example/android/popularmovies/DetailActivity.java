package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.popularmovies.data.MovieDatabaseAPI;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE_ID = "movie_id";
    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.display_fragment_container);

        if (fragment == null) {
            fragment = initFragment();
            fm.beginTransaction()
                    .add(R.id.display_fragment_container, fragment)
                    .commit();
        }
    }

    public Fragment initFragment() {
        // get movie from intent
        Intent intent;
        int movieId;
        if ((intent = getIntent()) != null) {
             movieId = intent.getIntExtra(EXTRA_MOVIE_ID, MovieDatabaseAPI.INVALID_MOVIE_ID);
        }
        else {
            Log.d(TAG, "initFragment: getIntent() return null.");
            movieId = MovieDatabaseAPI.INVALID_MOVIE_ID;
        }

        return DetailFragment.createFragment(movieId);
    }

    public static Intent newIntent(Context packageContext, int movieId) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);

        return intent;
    }

}
