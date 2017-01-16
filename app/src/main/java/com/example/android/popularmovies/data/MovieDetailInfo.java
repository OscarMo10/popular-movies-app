package com.example.android.popularmovies.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by oscar on 1/15/17.
 */

public class MovieDetailInfo {
    private String mTitle;
    private String mPosterUrl;
    private String mReleaseDate;
    private double mVoteAverage;
    private int mRuntime;
    private String mOverView;

    public MovieDetailInfo(String title, String posterUrl, String releaseDate, double voteAverage, int runtime, String overView) {
        mTitle = title;
        mPosterUrl = posterUrl;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mRuntime = runtime;
        mOverView = overView;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public int getRuntime() {
        return mRuntime;
    }

    public String getOverView() {
        return mOverView;
    }

    protected static MovieDetailInfo parseMovieDetailResponse(String jsonStr) {
        MovieDetailInfo movieDetailInfo = null;

        try {
            JSONObject result = new JSONObject(jsonStr);

            String title = result.getString(MovieDetailResponseProperty.TITLE);
            String posterPath = result.getString(MovieDetailResponseProperty.POSTER_PATH);
            String releaseDate = result.getString(MovieDetailResponseProperty.RELEASE_DATE);
            double voteAverage = result.getDouble(MovieDetailResponseProperty.VOTE_AVERAGE);
            int runtime = result.getInt(MovieDetailResponseProperty.RUNTIME);
            String overView = result.getString(MovieDetailResponseProperty.OVERVIEW);

            movieDetailInfo = new MovieDetailInfo(title, posterPath, releaseDate, voteAverage, runtime, overView);

        } catch (JSONException e) {
            Log.e(TAG, "parseMovieDetailResponse: Error while paring json.", e);
        }

        return movieDetailInfo;
    }

    private static class MovieDetailResponseProperty {
        private static final String POSTER_PATH = "poster_path";
        private static final String TITLE = "title";
        private static final String OVERVIEW = "overview";
        private static final String RUNTIME = "runtime";
        private static final String VOTE_AVERAGE = "vote_average";
        private static final String RELEASE_DATE = "release_date";
    }


}
