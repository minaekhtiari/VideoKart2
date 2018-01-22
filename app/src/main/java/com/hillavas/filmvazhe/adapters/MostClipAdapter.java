package com.hillavas.filmvazhe.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.api.VideoCardApi;
import com.hillavas.filmvazhe.model.Clip;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Arash on 16/05/17.
 */
public class MostClipAdapter extends RecyclerView.Adapter<MostClipAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Clip item);
    }

    List<Clip> clipList;
    private final OnItemClickListener onItemClickListener;

    public MostClipAdapter(List<Clip> clipList, OnItemClickListener onItemClickListener) {
        this.clipList = clipList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_most_clip_item, null);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.bind(clipList.get(position), onItemClickListener);

        holder.lblWord.setText(clipList.get(position).getWord().getName());
        holder.lblMovieName.setText(clipList.get(position).getMovieName());

        Glide.with(MyApplication.getContext())
                .load(String.format(VideoCardApi.clipCover, String.valueOf(clipList.get(position).getId())))
                .into(holder.imgCover)
        ;
//        if(position==0){
//            holder.imgCover.setImageResource(R.drawable.book1);
//        }else{
//            holder.imgCover.setImageResource(R.drawable.book2);
//
//        }


    }

    @Override
    public int getItemCount() {

        return clipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout;
        TextView lblWord;
        TextView lblMovieName;

        ImageView imgCover;

        ViewHolder(View itemView) {
            super(itemView);
            layout = (CardView) itemView.findViewById(R.id.card_view);
            lblWord = (TextView) itemView.findViewById(R.id.lbl_word);
            lblMovieName = (TextView) itemView.findViewById(R.id.lbl_movie_name);

            imgCover = (ImageView) itemView.findViewById(R.id.img_clip);

            layout.getLayoutParams().height = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * (20.0f / 100.0f));
            layout.getLayoutParams().width = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * (45.0f / 100.0f));
//            int width = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * (33.0f / 100.0f));
//            leftBorder.getLayoutParams().height = (int) (width * 1.5);


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
