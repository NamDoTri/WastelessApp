package com.wasteless.ui.wallets_manager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
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
//        Creates walletsManagerViewModel and makes it work
        walletsManagerViewModel = ViewModelProviders.of(this).get(WalletsManagerViewModel.class);
        allWallets = walletsManagerViewModel.getAllWallets();
        Log.d("wallets", "" + allWallets);

        walletsManagerAdapter = new WalletsManagerAdapter();
        walletsManagerAdapter.setWallets(allWallets);

//    Assign linear layout manager to the layout manager
        layoutManager = new LinearLayoutManager(getActivity());

//     Assigns the wallets layout to the walletManagerView
        walletsManagerView = root.findViewById(R.id.wallets_list);
//        Assigns the layout manager and the adapter to walletsManagerView
        walletsManagerView.setLayoutManager(layoutManager);
        walletsManagerView.setAdapter(walletsManagerAdapter);

        setHasOptionsMenu(true);



        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wallets_manager_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    };


}
