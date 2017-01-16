package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oscar on 1/15/17.
 */

public class MovieListResultItem extends BaseMovie implements Parcelable {
    public MovieListResultItem(JSONObject movieJson) throws JSONException {
        super(movieJson);
    }

    public MovieListResultItem(int id, String posterPath, String releaseDate, String title, String overview, double voteAverage) {
        super(id, posterPath, releaseDate, title, overview, voteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getPosterUrl());
        parcel.writeString(getReleaseDate());
        parcel.writeString(getTitle());
        parcel.writeString(getOverview());
        parcel.writeDouble(getVoteAverage());
    }

    public static final Parcelable.Creator<MovieListResultItem> CREATOR
            = new Parcelable.Creator<MovieListResultItem>()  {
        @Override
        public MovieListResultItem createFromParcel(Parcel parcel) {
            int id = parcel.readInt();
            String posterPath = parcel.readString();
            String releaseDate = parcel.readString();
            String title = parcel.readString();
            String overview = parcel.readString();
            double voteAverage = parcel.readDouble();

            return new MovieListResultItem(id, posterPath, releaseDate, title, overview, voteAverage);
        }

        @Override
        public MovieListResultItem[] newArray(int i) {
            return new MovieListResultItem[i];
        }
    };
}