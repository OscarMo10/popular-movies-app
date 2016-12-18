package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscoveryActivityFragment extends Fragment {

    public DiscoveryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.discovery_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return rootView;
    }

    private class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterHolder> {
        @Override
        public MoviePosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_movie, parent, false);
            return new MoviePosterHolder(view);
        }

        @Override
        public void onBindViewHolder(MoviePosterHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class MoviePosterHolder extends RecyclerView.ViewHolder {
        public ImageView mMoviePosterImageView;

        public MoviePosterHolder(View view) {
            super(view);
            mMoviePosterImageView = (ImageView) view;
        }
    }
}
