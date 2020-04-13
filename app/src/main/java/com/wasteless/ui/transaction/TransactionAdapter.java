package com.wasteless.ui.transaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Tag;
import com.wasteless.roomdb.entities.TagAssociation;
import com.wasteless.roomdb.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    TransactionViewModel transactionViewModel;

    private List<Transaction> transactions = new ArrayList<>();
    private List<TagAssociation> tags = new ArrayList<>();
    private OnTransactionClickListener listener;

    public void setTransactions(List<Transaction> transactions, List<TagAssociation> tags){
        this.transactions = transactions;
        this.tags = tags;
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
        TagAssociation tag = tags.get(position);
        Log.d("transactionAdapter", "tag: " + tag.tag);
        Log.d("transactionAdapter", "tag: " + tag.transactionId);
        Log.d("transactionAdapter", "tag: " + tag.index);

            ((TransactionHolder) holder).description.setText(transaction.description);
            ((TransactionHolder) holder).type.setText(transaction.type);
            ((TransactionHolder) holder).amount.setText(String.valueOf(transaction.amount));
            for (int i=0; i < tags.size(); i++) {
                if (tag.transactionId == transaction.transactionId) {
                    ((TransactionHolder) holder).tagOne.setBackgroundResource(R.drawable.transaction_recycler_view_tag);
                    ((TransactionHolder) holder).tagOne.setText(tag.tag);
                    ((TransactionHolder) holder).tagTwo.setBackgroundResource(R.drawable.transaction_recycler_view_tag);
                    ((TransactionHolder) holder).tagTwo.setText(tag.tag);
                    ((TransactionHolder) holder).tagThree.setBackgroundResource(R.drawable.transaction_recycler_view_tag);
                    ((TransactionHolder) holder).tagThree.setText(tag.tag);
                }
            }
//            ((TransactionHolder) holder).tagOne.setBackgroundResource(R.drawable.transaction_recycler_view_tag);
//            ((TransactionHolder) holder).tagOne.setText("asdasd");
//            ((TransactionHolder) holder).tagTwo.setBackgroundResource(R.drawable.transaction_recycler_view_tag);
//            ((TransactionHolder) holder).tagTwo.setText("asdasd");
//            ((TransactionHolder) holder).tagThree.setBackgroundResource(R.drawable.transaction_recycler_view_tag);
//            ((TransactionHolder) holder).tagThree.setText("asdasd");
        Log.d("transactionAdapter", "transaction: " + transaction);

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        private Long id;
        private TextView description;
        private TextView type;
        private TextView amount;
        private TextView date;
        private LinearLayout tags;
        private Button tagOne;
        private Button tagTwo;
        private Button tagThree;

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
