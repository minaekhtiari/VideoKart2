package com.hillavas.filmvazhe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.model.Word;
import com.hillavas.filmvazhe.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Arash on 16/05/17.
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> implements Filterable {


    public interface OnItemClickListener {
        void onItemClick(Word item);
    }

    private WordFilter userFilter;
    List<Word> wordList;
    List<Word> wordListFilter;
    int[] borderColor;
    private final OnItemClickListener onItemClickListener;

    public WordsAdapter(List<Word> wordList, OnItemClickListener onItemClickListener) {
        this.wordList = wordList;
        this.wordListFilter = wordList;
        this.onItemClickListener = onItemClickListener;
        borderColor = MyApplication.getContext().getResources().getIntArray(R.array.lesson_color);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_word_item, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.bind(wordList.get(position), onItemClickListener);

        holder.lblTitle.setText(StringUtils.capitalizeFirstLetter(wordList.get(position).getName()));

        String synonym = wordList.get(position).getNoun();
        if (synonym == null) synonym = wordList.get(position).getVerb();
        if (synonym == null) synonym = wordList.get(position).getAdj();

        holder.lblSynonym.setText(synonym);

        holder.lblCount.setText(String.format("%d clip", wordList.get(position).getClipCount()));
        holder.lblProun.setText(String.format("[%s]", wordList.get(position).getProun()));



//        if (wordList.get(position).isStudied()) {
//            holder.imgStudied.setVisibility(View.VISIBLE);
//        } else {
//            holder.imgStudied.setVisibility(View.INVISIBLE);
//
//        }


    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblTitle;
        TextView lblCount;
        TextView lblSynonym;
        TextView lblProun;
        ImageView imgStudied;

        ViewHolder(View itemView) {
            super(itemView);
            lblTitle = (TextView) itemView.findViewById(R.id.lbl_title);
            lblSynonym = (TextView) itemView.findViewById(R.id.lbl_synonym);
            lblCount = (TextView) itemView.findViewById(R.id.lbl_clip_count);
            lblProun = (TextView) itemView.findViewById(R.id.lbl_proun);

            imgStudied = (ImageView) itemView.findViewById(R.id.img_studied);

            // carView.getLayoutParams().height = (int) (((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * (25.0f / 100.0f));

        }

        public void bind(final Word item, final OnItemClickListener listener) {

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


    ///FILTER CODE


    @Override
    public Filter getFilter() {
        if (userFilter == null)
            userFilter = new WordFilter(this, wordList);
        return userFilter;
    }

    private static class WordFilter extends Filter {

        private final WordsAdapter adapter;

        private final List<Word> originalList;

        private final List<Word> filteredList;

        private WordFilter(WordsAdapter adapter, List<Word> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Word word : originalList) {
                    if (word.getName().contains(filterPattern)) {
                        filteredList.add(word);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.wordListFilter.clear();
            adapter.wordListFilter.addAll((ArrayList<Word>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
