package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.example.android.popularmovies.data.MovieContract.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by oscar on 1/1/17.
 */
public class TestUtilities {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertThat("Empty cursor return. " + error,
                valueCursor.moveToFirst(), is(true));
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertThat("Column " + columnName + " not found", idx, is(not(-1)));

            if (entry.getValue() instanceof Double) {
                double expectedValue = valueCursor.getDouble(idx);
                assertThat((Double) entry.getValue(), is(expectedValue));
            }
            else {
                String expectedValue = entry.getValue().toString();
                String actualValue = valueCursor.getString(idx);
                assertThat("Value " + actualValue + " did not match " +
                        "expected value " + expectedValue, actualValue, is(expectedValue));
            }

        }
    }

    static ContentValues createMovieEntryContentValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieEntry.COLUMN_MOVIE_ID, 5000);
        testValues.put(MovieEntry.COLUMN_OVERVIEW, "A man goes to Nebraska.");
        testValues.put(MovieEntry.COLUMN_RELEASE_DATE, "2004");
        testValues.put(MovieEntry.COLUMN_RUNTIME, 100);
        testValues.put(MovieEntry.COLUMN_TITLE, "Nebraska Man");
        testValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, 9.0d);

        return testValues;
    }

    static long insertMovieEntryValues(Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = createMovieEntryContentValues();

        long movieRowId;
        movieRowId = db.insert(MovieEntry.TABLE_NAME, null, testValues);

        // verify we got row back
        assertThat("Error: failure to insert movie values.", movieRowId, is(not(-1L)));

        return movieRowId;
    }
}
