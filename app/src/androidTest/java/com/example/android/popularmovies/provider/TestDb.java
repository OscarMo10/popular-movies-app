package com.example.android.popularmovies.provider;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static com.example.android.popularmovies.provider.MovieContract.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by oscar on 12/31/16.
 */

@RunWith(AndroidJUnit4.class)
public class TestDb {

    private Context mContext;

    @Before
    public void initTargetContext() {
        mContext = InstrumentationRegistry.getTargetContext();
        assertThat(mContext, notNullValue());
    }

    @Test
    public void testCreateDb() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();

        assertThat(db.isOpen(), is(true));

        // Check that table was created
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertThat("Error: database has not been created correctly", c.moveToFirst(), is(true));

        // does table contain correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieEntry.TABLE_NAME + ")",
                null);

        assertThat("Error: Unable to query the database for table info.", c.moveToFirst(), is(true));

        // Build a Hashset of all the column name we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(MovieEntry._ID);
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MovieEntry.COLUMN_VOTE_AVERAGE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertThat(movieColumnHashSet, hasSize(0));

        db.close();
    }

    @Test
    public void testMovieTable() {
        insertMovie();
    }

    public long insertMovie() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieEntryContentValues();

        long movieRowId;
        movieRowId = db.insert(MovieEntry.TABLE_NAME, null, testValues);
        assertThat("Error: failure to insert movie entry.", movieRowId, is(not(-1L)));

        // Query database to very data
        Cursor cursor = db.query(
                MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // check if we got records back from query
        assertThat("Error: No records returned from movie query.", cursor.moveToFirst(), is(true));

        TestUtilities.validateCurrentRecord("Error: Movie Entry Query Validation Failed",
                cursor, testValues);

        // Move cursor to make sure that there is only one record in the database
        assertThat("Error: More than one record returned from movie query",
                cursor.moveToNext(), is(false));

        // close cursor and database
        cursor.close();
        db.close();

        return movieRowId;
    }
}
