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
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
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

    private int descriptionButtonId;
    private int dateButtonId;
    private int tagButtonId;
    private int categoryButtonId;

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
        Log.d("chosenFilter", "radio button" + checkedRadioButton.getText());
        searchResultView = root.findViewById(R.id.search_list);

//        Assigning buttonIds
//        filterButtons.findViewById(root.findIdById(R.id.filter_name))

//    RecyclerView.Adapter
        layoutManager = new LinearLayoutManager(getActivity());
        searchResultView.setAdapter(transactionAdapter);
        searchResultView.setLayoutManager(layoutManager );
        searchResultView.setHasFixedSize(true);

        transactionAdapter.setOnTransactionClickListener(new TransactionAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(Transaction transaction) {
                TransactionFragment transactionFragment = new TransactionFragment();
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
//        LiveData<List<Transaction>> searchedData = searchViewModel.searchTransactionsByDescription(query);
        searchViewModel.searchTransactionsByDescription(query).observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                Log.d("onQueryTextSubmit", "" + transactions);
                transactionAdapter.setTransactions(transactions);
            }
        });
//        transactionAdapter.setTransactions(searchedData);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("onQueryTextChange", "It works: " + newText);
        searchViewModel.searchTransactionsByDescription(newText).observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
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
        if (checkedId == 2131230857) {
            Log.d("RadioListener", "category");
//            MutableLiveData<String> category = Transformations.map("category", new Function<String, MutableLiveData<String>>() {
//                @Override
//                public MutableLiveData<String> apply(String input) {
//                    return null;
//                }
//            });
            MutableLiveData<String> category = null;
            category.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    s = "category";
                    Log.d("someShit", "" + s);
                }
            });
//            MutableLiveData<String> category = "category";
//            searchViewModel.setActiveFilter("category").observe(getViewLifecycleOwner(), new Observer<MutableLiveData<String>>() {
//                @Override
//                public void onChanged(MutableLiveData<String> stringMutableLiveData) {
//
//                }
//            });
//            searchViewModel.setActiveFilter();
        }

        if (checkedId == 2131230858) {
            Log.d("RadioListener", "date");
        }

        if (checkedId == 2131230859) {
            Log.d("RadioListener", "description");
        }

        if (checkedId == 2131230860) {
            Log.d("RadioListener", "tag");
        }
//        searchViewModel.setActiveFilter(checkedId);
    }

//    @Override
//    public void onRadioButtonClicked(View view) {
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.filter_name:
//                if (checked)
//                    Log.d("checkedRadioButton", "name");
//                    break;
//            case R.id.filter_date:
//                if (checked)
//                    // Ninjas rule
//                    Log.d("checkedRadioButton", "date");
//                    break;
//            case R.id.filter_category:
//                if (checked)
//                    // Pirates are the best
//                    Log.d("checkedRadioButton", "category");
//                    break;
//            case R.id.filter_tag:
//                if (checked)
//                    // Ninjas rule
//                    Log.d("checkedRadioButton", "tag");
//                    break;
//        }
//    }
}
