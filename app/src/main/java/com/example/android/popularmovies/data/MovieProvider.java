package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.android.popularmovies.data.MovieContract.*;

/**
 * Created by oscar on 12/31/16.
 */

public class MovieProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIE = 100;



    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_MOVIE, MOVIE);

        return matcher;
    }



    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                retCursor = getMovies();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getMovies() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        return db.query(
                MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        String type;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                type =  MovieEntry.CONTENT_TYPE;
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                id = insertMovie(contentValues);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri " + uri);
            }
        }

        return MovieEntry.buildMovieUri(id);
    }

    private long insertMovie(ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        return db.insert(MovieEntry.TABLE_NAME, null, values);
    }
    @Override
    public int delete(Uri uri, String selection, String[] strings) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;


        selection = (selection == null) ? "1" : selection;

        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, selection, null);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri " + uri);
            }
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
