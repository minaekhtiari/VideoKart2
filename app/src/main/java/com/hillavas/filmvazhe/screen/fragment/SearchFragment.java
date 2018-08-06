package com.hillavas.filmvazhe.screen.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.adapters.SearchSeggestionAdapter;
import com.hillavas.filmvazhe.adapters.WordsAdapter;
import com.hillavas.filmvazhe.model.LessonStatus;
import com.hillavas.filmvazhe.model.Word;
import com.hillavas.filmvazhe.screen.BookWordDialougeListActivity;
import com.hillavas.filmvazhe.utils.Validator;
import com.hillavas.filmvazhe.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class SearchFragment extends Fragment implements TextWatcher, View.OnClickListener, MaterialSearchBar.OnSearchActionListener {


    @BindView(R.id.grid_movies)
    RecyclerView listCard;

    @BindView(R.id.searchBar)
    MaterialSearchBar searchBar;


    WordsAdapter wordsAdapter;
    ArrayList<Word> wordArrayList = new ArrayList<>();

    SearchSeggestionAdapter adapter;
    List<String> suggestion = new ArrayList<>();
    private boolean searchTyping = true;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);

        LinearLayoutManager llm = new LinearLayoutManager(MyApplication.getContext());
        listCard.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listCard.getContext(),
                llm.getOrientation());
        listCard.addItemDecoration(dividerItemDecoration);

//        adapter = new SearchSeggestionAdapter(getActivity(), suggestion);
//        lvSearchSuggestion.setAdapter(adapter);

        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this);
        searchBar.addTextChangeListener(this);
        //restore last queries from disk
        // lastSearches = loadSearchSuggestionFromDisk();
        //searchBar.setLastSuggestions(list);


        searchBar.setLastSuggestions(suggestion);

        searchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {

               getWords(searchBar.getLastSuggestions().get(position).toString());
                searchBar.hideSuggestionsList();

            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });

        return v;

    }

    void getWords(String word) {

        wordArrayList.clear();

        MyApplication.apiVideokart.searchBookWord(word, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {
                    JsonArray array = result.getAsJsonArray("data");
                    if (array.size() > 0) {
                        for (int i = 0; i < array.size(); i++) {
                            wordArrayList.add(new Word(array.get(i).getAsJsonObject()));
                        }

                        wordsAdapter = new WordsAdapter(wordArrayList, new WordsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(final Word item) {

                                MyApplication.apiVideokart.getMovieWords(null, item.getId(), new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                                        JsonObject status = result.getAsJsonObject("status");

                                        if (status.get("code").getAsInt() == 200) {
                                            JsonArray array = result.getAsJsonArray("data");
                                            if (array.size() > 0) {

                                                    Intent intent = new Intent(getContext(), BookWordDialougeListActivity.class);
                                                    intent.putExtra("word", item);
                                                    intent.putExtra("searchResult", new String(responseBody));
                                                    startActivity(intent);


                                            } else {
                                                SnackbarManager.show(
                                                        Snackbar.with(getContext()) // context
                                                                .textTypeface(MyApplication.getTypeFace())
                                                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                                                .swipeToDismiss(true)
                                                                .text("کلمه ای یافت نشد") // text to display

                                                        , getActivity()); // activity where it is dis                                                // TODO: 16/07/08 cant find any word
                                            }
                                        } else {
                                            SnackbarManager.show(
                                                    Snackbar.with(getContext()) // context
                                                            .textTypeface(MyApplication.getTypeFace())
                                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                                            .swipeToDismiss(true)
                                                            .text(status.get("message").getAsString()) // text to display

                                                    , getActivity()); // activity where it is dis
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });

                            }
                        });
                        listCard.setAdapter(wordsAdapter);

                    } else {
                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .textTypeface(MyApplication.getTypeFace())
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .swipeToDismiss(true)
                                        .text("کلمه ای یافت نشد") // text to display

                                , getActivity()); // activity where it is dis                        // TODO: 16/07/08 cant find any word
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .swipeToDismiss(true)
                                .text("خطا در برقراری ارتباط") // text to display

                        , getActivity()); // activity where it is displayed
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });


    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        suggestion.clear();
//        adapter.notifyDataSetChanged();
//        if (lvSearchSuggestion.getVisibility() == View.GONE)
//            lvSearchSuggestion.setVisibility(View.VISIBLE);

        if (charSequence.toString().length() > 0 && Validator.IsEnglish(charSequence.toString())) {

            if (searchTyping) {
                searchTyping = false;
                MyApplication.apiVideokart.searchSimple(charSequence.toString(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                        JsonObject status = result.getAsJsonObject("status");


                        if (status.get("code").getAsInt() == 200) {
                            JsonArray array = result.getAsJsonArray("data");

                            for (int i = 0; i < array.size(); i++) {
                                if (i == 5)
                                    break;
                                suggestion.add(new String(array.get(i).getAsString()));
                            }
                            searchBar.updateLastSuggestions(suggestion);
                            if (suggestion.size() > 0)
                                searchBar.showSuggestionsList();

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
//                        adapter.notifyDataSetChanged();
                        searchTyping = true;
                    }
                });
            }
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchBar.hideSuggestionsList();

        getWords(text.toString());
        
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_SPEECH:

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getContext(), "Oops! Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onDestroy() {
        searchBar.getLastSuggestions();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    searchBar.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
                    searchBar.enableSearch();
                }
                break;
            }
        }
    }
}
