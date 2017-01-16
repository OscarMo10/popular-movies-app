package com.example.android.popularmovies.data;

import android.net.Uri;
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

public final class MovieVideoInfo {
    private String mId;
    private String mName;
    private String mVideoUrl;

    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";
    private static final String YOUTUBE_VIDEO_QUERY_PARAM = "v";

    public MovieVideoInfo(String id, String name, String videoUrl) {
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

    private static MovieVideoInfo createMovieVideoInfo(JSONObject entry) throws JSONException {
        String id = entry.getString(MovieVideosResponseProperty.ID);
        String name = entry.getString(MovieVideosResponseProperty.NAME);
        String key = entry.getString(MovieVideosResponseProperty.KEY);

        String urlForVideo = createYoutubeUrlFromKey(key);

        return new MovieVideoInfo(id, name, urlForVideo);
    }

    private static String createYoutubeUrlFromKey(String key) {
        String url = Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_VIDEO_QUERY_PARAM, key)
                .build()
                .toString();

        return url;
    }

    protected static List<MovieVideoInfo> parseMovieVideosResponse(String jsonStr) {
        List<MovieVideoInfo> videos = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray results = jsonObject.getJSONArray(MovieVideosResponseProperty.RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject entry = results.getJSONObject(i);

                MovieVideoInfo movieVideo = MovieVideoInfo.createMovieVideoInfo(entry);
                videos.add(movieVideo);
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "parseMovieVideosResponse: Error parsing response from movie videos endpoint", e);
        }

        return videos;
    }


    private static class MovieVideosResponseProperty {
        private static final String RESULTS = "results";
        private static final String ID = "id";
        private static final String NAME = "name";
        private static final String KEY = "key";
    }
}
