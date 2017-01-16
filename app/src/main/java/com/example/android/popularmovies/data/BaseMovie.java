package com.example.android.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oscar on 1/15/17.
 */

class BaseMovie {
    private final int mId;
    private String mPosterUrl;
    private final String mReleaseDate;
    private final String mTitle;
    private final String mOverview;
    private final double mVoteAverage;

    public BaseMovie(int id, String posterPath, String releaseDate, String title, String overview, double voteAverage) {
        mId = id;
        mReleaseDate = releaseDate;
        mTitle = title;
        mOverview = overview;
        mVoteAverage = voteAverage;

        setPosterUrl(posterPath);
    }

    public BaseMovie(JSONObject movieJson) throws JSONException {
        mId = movieJson.getInt(BaseMovieProperty.ID);
        mReleaseDate = movieJson.getString(BaseMovieProperty.RELEASE_DATE);
        mTitle = movieJson.getString(BaseMovieProperty.TITLE);
        mOverview = movieJson.getString(BaseMovieProperty.OVERVIEW);
        mVoteAverage = movieJson.getDouble(BaseMovieProperty.VOTE_AVERAGE);

        setPosterUrl(movieJson.getString(BaseMovieProperty.POSTER_PATH));
    }

    private void setPosterUrl(String posterPath) {
        mPosterUrl = MovieDatabaseAPI.getPosterUrl(posterPath);
    }

    public int getId() {
        return mId;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    private static class BaseMovieProperty {
        final static String ID = "id";
        final static String RESULTS = "results";
        final static String POSTER_PATH = "poster_path";
        final static String OVERVIEW = "overview";
        final static String RELEASE_DATE = "release_date";
        final static String TITLE = "title";
        final static String VOTE_AVERAGE = "vote_average";
    }
}
