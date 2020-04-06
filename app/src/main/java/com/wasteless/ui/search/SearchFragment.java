package com.wasteless.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.wasteless.R;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.transaction.TransactionAdapter;
import com.wasteless.ui.transaction.TransactionFragment;
import com.wasteless.ui.transaction.TransactionItemDecorator;

import java.util.List;

public class SearchFragment extends Fragment implements  SearchView.OnQueryTextListener, RadioGroup.OnCheckedChangeListener {

    private SearchViewModel searchViewModel;
    private RecyclerView searchResultView;
    private RecyclerView.LayoutManager layoutManager;
    private TransactionAdapter transactionAdapter;

    private int descriptionButtonId;
    private int dateButtonId;
    private int tagButtonId;
    private MutableLiveData<String> category;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        transactionAdapter = new TransactionAdapter();
        searchViewModel.getOnOpenData().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                transactionAdapter.setTransactions(transactions);
            }
        });
        searchViewModel.setActiveFilter("date");
        searchViewModel.setSearchValue("");


        View root = inflater.inflate(R.layout.fragment_search, container, false);
//     Initialised view objects
        SearchView searchField = root.findViewById(R.id.search_field);
        RadioGroup filterButtons = root.findViewById(R.id.filters_list);
        RadioButton checkedRadioButton = filterButtons.findViewById(filterButtons.getCheckedRadioButtonId());
        Log.d("chosenFilter", "radio button" + checkedRadioButton.getText());
        searchResultView = root.findViewById(R.id.search_list);

//    RecyclerView.Adapter
        layoutManager = new LinearLayoutManager(getActivity());
        searchResultView.addItemDecoration(new TransactionItemDecorator(getActivity(), R.drawable.transaction_divider));
        searchResultView.setAdapter(transactionAdapter);
        searchResultView.setLayoutManager(layoutManager );
        searchResultView.setHasFixedSize(true);

        transactionAdapter.setOnTransactionClickListener(new TransactionAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(Transaction transaction) {
                TransactionFragment transactionFragment = new TransactionFragment();
                Bundle transactionBundle = new Bundle();
                transactionBundle.putLong("id", transaction.transactionId);
                transactionFragment.setArguments(transactionBundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, transactionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                searchViewModel.delete(transactionAdapter.getTransactionAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Transaction deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(searchResultView);


        searchField.setOnQueryTextListener(this);
        filterButtons.setOnCheckedChangeListener(this);

        super.onCreate(savedInstanceState);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
    }

    @Override
    public void onPause() {
        Log.d("onPause", "It has stopped at this point");
        super.onPause();
    };

    @Override
    public void onStop() {
        Log.d("onStop",  "it has stopped at this point");
//        onSaveInstanceState();
        super.onStop();
    };

//    Search field listeners
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("onQueryTextSubmit", "It works: " + query);
        searchViewModel.setSearchValue(query).observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                Log.d("onQueryTextSubmit", "" + transactions);
                transactionAdapter.setTransactions(transactions);
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("onQueryTextChange", "It works: " + newText);
        searchViewModel.setSearchValue(newText).observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                Log.d("onQueryTextSubmit", "" + transactions);
                transactionAdapter.setTransactions(transactions);
            }
        });
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d("RadioListener", "button Id: " + checkedId + " was checked!");
        if (checkedId == R.id.filter_category) {
            Log.d("RadioListener", "category");
            searchViewModel.setActiveFilter("category");
            MutableLiveData<String> activeFilter = searchViewModel.getActiveFilter();
            Log.d("RadioListener", "" + activeFilter);
        }

        if (checkedId == R.id.filter_date) {
            Log.d("RadioListener", "date");
            searchViewModel.setActiveFilter("date");
            MutableLiveData<String> activeFilter = searchViewModel.getActiveFilter();
            Log.d("RadioListener", "" + activeFilter);
        }

        if (checkedId == R.id.filter_description) {
            Log.d("RadioListener", "description");
            searchViewModel.setActiveFilter("description");
            MutableLiveData<String> activeFilter = searchViewModel.getActiveFilter();
            Log.d("RadioListener", "" + activeFilter);
        }

        if (checkedId == R.id.filter_tag) {
            Log.d("RadioListener", "tag");
            searchViewModel.setActiveFilter("tag");
            MutableLiveData<String> activeFilter = searchViewModel.getActiveFilter();
            Log.d("RadioListener", "" + activeFilter);
        }
    }
}
