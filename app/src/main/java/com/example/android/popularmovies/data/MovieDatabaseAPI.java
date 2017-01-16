package com.example.android.popularmovies.data;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.data.MovieDatabaseAPI.ENDPOINTS.POPULAR_MOVIES_ENDPOINT;
import static com.example.android.popularmovies.data.MovieVideoInfo.parseMovieVideosResponse;

/**
 * Created by oscar on 12/17/16.
 */

public class MovieDatabaseAPI {
    private static final String TAG = MovieDatabaseAPI.class.getSimpleName();

    private static final String BASE_MOVIE_DB_API_URL = "https://api.themoviedb.org/3";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p";

    private static final String QUERY_API_KEY = "api_key";
    private static final String QUERY_PAGE = "page";
    private static final String QUERY_LANGUAGE = "language";

    private static final String FILE_SIZE = "w300";

    // public constants for MOVIE DB API
    public static final String LANGUAGE_ENGLISH = "en-US";
    public static final int INVALID_MOVIE_ID = -1;





    static class ENDPOINTS {
        // Popular Movies Endpoint
        static final String POPULAR_MOVIES_ENDPOINT = "/movie/popular";

        // Movie Detail Endpoint
        static final String MOVIE_DETAIL_ENDPOINT_FORMAT = "/movie/%d";

        static final String MOVIE_VIDEOS_ENDPOINT_FORMAT = "/movie/%d/videos";
    }

    public static List<Movie> getPopularMovies(int page) throws IOException {
        String urlString = Uri.parse(BASE_MOVIE_DB_API_URL + POPULAR_MOVIES_ENDPOINT).buildUpon()
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
            JSONArray results = jsonObject.getJSONArray(PopularMovieResponseProperty.RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                movies.add(Movie.getMovieFromJsonObject(result));
            }

        } catch (JSONException e) {
            Log.e(TAG, "parseMovieJson: ", e);
        }


        return movies;
    }

    public MovieDetailInfo getMovieDetail(int movieId) throws IOException {
        String urlString = getUrlForMovieDetail(movieId);
        String jsonStr = Utils.getURLString(urlString);
        MovieDetailInfo movieDetailInfo = MovieDetailInfo.parseMovieDetailResponse(jsonStr);

        return movieDetailInfo;
    }

    private static String getUrlForMovieDetail(int movieId) {
        String movieDetailEndpoint = String.format(ENDPOINTS.MOVIE_DETAIL_ENDPOINT_FORMAT, movieId);
        String urlString = Uri.parse(BASE_MOVIE_DB_API_URL + movieDetailEndpoint).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()
                .toString();

        return urlString;
    }

    public List<MovieVideoInfo> getVideosInfo(int movieId) throws IOException{

        String urlStr = getUrlForMovieVideosEndpoint(movieId);
        String jsonStr = Utils.getURLString(urlStr);
        List<MovieVideoInfo> results = parseMovieVideosResponse(jsonStr);

        return results;
    }

    private static String getUrlForMovieVideosEndpoint(int movieId) {
        String endpointFormat = ENDPOINTS.MOVIE_VIDEOS_ENDPOINT_FORMAT;
        String movieVideosEnpoint = String.format(endpointFormat, movieId);

        return Uri.parse(BASE_MOVIE_DB_API_URL + movieVideosEnpoint).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()
                .toString();
    }

    public static String getPosterUrl(String posterPath) {
        return new StringBuilder()
                .append(BASE_IMAGE_URL)
                .append('/')
                .append(FILE_SIZE)
                .append(posterPath)
                .toString();
    }



    private static class PopularMovieResponseProperty {
        final static String ID = "id";
        final static String RESULTS = "results";
        final static String POSTER_PATH = "poster_path";
        final static String OVERVIEW = "overview";
        final static String RELEASE_DATE = "release_date";
        final static String TITLE = "title";
        final static String VOTE_AVERAGE = "vote_average";
    }
}
