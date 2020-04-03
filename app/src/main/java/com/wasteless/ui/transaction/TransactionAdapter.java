package com.wasteless.ui.transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private List<Transaction> transactions = new ArrayList<>();
    private OnTransactionClickListener listener;

    public void setTransactions(List<Transaction> transactions){
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_row, parent,false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.description.setText(transaction.description);
        holder.type.setText(transaction.type);
        holder.amount.setText(String.valueOf(transaction.amount));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView type;
        private TextView amount;
        private LinearLayout tags;

        public TransactionHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.transaction_description);
            type = itemView.findViewById(R.id.transaction_category);
            amount = itemView.findViewById(R.id.transaction_amount);
//            tags = itemView.findViewById(R.id.transaction_tags);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onTransactionClick(transactions.get(position));
                    }
                }
            });
        }
    }

    public interface OnTransactionClickListener{
        void onTransactionClick(Transaction transaction);
    }

    public void setOnTransactionClickListener(OnTransactionClickListener listener){
        this.listener = listener;
    }

    public Transaction getTransactionAt(int position){
        return transactions.get(position);
    }

    /*Probably gonna need these later on when implementing transactions as separate expenses and incomes

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView category;
        private TextView amount;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.transaction_description);
            category = itemView.findViewById(R.id.transaction_category);
            amount = itemView.findViewById(R.id.transaction_amount);
        }
    }

    class IncomeViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView type;
        private TextView amount;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.transaction_description);
            type = itemView.findViewById(R.id.transaction_category);
            amount = itemView.findViewById(R.id.transaction_amount);
        }
    }*/
}
