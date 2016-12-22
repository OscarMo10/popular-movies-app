package com.example.android.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.MovieDatabaseAPI.ENDPOINTS.POPULAR_MOVIES_ENDPOINT;

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

    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";
    private static final String YOUTUBE_VIDEO_QUERY_PARAM = "v";



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

    public static MovieDetailResult getMovieDetail(int movieId) throws IOException {

        String urlString = getUrlForMovieDetail(movieId);
        String jsonStr = Utils.getURLString(urlString);
        MovieDetailResult movieDetailResult = parseMovieDetailResult(jsonStr);

        return movieDetailResult;
    }

    private static String getUrlForMovieDetail(int movieId) {
        String movieDetailEndpoint = String.format(ENDPOINTS.MOVIE_DETAIL_ENDPOINT_FORMAT, movieId);
        String urlString = Uri.parse(BASE_MOVIE_DB_API_URL + movieDetailEndpoint).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()
                .toString();

        return urlString;
    }


    public static List<MovieVideosResult> getVideosForMovie(int movieId) throws IOException {
        String urlStr = getUrlForMovieVideosEndpoint(movieId);
        String jsonStr = Utils.getURLString(urlStr);
        List<MovieVideosResult> results = parseMovieVideosResponse(jsonStr);

        return results;
    }

    private static List<MovieVideosResult> parseMovieVideosResponse(String jsonStr) {
        List<MovieVideosResult> videos = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray results = jsonObject.getJSONArray(MovieVideosResponseProperty.RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject entry = results.getJSONObject(i);
                MovieVideosResult movieVideo = parseResultEntry(entry);
                videos.add(movieVideo);
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "parseMovieVideosResponse: Error parsing response from movie videos endpoint", e);
        }

        return videos;
    }

    private static MovieVideosResult parseResultEntry(JSONObject entry) throws JSONException{
        String id = entry.getString(MovieVideosResponseProperty.ID);
        String name = entry.getString(MovieVideosResponseProperty.NAME);
        String key = entry.getString(MovieVideosResponseProperty.KEY);

        String urlForVideo = createYoutubeUrlFromKey(key);

        return new MovieVideosResult(id, name, urlForVideo);
    }

    private static String createYoutubeUrlFromKey(String key) {
        String url = Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_VIDEO_QUERY_PARAM, key)
                .build()
                .toString();

        return url;
    }

    private static String getUrlForMovieVideosEndpoint(int movieId) {
        String endpointFormat = ENDPOINTS.MOVIE_VIDEOS_ENDPOINT_FORMAT;
        String movieVideosEnpoint = String.format(endpointFormat, movieId);

        return Uri.parse(BASE_MOVIE_DB_API_URL + movieVideosEnpoint).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()
                .toString();
    }

    private static MovieDetailResult parseMovieDetailResult(String jsonStr) {
        MovieDetailResult movieDetailResult = null;

        try {
            JSONObject result = new JSONObject(jsonStr);

            String title = result.getString(MovieDetailResponseProperty.TITLE);
            String posterPath = result.getString(MovieDetailResponseProperty.POSTER_PATH);
            String releaseDate = result.getString(MovieDetailResponseProperty.RELEASE_DATE);
            double voteAverage = result.getDouble(MovieDetailResponseProperty.VOTE_AVERAGE);
            int runtime = result.getInt(MovieDetailResponseProperty.RUNTIME);
            String overView = result.getString(MovieDetailResponseProperty.OVERVIEW);

            movieDetailResult = new MovieDetailResult(title, posterPath, releaseDate, voteAverage, runtime, overView);

        } catch (JSONException e) {
            Log.e(TAG, "parseMovieDetailResult: Error while paring json.", e);
        }

        return movieDetailResult;
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

    public static String getPosterUrl(String posterPath) {
        return new StringBuilder()
                .append(BASE_IMAGE_URL)
                .append('/')
                .append(FILE_SIZE)
                .append(posterPath)
                .toString();
    }

    public static class Movie implements Parcelable{
        private final int mId;
        private final String mPosterPath;
        private final String mReleaseDate;
        private final String mTitle;
        private final String mOverview;
        private final double mVoteAverage;

        public Movie(int id, String posterPath,
                     String releaseDate,
                     String title,
                     String overview,
                     double voteAverage) {
            mId = id;
            mPosterPath = posterPath;
            mReleaseDate = releaseDate;
            mTitle = title;
            mOverview = overview;
            mVoteAverage = voteAverage;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(mId);
            parcel.writeString(mPosterPath);
            parcel.writeString(mReleaseDate);
            parcel.writeString(mTitle);
            parcel.writeString(mOverview);
            parcel.writeDouble(mVoteAverage);
        }

        public static final Parcelable.Creator<Movie> CREATOR
                = new Parcelable.Creator<Movie>() {
            @Override
            public Movie createFromParcel(Parcel parcel) {
                int id = parcel.readInt();
                String posterPath = parcel.readString();
                String releaseDate = parcel.readString();
                String title = parcel.readString();
                String overview = parcel.readString();
                double voteAverage = parcel.readDouble();

                return new Movie(id, posterPath, releaseDate, title, overview, voteAverage);
            }

            @Override
            public Movie[] newArray(int i) {
                return new Movie[i];
            }
        };

        public static Movie getMovieFromJsonObject(JSONObject movieJsonObject) throws JSONException {
            int id = movieJsonObject.getInt(PopularMovieResponseProperty.ID);
            String posterPath = movieJsonObject.getString(PopularMovieResponseProperty.POSTER_PATH);
            String releaseDate = movieJsonObject.getString(PopularMovieResponseProperty.RELEASE_DATE);
            String title = movieJsonObject.getString(PopularMovieResponseProperty.TITLE);
            String overview = movieJsonObject.getString(PopularMovieResponseProperty.OVERVIEW);
            double voteAverage = movieJsonObject.getDouble(PopularMovieResponseProperty.VOTE_AVERAGE);

            return new Movie(id, posterPath, releaseDate, title, overview, voteAverage);
        }

        public int getId() {
            return mId;
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

    public static class MovieDetailResult {
        private String mTitle;
        private String mPosterUrl;
        private String mReleaseDate;
        private double mVoteAverage;
        private int mRuntime;
        private String mOverView;

        public MovieDetailResult(String title, String posterUrl, String releaseDate, double voteAverage, int runtime, String overView) {
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
    }

    public static class MovieVideosResult {
        private String mId;
        private String mName;
        private String mVideoUrl;

        public MovieVideosResult(String id, String name, String videoUrl) {
            mId = id;
            mName = name;
            mVideoUrl = videoUrl;
        }


        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public String getVideoUrl() {
            return mVideoUrl;
        }
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

    private static class MovieDetailResponseProperty {
        private static final String POSTER_PATH = "poster_path";
        private static final String TITLE = "title";
        private static final String OVERVIEW = "overview";
        private static final String RUNTIME = "runtime";
        private static final String VOTE_AVERAGE = "vote_average";
        private static final String RELEASE_DATE = "release_date";
    }

    private static class MovieVideosResponseProperty {
        private static final String RESULTS = "results";
        private static final String ID = "id";
        private static final String NAME = "name";
        private static final String KEY = "key";
    }
}
