package com.wasteless.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.models.TestTransaction;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private List<TestTransaction> testTransactions = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_row, parent,false);
        return new RecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        TestTransaction transaction = testTransactions.get(position);

        holder.description.setText(transaction.getDescription());
        holder.category.setText(transaction.getCategory());
        holder.amount.setText(transaction.getAmount());
    }

    @Override
    public int getItemCount() {
        return testTransactions.size();
    }

    public void setTestTransactions(List<TestTransaction> testTransactions){
        this.testTransactions = testTransactions;
        notifyDataSetChanged();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView category;
        private TextView amount;

        public RecyclerHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.transaction_description);
            category = itemView.findViewById(R.id.transaction_category);
            amount = itemView.findViewById(R.id.transaction_amount);
        }
    }
}
