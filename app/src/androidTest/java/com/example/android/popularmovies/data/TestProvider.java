package com.example.android.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.example.android.popularmovies.data.MovieContract.MovieEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by oscar on 1/9/17.
 */
@RunWith(JUnit4.class)
public class TestProvider {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    private Context mContext;

    @Before
    public void initTargetContext() {
        mContext = InstrumentationRegistry.getTargetContext();
        deleteAllRecordsFromProvider();
    }

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieEntry.Content_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.Content_URI,
                null,
                null,
                null,
                null
        );

        assertThat(
                "Error: Records not deleleted form Movie table during delete",
                cursor.getCount(),
                is(0)
        );
        cursor.close();
    }

    @Test
    public void testProviderRegistery() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName  componentName = new ComponentName(
                mContext.getPackageName(),
                MovieProvider.class.getName()
        );

        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertThat(
                    "Error: MovieProvider registered with authority: " + providerInfo.authority +
                            "instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority,
                    is(MovieContract.CONTENT_AUTHORITY)
            );
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue(
                    "Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false
            );
        }
    }

    @Test
    public void testGetType() {
        String type = mContext.getContentResolver().getType(MovieEntry.Content_URI);
        assertThat(
                "Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                type,
                is(MovieEntry.CONTENT_TYPE)
        );
    }

    @Test
    public void testBasicMovieQuery() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createMovieEntryContentValues();
        long movieRowId = db.insert(
                MovieEntry.TABLE_NAME,
                null,
                contentValues
        );

        assertThat(
                "Error: Unable to insert MovieEntry into database",
                movieRowId,
                is(not(-1L))
        );

        db.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.Content_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor(
                "testBasicMovieQuery",
                cursor,
                contentValues
        );


    }
}
