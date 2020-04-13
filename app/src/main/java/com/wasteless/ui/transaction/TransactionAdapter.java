package com.wasteless.ui.transaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Transaction> transactions = new ArrayList<>();
    private OnTransactionClickListener listener;

    public void setTransactions(List<Transaction> transactions){
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_row, parent,false);
            return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Transaction transaction = transactions.get(position);

            ((TransactionHolder) holder).description.setText(transaction.description);
            ((TransactionHolder) holder).type.setText(transaction.type);
            ((TransactionHolder) holder).amount.setText(String.valueOf(transaction.amount));
            ((TransactionHolder) holder).tagOne.setImageResource(R.drawable.transaction_recycler_view_tag);
            ((TransactionHolder) holder).tagTwo.setImageResource(R.drawable.transaction_recycler_view_tag);
            ((TransactionHolder) holder).tagThree.setImageResource(R.drawable.transaction_recycler_view_tag);
        Log.d("transactionAdapter", "transaction: " + transaction);

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView type;
        private TextView amount;
        private TextView date;
        private LinearLayout tags;
        private ImageView tagOne;
        private ImageView tagTwo;
        private ImageView tagThree;

        public TransactionHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.transaction_description);
            type = itemView.findViewById(R.id.transaction_category);
            amount = itemView.findViewById(R.id.transaction_amount);
//            date = itemView.findViewById(R.id.transaction_date);
            tags = itemView.findViewById(R.id.tags_container);
            tagOne = itemView.findViewById(R.id.tag_one);
            tagTwo = itemView.findViewById(R.id.tag_two);
            tagThree = itemView.findViewById(R.id.tag_three);

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
