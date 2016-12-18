package com.example.android.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 12/17/16.
 */

public class MovieDatabaseAPI {
    private static final String TAG = MovieDatabaseAPI.class.getSimpleName();

    private static final String BASE_API_URL = "https://api.themoviedb.org/3";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p";

    private static final String QUERY_API_KEY = "api_key";
    private static final String QUERY_PAGE = "page";
    private static final String QUERY_LANGUAGE = "language";

    private static final String FILE_SIZE = "w300";

    // public constants
    public static final String LANGUAGE_ENGLISH = "en-US";

    // Popular Movies Endpoint
    private static final String POPULAR_MOVIES_ENDPOINT = "/movie/popular";

    public static List<Movie> getPopularMovies(int page) throws IOException {
        String urlString = Uri.parse(BASE_API_URL + POPULAR_MOVIES_ENDPOINT).buildUpon()
                .appendQueryParameter(QUERY_PAGE, Integer.toString(page))
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()
                .toString();

        Log.d(TAG, "getPopularMovies: urlString = " + urlString);

        List<Movie> movies = null;
        String jsonString = Utils.getURLString(urlString);
        movies = parseMovieJson(jsonString);

        return movies;
    }

    private static List<Movie> parseMovieJson(String jsonString) {

        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray(MovieJsonProperites.RESULTS_PROPERTY);

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                movies.add(Movie.getMovieFromJsonObject(result));
            }

        } catch (JSONException e) {
            Log.e(TAG, "parseMovieJson: ", e);
        }


        return movies;
    }

    public static String getPosterUrl(String posterPath) {
        return new StringBuilder()
                .append(BASE_IMAGE_URL)
                .append('/')
                .append(FILE_SIZE)
                .append('/')
                .append(posterPath)
                .toString();
    }

    public static class Movie {
        private final String mPosterPath;
        private final String mReleaseDate;
        private final String mTitle;
        private final String mOverview;
        private final double mVoteAverage;

        public Movie(String posterPath, String releaseDate, String title, String overview, double voteAverage) {
            mPosterPath = posterPath;
            mReleaseDate = releaseDate;
            mTitle = title;
            mOverview = overview;
            mVoteAverage = voteAverage;
        }

        public static Movie getMovieFromJsonObject(JSONObject movieJsonObject) throws JSONException {
            String posterPath = movieJsonObject.getString(MovieJsonProperites.POSTER_PATH_PROPERTY);
            String releaseDate = movieJsonObject.getString(MovieJsonProperites.RELEASE_DATE_PROPERTY);
            String title = movieJsonObject.getString(MovieJsonProperites.TITLE_PROPERTY);
            String overview = movieJsonObject.getString(MovieJsonProperites.OVERVIEW_PROPERTY);
            double voteAverage = movieJsonObject.getDouble(MovieJsonProperites.VOTE_AVERAGE_PROPERTY);

            return new Movie(posterPath, releaseDate, title, overview, voteAverage);
        }

        public String getPosterPath() {
            return mPosterPath;
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
    }

    private static class MovieJsonProperites {
        final static String RESULTS_PROPERTY = "results";
        final static String POSTER_PATH_PROPERTY = "poster_path";
        final static String OVERVIEW_PROPERTY = "overview";
        final static String RELEASE_DATE_PROPERTY = "release_date";
        final static String TITLE_PROPERTY = "title";
        final static String VOTE_AVERAGE_PROPERTY = "vote_average";
    }
}
