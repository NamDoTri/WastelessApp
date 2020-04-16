package com.wasteless.ui.wallets_manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class WalletsManagerFragment extends Fragment {

    private WalletsManagerViewModel walletsManagerViewModel;
    private RecyclerView searchResultView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Wallet> wallets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wallets_manager, container, false);

        return root;
    }




}
