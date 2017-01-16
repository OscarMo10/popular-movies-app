package com.example.android.popularmovies;

import android.support.test.runner.AndroidJUnit4;

import com.example.android.popularmovies.data.MovieDatabaseAPI;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by oscar on 12/17/16.
 */

@RunWith(AndroidJUnit4.class)
public class MovieDatabaseAPITest {
    @Test
    public void getPopularMovies() throws Exception {
        List<MovieDatabaseAPI.Movie> list = MovieDatabaseAPI.getPopularMovies(1);

        assertNotNull(list);

        if (list != null) {
            assertFalse("Movie list return is emtpy", list.size() == 0);
        }

    }

}