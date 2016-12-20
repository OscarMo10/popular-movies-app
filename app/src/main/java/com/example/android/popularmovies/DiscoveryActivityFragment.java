package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscoveryActivityFragment extends Fragment {
    private MoviePosterAdapter mMoviePosterAdapter;

    public DiscoveryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.discovery_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);

        // set span count for layout manager based on poster image size
        setSpanForGridLayoutManager(layoutManager);

        recyclerView.setLayoutManager(layoutManager);

        // set adapter
        mMoviePosterAdapter = new MoviePosterAdapter(null);
        recyclerView.setAdapter(mMoviePosterAdapter);

        new FetchMovies().execute();

        return rootView;
    }

    private void setSpanForGridLayoutManager(GridLayoutManager layoutManager) {
        // get width of device in pixels
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        int span = Math.max(1, width / 281);

        layoutManager.setSpanCount(span);
    }

    private class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterHolder> {
        private List<MovieDatabaseAPI.Movie> mData;

        public MoviePosterAdapter(List<MovieDatabaseAPI.Movie> data) {
            if(data != null)
            {
                mData = data;
            }
            else {
                mData = new ArrayList<>();
            }

        }

        public void addMovies(List<MovieDatabaseAPI.Movie> moreMovies) {
            if (moreMovies == null || moreMovies.size() == 0) {
                return;
            }

            int startIndex = mData.size();
            mData.addAll(moreMovies);

            // notify dataset has changed
            this.notifyItemRangeChanged(startIndex, moreMovies.size());

        }

        @Override
        public MoviePosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_movie, parent, false);
            return new MoviePosterHolder(view);
        }

        @Override
        public void onBindViewHolder(MoviePosterHolder holder, int position) {
            MovieDatabaseAPI.Movie movie = mData.get(position);
            holder.bindMovie(movie);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class MoviePosterHolder extends RecyclerView.ViewHolder {
        public ImageView mMoviePosterImageView;

        public MoviePosterHolder(View view) {
            super(view);
            mMoviePosterImageView = (ImageView) view;
        }

        public void bindMovie(MovieDatabaseAPI.Movie movie) {
            String posterPathUrl = MovieDatabaseAPI.getPosterUrl(movie.getPosterPath());
            Log.d(TAG, "bindMovie: posterURL: " + posterPathUrl);
            Picasso.with(getActivity()).load(posterPathUrl).into(mMoviePosterImageView);
        }
    }

    private class FetchMovies extends AsyncTask<Void, Void, List<MovieDatabaseAPI.Movie>> {

        @Override
        protected List<MovieDatabaseAPI.Movie> doInBackground(Void... voids) {
            int popularMoviePage = 1;
            List<MovieDatabaseAPI.Movie> movies = null;

            try {
                 movies = MovieDatabaseAPI.getPopularMovies(popularMoviePage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<MovieDatabaseAPI.Movie> movies) {
            if (movies == null) {
                Log.d(TAG, "onPostExecute: Movies list was null");
                Toast.makeText(getActivity(), "Error retrieving data from network.", Toast.LENGTH_LONG).show();
                return;
            }

            mMoviePosterAdapter.addMovies(movies);
        }
    }

    public interface MovieChosenListener {
        public void onMovieChosen(MovieDatabaseAPI.Movie movie);
    }
}
