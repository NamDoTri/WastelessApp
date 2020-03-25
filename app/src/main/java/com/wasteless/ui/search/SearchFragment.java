package com.wasteless.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements  SearchView.OnQueryTextListener {

    private SearchViewModel searchViewModel;
    private ArrayList<Filter> filters = new ArrayList<>();
    private FiltersAdapter filtersAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_filter, container, false);
//     Initialised view objects
        SearchView searchField = (SearchView) root.findViewById(R.id.search_field);
        RecyclerView filtersView = (RecyclerView) root.findViewById(R.id.filters_list);
        RecyclerView searchResultView = (RecyclerView) root.findViewById(R.id.search_list);

//    RecyclerView.Adapter
        filtersAdapter = new FiltersAdapter(filters);

        searchField.setOnQueryTextListener(this);

//        filtersView.setAdapter(filtersAdapter);

        super.onCreate(savedInstanceState);

        prepareData();

        return root;
    }

    private void prepareData() {
        Filter filter = new Filter("date");


    }

    @Override
    public void onPause() {
        Log.d("onPause", "It has stopped at this point");
        super.onPause();
    };

    @Override
    public void onStop() {
        Log.d("onStop",  "it has stopped at this point");
        super.onStop();
    };

//    Search field listeners
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("onQueryTextSubmit", "It works: " + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("onQueryTextChange", "It works: " + newText);
        return false;
    }
}
