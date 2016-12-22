package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by oscar on 12/21/16.
 */

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MovieVideoHolder> {
    private List<MovieDatabaseAPI.MovieVideosResult> mData;
    Context mContext;


    public MovieVideoAdapter(Context context, List<MovieDatabaseAPI.MovieVideosResult> data) {
        mContext = context;
        mData = data;
    }

    public void appendMovieVideos(List<MovieDatabaseAPI.MovieVideosResult> moreData) {
        if (moreData == null || moreData.isEmpty()) {
            return;
        }

        int positionStart = moreData.size();
        int itemCount = moreData.size();

        mData.addAll(moreData);
        this.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public MovieVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.list_item_video, parent, false);

        return new MovieVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieVideoHolder holder, int position) {
        MovieDatabaseAPI.MovieVideosResult movieVideo = mData.get(position);
        holder.bindMovieVideo(movieVideo);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MovieVideoHolder extends RecyclerView.ViewHolder {
        private TextView videoNameTextView;

        public MovieVideoHolder(View itemView) {
            super(itemView);
            initChildViews(itemView);
        }

        public void bindMovieVideo(final MovieDatabaseAPI.MovieVideosResult movieVideo) {
            String name = movieVideo.getName();
            videoNameTextView.setText(name);

            // set onClickListener
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String videoUrl = movieVideo.getVideoUrl();
                    Uri videoUri = Uri.parse(videoUrl);
                    intent.setData(videoUri);

                    mContext.startActivity(intent);
                }
            });
        }

        private void initChildViews(View view) {
            videoNameTextView = (TextView) view.findViewById(R.id.videoNameTextView);
        }

    }
}
