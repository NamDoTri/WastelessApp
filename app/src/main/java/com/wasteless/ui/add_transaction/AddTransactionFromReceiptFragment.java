package com.wasteless.ui.add_transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;

public class AddTransactionFromReceiptFragment extends Fragment {
    private AddTransactionViewModel addTransactionViewModel;

    public View AddTransactionFromReceipt(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        addTransactionViewModel = ViewModelProviders.of(this).get(AddTransactionViewModel.class);
        final View AddTransactionFromReceiptFragmentView = inflater.inflate(R.layout.fragment_add_transaction_from_gallery, container, false);

        return AddTransactionFromReceiptFragmentView;
    }
}
