package com.example.android.popularmovies.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by oscar on 1/15/17.
 */

public class MovieDetailInfo extends BaseMovie {
    private int mRuntime;

    private MovieDetailInfo(JSONObject movieDetailJson) throws JSONException{
        super(movieDetailJson);
        init(movieDetailJson);
    }

    private void init(JSONObject movieDetailJson) throws JSONException {
        mRuntime = movieDetailJson.getInt(MovieDetailJSONProperty.RUNTIME);
    }

    public int getRuntime() {
        return mRuntime;
    }


    protected static MovieDetailInfo createMovieDetailFromJsonStr(String jsonStr) {
        MovieDetailInfo movieDetailInfo = null;

        try {
            JSONObject result = new JSONObject(jsonStr);
            movieDetailInfo = new MovieDetailInfo(result);
        } catch (JSONException e) {
            Log.e(TAG, "parseMovieDetailResponse: Error while paring json.", e);
        }

        return movieDetailInfo;
    }

    private static class MovieDetailJSONProperty {
        private static final String RUNTIME = "runtime";
    }


}
