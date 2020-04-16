package com.wasteless.ui.wallets_manager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class WalletsManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Wallet> wallets;

    public void setWallets(List<Wallet> allWallets) {
        this.wallets = allWallets;
        Log.d("wallets", "adapter: " + wallets);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_wallet_element, parent,false);
        return new WalletsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Wallet wallet = wallets.get(position);
        Log.d("wallets", "position: " + position);
        ((WalletsHolder) holder).walletName.setText(wallet.name);
        Log.d("wallets", "walletId: " + wallet.name);
        ((WalletsHolder) holder).walletAmount.setText(Double.toString(wallet.balance));
        Log.d("wallets", "wallet balance: " + wallet.balance);
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    class WalletsHolder extends RecyclerView.ViewHolder {

        private TextView walletName;
        private TextView walletAmount;

        public WalletsHolder (View itemView) {
            super(itemView);
            walletName = itemView.findViewById(R.id.wallet_name);
            walletAmount = itemView.findViewById(R.id.wallet_amount);
        }



    }



}
