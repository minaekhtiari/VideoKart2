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

import com.hillavas.filmvazhe.api.VideoCardApi;
import com.hillavas.filmvazhe.model.Book;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Arash on 16/05/17.
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Book item);
    }

    List<Book> bookList;
    private final OnItemClickListener onItemClickListener;

    public BooksAdapter(List<Book> bookList, OnItemClickListener onItemClickListener) {
        this.bookList = bookList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_item, null);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.bind(bookList.get(position), onItemClickListener);

//        holder.lblCount.setText(String.valueOf(lessonList.get(position).getClipCount()));
//

        Glide.with(MyApplication.getContext())
                .load(String.format(VideoCardApi.bookCover, String.valueOf(bookList.get(position).getId())))
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

        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout;
        TextView lblText;
        TextView lblCount;
        TextView lblWordState;
        TextView lblDialogueState;
        ImageView imgCover;

        ViewHolder(View itemView) {
            super(itemView);
            layout = (CardView) itemView.findViewById(R.id.card_view);

//            lblCount = (TextView) itemView.findViewById(R.id.lbl_count);
            //lblDate = (TextView) itemView.findViewById(R.id.lbl_title);
//            lblWordState = (TextView) itemView.findViewById(R.id.lbl_word_State);
//            lblDialogueState = (TextView) itemView.findViewById(R.id.lbl_dialouge_state);
//
            imgCover = (ImageView) itemView.findViewById(R.id.img_book);

            layout.getLayoutParams().height = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * (35.0f / 100.0f));
            layout.getLayoutParams().width = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * (100.0f / 100.0f));
//            int width = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * (33.0f / 100.0f));
//            leftBorder.getLayoutParams().height = (int) (width * 1.5);


        }

        public void bind(final Book item, final OnItemClickListener listener) {

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
