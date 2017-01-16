package com.example.android.popularmovies.data;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.Utils;

import org.json.JSONException;

import java.io.IOException;
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

        // MovieListResultItem Detail Endpoint
        static final String MOVIE_DETAIL_ENDPOINT_FORMAT = "/movie/%d";

        static final String MOVIE_VIDEOS_ENDPOINT_FORMAT = "/movie/%d/videos";
    }

    public static MovieListResult getPopularMovies(int page) throws IOException, JSONException {
        String urlString = Uri.parse(BASE_MOVIE_DB_API_URL + POPULAR_MOVIES_ENDPOINT).buildUpon()
                .appendQueryParameter(QUERY_PAGE, Integer.toString(page))
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()
                .toString();

        Log.d(TAG, "getPopularMovies: urlString = " + urlString);

        String jsonString = Utils.getURLString(urlString);
        MovieListResult movieListResult =
                MovieListResult.createMovieListResultFromJsonStr(jsonString);

        return movieListResult;
    }

    public MovieDetailInfo getDetailsForMovie(int movieId) throws IOException {
        String urlString = getUrlForMovieDetail(movieId);
        String jsonStr = Utils.getURLString(urlString);
        MovieDetailInfo movieDetailInfo = MovieDetailInfo.createMovieDetailFromJsonStr(jsonStr);

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

    public List<MovieVideoInfo> getVidesForMovie(int movieId) throws IOException{

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




}
