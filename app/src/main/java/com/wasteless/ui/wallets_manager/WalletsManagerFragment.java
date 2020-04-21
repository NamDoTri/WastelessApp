package com.wasteless.ui.wallets_manager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.ui.settings.newWallet.NewWalletFragment;

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
        walletsManagerView.addItemDecoration(new WalletItemDecorator(getActivity(), R.drawable.transaction_divider));
        walletsManagerView.setLayoutManager(layoutManager);
        walletsManagerView.setAdapter(walletsManagerAdapter);

        setHasOptionsMenu(true);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                walletsManagerViewModel.deleteWallet(walletsManagerAdapter.getWalletAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Wallet deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(walletsManagerView);




        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wallets_manager_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("menu", "selected option: " + item);
        NewWalletFragment newWalletFragment = new NewWalletFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.nav_host_fragment, newWalletFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return super.onOptionsItemSelected(item);
    }

}
