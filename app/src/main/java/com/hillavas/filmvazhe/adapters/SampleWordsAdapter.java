package com.hillavas.filmvazhe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;


import com.hillavas.filmvazhe.model.Clip;
import com.hillavas.filmvazhe.R;


/**
 * Created by ArashJahani on 15/05/2015.
 */
public class SampleWordsAdapter extends BaseAdapter {

    List<Clip> postList;
Context context;

    public SampleWordsAdapter(Context context,List<Clip> postList) {
        this.postList = postList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.layout_sample_word_item, null);

            holder = new ViewHolder();

            holder.lblCounter = (TextView) view.findViewById(R.id.lbl_counter);
            holder.lblTitle = (TextView) view.findViewById(R.id.lbl_title);
            holder.lblText = (TextView) view.findViewById(R.id.lbl_text);
            holder.lblLikeCount = (TextView) view.findViewById(R.id.lbl_like);
            holder.lblMovieName = (TextView) view.findViewById(R.id.lbl_movie_name);

            view.setTag(holder);
        }
       holder = (ViewHolder) view.getTag();

        holder.lblCounter.setText(String.valueOf(position+1)+".");

        holder.lblText.setText( postList.get(position).getdialogText());

        holder.lblLikeCount.setText(String.valueOf(postList.get(position).getLikeCount()));

        holder.lblMovieName.setText(postList.get(position).getMovieName());

        return view;
    }

    static class ViewHolder {
        TextView lblCounter;
        TextView lblTitle;
        TextView lblText;
        TextView lblLikeCount;
        TextView lblMovieName;
    }
}
