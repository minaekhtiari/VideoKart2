package com.hillavas.filmvazhe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.model.Lesson;
import com.hillavas.filmvazhe.model.LessonStatus;

import java.util.List;

/**
 * Created by Arash on 16/05/17.
 */
public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Lesson item);
    }

    List<Lesson> lessonList;
    int[] borderColor;

    private final OnItemClickListener onItemClickListener;

    public LessonsAdapter(List<Lesson> lessonList, OnItemClickListener onItemClickListener) {
        this.lessonList = lessonList;
        this.onItemClickListener = onItemClickListener;
        borderColor = MyApplication.getContext().getResources().getIntArray(R.array.lesson_color);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lesson_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.bind(lessonList.get(position), onItemClickListener);

        holder.lblPostTitle.setText(lessonList.get(position).getName());


        if (lessonList.get(position).getStatus() == LessonStatus.Free) {

            holder.layoutProgress.setVisibility(View.VISIBLE);
            holder.imgLock.setVisibility(View.GONE);

        } else if (lessonList.get(position).getStatus() == LessonStatus.Premium) {

            holder.layoutProgress.setVisibility(View.GONE);
            holder.imgLock.setVisibility(View.VISIBLE);
        }

        holder.roundCornerProgressBar.setMax((lessonList.get(position).getWordsCount()));

        holder.roundCornerProgressBar.setProgress((lessonList.get(position).getStudiedCount()));

        holder.lblProgressState.setText(String.format("%d/%d", lessonList.get(position).getStudiedCount(), lessonList.get(position).getWordsCount()));


    }

    @Override
    public int getItemCount() {

        return lessonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblPostTitle, lblProgressState;
        FrameLayout leftBorder;
        RelativeLayout layoutProgress;
        ImageView imgLock;
        RoundCornerProgressBar roundCornerProgressBar;

        ViewHolder(View itemView) {
            super(itemView);

            lblPostTitle = (TextView) itemView.findViewById(R.id.lbl_title);
            lblProgressState = (TextView) itemView.findViewById(R.id.lbl_progress_state);

            layoutProgress = (RelativeLayout) itemView.findViewById(R.id.layout_progress);
            imgLock = (ImageView) itemView.findViewById(R.id.img_lock);
            roundCornerProgressBar = (RoundCornerProgressBar) itemView.findViewById(R.id.roundCornerProgressBar);

            //layout.getLayoutParams().height = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * (40.0f / 100.0f));
//            int width = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * (33.0f / 100.0f));
//            leftBorder.getLayoutParams().height = (int) (width * 1.5);


        }

        public void bind(final Lesson item, final OnItemClickListener listener) {

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
