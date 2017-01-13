package com.example.android.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import com.example.android.popularmovies.data.MovieContract;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by oscar on 1/3/17.
 */

@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {
    private static final long TEST_MOVIE_ID = 10L;

    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.Content_URI;

    @Test
    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        // test movie dir
        assertThat(testMatcher.match(TEST_MOVIE_DIR), is(MovieProvider.MOVIE));
    }
}
