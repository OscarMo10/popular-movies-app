package com.example.android.popularmovies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by oscar on 1/16/17.
 */

public class FormatUtils {
    private static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy-mm-dd", Locale.US);

    private static String formatMovieRuntime(int movieDuration) {
        String timeStr = Integer.toString(movieDuration);

        return timeStr + "min";
    }

    private static String formatMovieReleaseDate(String releaseDate) {
        Date date = null;
        try {
            date = DATE_FORMATTER.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = DATE_FORMATTER.getCalendar();
        int year = calendar.get(Calendar.YEAR);

        return Integer.toString(year);
    }

    private static String formatVoteAverageString(double userReview) {
        String reviewStr = Double.toString(userReview);

        return reviewStr + "/10";
    }
}
