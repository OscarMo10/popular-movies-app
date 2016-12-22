package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.example.android.popularmovies.R.id.movieRuntimeTextView;
import static com.example.android.popularmovies.R.id.movieTitleTextView;
import static com.example.android.popularmovies.R.id.posterImageView;
import static com.example.android.popularmovies.R.id.userReviewTextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private TextView mMovieRuntimeTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseTextView;
    private TextView mMovieLengthTextView;
    private TextView mUserReviewTextView;
    private TextView mMovieOverviewTextView;
    private RecyclerView mRecyclerView;
    private MovieVideoAdapter mAdapter;

    public static final String MOVIE_ID_ARGUMENT = "movie";

    public DetailActivityFragment() {
    }

    public static DetailActivityFragment createFragment(int movieId) {
        DetailActivityFragment fragment = new DetailActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_ARGUMENT, movieId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_display, container, false);

        initChildViews(view);

        fetchMovieData();

        return view;
    }

    private void initChildViews(View view) {
        mMovieRuntimeTextView = (TextView) view.findViewById(movieTitleTextView);
        mPosterImageView = (ImageView) view.findViewById(posterImageView);
        mReleaseTextView = (TextView) view.findViewById(R.id.releaseDateTextView);
        mMovieLengthTextView = (TextView) view.findViewById(movieRuntimeTextView);
        mUserReviewTextView = (TextView) view.findViewById(userReviewTextView);
        mMovieOverviewTextView = (TextView) view.findViewById(R.id.movieOverviewTextView);

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movieVideoRecyclerView);

        // set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // add adapter
        List<MovieDatabaseAPI.MovieVideosResult> data = new ArrayList<>();
        mAdapter = new MovieVideoAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addResultsToAdapter(List<MovieDatabaseAPI.MovieVideosResult> data) {
        if (data != null && data.size() == 0) {
            Log.d(TAG, "addResultsToAdapter: Data is null or empty");    
        }
        
        if (mAdapter != null) {
            mAdapter.appendMovieVideos(data);
        }
        else {
            Log.d(TAG, "addResultsToAdapter: Adapter is null.");
        }
    }
    private void fetchMovieData() {
        int movieId = getArguments().getInt(MOVIE_ID_ARGUMENT, MovieDatabaseAPI.INVALID_MOVIE_ID);
        if (movieId == MovieDatabaseAPI.INVALID_MOVIE_ID) {
            return;
        }

        // fetch data. views will be populated by data in onPostExecute
        new FetchDetail().execute(movieId);
        new FetchTrailers().execute(movieId);
    }

    private void bindMovieDetail(MovieDatabaseAPI.MovieDetailResult movieDetailResult) {
        if (movieDetailResult == null) {
            Log.d(TAG, "bindMovieDetail: movieDetailResult is null. Views will not be populated");
            return;
        }

        // Set poster image
        String posterUrl = MovieDatabaseAPI.getPosterUrl(movieDetailResult.getPosterUrl());
        Picasso.with(getActivity()).load(posterUrl).into(mPosterImageView);

        // set rest of details
        String title = movieDetailResult.getTitle();
        mMovieRuntimeTextView.setText(movieDetailResult.getTitle());

        String releaseDate = movieDetailResult.getReleaseDate();
        String releaseYear = getYearFromDateString(releaseDate);
        mReleaseTextView.setText(releaseYear);

        int movieRuntime = movieDetailResult.getRuntime();
        String formattedMovieRuntimeStr = getFormattedMovieRuntime(movieRuntime);
        mMovieLengthTextView.setText(formattedMovieRuntimeStr);

        double voteAverage = movieDetailResult.getVoteAverage();
        String voteAverageStr = getFormattedVoteAverageString(voteAverage);
        mUserReviewTextView.setText(voteAverageStr);

        String overview = movieDetailResult.getOverView();
        mMovieOverviewTextView.setText(overview);
    }

    private String getFormattedMovieRuntime(int movieDuration) {
        String timeStr = Integer.toString(movieDuration);

        return timeStr + "min";
    }

    private String getYearFromDateString(String releaseDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
        Date date = null;
        try {
            date = formatter.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = formatter.getCalendar();
        int year = calendar.get(Calendar.YEAR);

        return Integer.toString(year);

    }

    private String getFormattedVoteAverageString(double userReview) {
        String reviewStr = Double.toString(userReview);

        return reviewStr + "/10";
    }

    private class FetchDetail extends AsyncTask<Integer, Void, MovieDatabaseAPI.MovieDetailResult> {


        @Override
        protected MovieDatabaseAPI.MovieDetailResult doInBackground(Integer... params) {
            int movieId = params[0];
            MovieDatabaseAPI.MovieDetailResult movieDetailResult = null;

            try {
                 movieDetailResult = MovieDatabaseAPI.getMovieDetail(movieId);
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: Error fetching movie details");
                e.printStackTrace();
            }

            return movieDetailResult;
        }

        @Override
        protected void onPostExecute(MovieDatabaseAPI.MovieDetailResult movieDetailResult) {
            bindMovieDetail(movieDetailResult);
        }
    }

    private class FetchTrailers extends AsyncTask<Integer, Void, List<MovieDatabaseAPI.MovieVideosResult>> {

        @Override
        protected List<MovieDatabaseAPI.MovieVideosResult> doInBackground(Integer... integers) {
            int movieId = integers[0];

            List<MovieDatabaseAPI.MovieVideosResult> result = null;
            try {
                result =
                        MovieDatabaseAPI.getVideosForMovie(movieId);
            }
            catch (IOException e) {
                Log.e(TAG, "doInBackground: Error fetching data for movie videos", e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<MovieDatabaseAPI.MovieVideosResult> data) {
            addResultsToAdapter(data);
        }
    }
}
