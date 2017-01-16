package com.example.android.popularmovies.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by oscar on 1/15/17.
 */

public class MovieListResult {
    private final int mPage;
    private final int mTotalResult;
    private final int mTotalPages;
    private final List<MovieListResultItem> mMovies;

    private MovieListResult(int page,
                            int totalResult,
                            int totalPages,
                            List<MovieListResultItem> movies)
    {
        mPage = page;
        mTotalResult = totalResult;
        mTotalPages = totalPages;
        mMovies = movies;
    }

    public static MovieListResult createMovieListResultFromJsonStr(String jsonStr) throws JSONException {
        MovieListResult movieListResult = null;

        try {
            JSONObject movieList = new JSONObject(jsonStr);

            int page = movieList.getInt(MovieListResultProperty.PAGE);
            int totalResults = movieList.getInt(MovieListResultProperty.TOTAL_RESULTS);
            int totalPages = movieList.getInt(MovieListResultProperty.TOTAL_PAGES);

            List<MovieListResultItem> movies = getMovies(movieList);
            movieListResult = new MovieListResult(page, totalResults, totalPages, movies);
        } catch (JSONException e) {
            Log.e(TAG, "createMovieListResultFromJsonStr: ", e);
        }

        return movieListResult;
    }

    public static List<MovieListResultItem> getMovies(JSONObject movieResultJson) throws JSONException{
        JSONArray movieJsonArray = movieResultJson.getJSONArray(MovieListResultProperty.RESULTS);

        List<MovieListResultItem> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            JSONObject movieEntry = movieJsonArray.getJSONObject(i);
            movies.add(new MovieListResultItem(movieEntry));
        }

        return movies;
    }

    private class MovieListResultProperty {
        private static final String PAGE = "page";
        private static final String TOTAL_RESULTS = "total_results";
        private static final String TOTAL_PAGES = "total_pages";
        private static final String RESULTS = "results";
    }

}
