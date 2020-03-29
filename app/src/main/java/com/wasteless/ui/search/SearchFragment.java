package com.wasteless.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.transaction.TransactionAdapter;
import com.wasteless.ui.transaction.TransactionFragment;

import java.util.List;

public class SearchFragment extends Fragment implements  SearchView.OnQueryTextListener, RadioGroup.OnCheckedChangeListener {

    private SearchViewModel searchViewModel;
    private RecyclerView searchResultView;
    private RecyclerView.LayoutManager layoutManager;
    private TransactionAdapter transactionAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
//        searchViewModel.getTestTransactionsList().observer().
        transactionAdapter = new TransactionAdapter();
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

        transactionAdapter.setOnTransactionClickListener(new TransactionAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(Transaction transaction) {
                TransactionFragment transactionFragment = new TransactionFragment();

                //I think that in the end I only need to transfer the ID of the item that was clicked
                //since we should pull more data from the db in the transaction details-fragment (like actual date, wallet etc).

                //So this is just to test out the process of changing fragments and to design the details
                Bundle transactionBundle = new Bundle();
                transactionBundle.putString("description", transaction.description);
                //transactionBundle.putString("category", transaction.category);
                transactionBundle.putString("amount", String.valueOf(transaction.amount));
                transactionFragment.setArguments(transactionBundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, transactionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        searchField.setOnQueryTextListener(this);
        filterButtons.setOnCheckedChangeListener(this);

        super.onCreate(savedInstanceState);

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
