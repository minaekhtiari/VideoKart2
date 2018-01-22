package com.hillavas.filmvazhe.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.hillavas.filmvazhe.api.VideoCardApi;
import com.hillavas.filmvazhe.model.Movie;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Arash on 16/05/17.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Movie item);
    }

    List<Movie> movieList;
    private final MoviesAdapter.OnItemClickListener onItemClickListener;

    public MoviesAdapter(List<Movie> movieList, MoviesAdapter.OnItemClickListener onItemClickListener) {
        this.movieList = movieList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_item, null);
        MoviesAdapter.ViewHolder pvh = new MoviesAdapter.ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, final int position) {

        holder.bind(movieList.get(position), onItemClickListener);


        Glide.with(MyApplication.getContext())
                .load(String.format(VideoCardApi.imagesPath, String.valueOf(movieList.get(position).getId())))
                .into(holder.imgPlace);


    }

    @Override
    public int getItemCount() {

        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView carView;
        RoundedImageView imgPlace;
        TextView lblTitle;

        ViewHolder(View itemView) {
            super(itemView);
            carView = (CardView) itemView.findViewById(R.id.card_view);
            imgPlace = (RoundedImageView) itemView.findViewById(R.id.img_cover);
            lblTitle = (TextView) itemView.findViewById(R.id.lbl_title);


            int width = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * (33.0f / 100.0f));
            carView.getLayoutParams().height = (int) (width * 1.5);

        }

        public void bind(final Movie item, final MoviesAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

