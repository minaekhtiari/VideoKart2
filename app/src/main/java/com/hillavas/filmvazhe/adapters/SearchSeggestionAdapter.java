package com.hillavas.filmvazhe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by ArashJahani on 15/05/2015.
 */
public class SearchSeggestionAdapter extends BaseAdapter {

    List<String> seggestion;
    Context context;

    public SearchSeggestionAdapter(Context context, List<String> seggestion) {
        this.seggestion = seggestion;
        this.context = context;
    }

    @Override
    public int getCount() {
        return seggestion.size();
    }

    @Override
    public Object getItem(int position) {
        return seggestion.get(position);
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
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);

            holder = new ViewHolder();
            holder.lblTitle = (TextView) view.findViewById(android.R.id.text1);


            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();


        holder.lblTitle.setText(seggestion.get(position).toString());

        return view;
    }

    static class ViewHolder {
        TextView lblTitle;

    }
}
