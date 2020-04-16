package com.wasteless.ui.wallets_manager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class WalletsManagerFragment extends Fragment {

    private WalletsManagerViewModel walletsManagerViewModel;
    private WalletsManagerAdapter walletsManagerAdapter;
    private RecyclerView walletsManagerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Wallet> allWallets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wallets_manager, container, false);
        walletsManagerViewModel = ViewModelProviders.of(this).get(WalletsManagerViewModel.class);
        allWallets = walletsManagerViewModel.getAllWallets();
        Log.d("wallets", "" + allWallets);
        walletsManagerAdapter = new WalletsManagerAdapter();
        walletsManagerAdapter.setWallets(allWallets);

        walletsManagerView.setAdapter(walletsManagerAdapter);
        walletsManagerView.setLayoutManager(layoutManager);


        return root;
    }




}
