package com.example.android.popularmovies.data;

import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmovies.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.example.android.popularmovies.data.MovieVideoInfo.parseMovieVideosResponse;

/**
 * Created by oscar on 1/15/17.
 */

public class Movie implements Parcelable {
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
        int id = movieJsonObject.getInt(MovieDatabaseAPI.PopularMovieResponseProperty.ID);
        String posterPath = movieJsonObject.getString(MovieDatabaseAPI.PopularMovieResponseProperty.POSTER_PATH);
        String releaseDate = movieJsonObject.getString(MovieDatabaseAPI.PopularMovieResponseProperty.RELEASE_DATE);
        String title = movieJsonObject.getString(MovieDatabaseAPI.PopularMovieResponseProperty.TITLE);
        String overview = movieJsonObject.getString(MovieDatabaseAPI.PopularMovieResponseProperty.OVERVIEW);
        double voteAverage = movieJsonObject.getDouble(MovieDatabaseAPI.PopularMovieResponseProperty.VOTE_AVERAGE);

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