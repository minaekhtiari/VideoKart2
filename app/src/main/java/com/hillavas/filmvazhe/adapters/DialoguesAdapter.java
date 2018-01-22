package com.hillavas.filmvazhe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hillavas.filmvazhe.utils.StringUtils;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.model.Clip;

import java.util.List;

/**
 * Created by Arash on 16/05/17.
 */
public class DialoguesAdapter extends RecyclerView.Adapter<DialoguesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Clip item);
    }

    List<Clip> movieList;
    private final OnItemClickListener onItemClickListener;

    public DialoguesAdapter(List<Clip> posts, OnItemClickListener onItemClickListener) {
        this.movieList = posts;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dialogue_item, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.bind(movieList.get(position), onItemClickListener);

        holder.lblTitle.setText(StringUtils.capitalizeFirstLetter(movieList.get(position).getWordName()));
        holder.lblText.setText(String.format("\"%s\"", movieList.get(position).getdialogText()));

        holder.lblLikeCount.setText(String.valueOf(movieList.get(position).getLikeCount()));

        holder.lblLikeCount.setText(String.valueOf(movieList.get(position).getLikeCount()));

        holder.lblDuration.setText(String.format("%02d:%02d", (movieList.get(position).getDuration() / 1000) / 60, (movieList.get(position).getDuration() / 1000) % 60));

        holder.lblViewCount.setText(String.valueOf(movieList.get(position).getViewCount()));

        if (movieList.get(position).getStudied())
            holder.imgStudied.setVisibility(View.VISIBLE);
        else
            holder.imgStudied.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblTitle;
        TextView lblText;
        RelativeLayout layoutMovieName;
        ImageView imgStudied;
        ImageView imgBookmard;
        TextView lblLikeCount, lblViewCount;
        TextView lblDuration;


        ViewHolder(View itemView) {
            super(itemView);
            layoutMovieName = (RelativeLayout) itemView.findViewById(R.id.layout_movie_name);
            lblTitle = (TextView) itemView.findViewById(R.id.lbl_title);
            lblText = (TextView) itemView.findViewById(R.id.lbl_text);
            imgBookmard = (ImageView) itemView.findViewById(R.id.img_bookmark);
            imgStudied = (ImageView) itemView.findViewById(R.id.img_studied);
            lblLikeCount = (TextView) itemView.findViewById(R.id.lbl_like);
            lblViewCount = (TextView) itemView.findViewById(R.id.lbl_view_count);
            lblDuration = (TextView) itemView.findViewById(R.id.lbl_time);


            layoutMovieName.setVisibility(View.INVISIBLE);
            // int height= (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * (20.0f / 100.0f));
            // leftBorder.getLayoutParams().height = height;
        }

        public void bind(final Clip item, final OnItemClickListener listener) {

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
