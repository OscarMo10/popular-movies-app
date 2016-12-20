package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DiscoveryActivity extends AppCompatActivity implements DiscoveryActivityFragment.MovieChosenListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.discover_fragment_container);

        if (fragment == null) {
            fragment = new DiscoveryActivityFragment();
            fm.beginTransaction()
                    .add(R.id.discover_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onMovieChosen(MovieDatabaseAPI.Movie movie) {
        // TODO Implement function
    }
}
