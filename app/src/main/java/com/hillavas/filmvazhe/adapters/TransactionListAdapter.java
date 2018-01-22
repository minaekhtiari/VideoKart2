package com.hillavas.filmvazhe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.model.Transaction;
import com.hillavas.filmvazhe.utils.PersianDateTime;

import org.joda.time.DateTime;

import java.util.List;


/**
 * Created by ArashJahani on 15/05/2015.
 */
public class TransactionListAdapter extends BaseAdapter {

    List<Transaction> transactionList;
    Context context;

    public TransactionListAdapter(Context context, List<Transaction> transactionList) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return transactionList.size();
    }

    @Override
    public Object getItem(int position) {
        return transactionList.get(position);
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
            view = inflater.inflate(R.layout.item_transaction_list, null);

            holder = new ViewHolder();

            holder.lblCounter = (TextView) view.findViewById(R.id.lbl_counter);
            holder.lblTitle = (TextView) view.findViewById(R.id.lbl_title);
            holder.lblDate = (TextView) view.findViewById(R.id.lbl_date);


            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();

        holder.lblCounter.setText("." + String.valueOf(position + 1));


        holder.lblTitle.setText((transactionList.get(position).getActivation()) ? "فعال سازی" : "غیرفعال سازی");
        holder.lblTitle.setTextColor((transactionList.get(position).getActivation()) ? Color.parseColor("#4CAF50") : Color.parseColor("#FF5722"));

        DateTime dateTime = DateTime.parse(transactionList.get(position).getDate());
        holder.lblDate.setText(PersianDateTime.valueOf(dateTime).toDateString() + " - " + PersianDateTime.valueOf(dateTime).toTimeString());


        return view;
    }

    static class ViewHolder {
        TextView lblCounter;
        TextView lblTitle;
        TextView lblDate;

    }
}
