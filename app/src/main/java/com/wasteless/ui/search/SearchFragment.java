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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.models.TestTransaction;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.transaction.ActualAdapter;
import com.wasteless.ui.transaction.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements  SearchView.OnQueryTextListener, RadioGroup.OnCheckedChangeListener {

    private SearchViewModel searchViewModel;
    private RecyclerView searchResultView;
    private RecyclerView.LayoutManager layoutManager;
    private ActualAdapter transactionAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
//        searchViewModel.getTestTransactionsList().observer().
        transactionAdapter = new ActualAdapter();
        searchViewModel.getOnOpenData().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                transactionAdapter.setTransactions(transactions);
            }
        });

        View root = inflater.inflate(R.layout.fragment_search, container, false);
//     Initialised view objects
        SearchView searchField = root.findViewById(R.id.search_field);
        RadioGroup filterButtons = root.findViewById(R.id.filters_list);
        RadioButton checkedRadioButton = filterButtons.findViewById(filterButtons.getCheckedRadioButtonId());
        searchResultView = root.findViewById(R.id.search_list);

//    RecyclerView.Adapter
        layoutManager = new LinearLayoutManager(getActivity());
        searchResultView.setAdapter(transactionAdapter);
        searchResultView.setLayoutManager(layoutManager );
        searchResultView.setHasFixedSize(true);

        searchField.setOnQueryTextListener(this);
        filterButtons.setOnCheckedChangeListener(this);

        super.onCreate(savedInstanceState);

//        prepareData();

        return root;
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d("RadioListener", "button Id: " + checkedId + "was checked!");
    }
}
