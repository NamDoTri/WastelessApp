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

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private List<Transaction> transactions = new ArrayList<>();
    private OnTransactionClickListener listener;

    public TransactionAdapter(List<Transaction> transactions ) {
        this.transactions = transactions;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position > transactions.size();
    }

    public void setTransactions(List<Transaction> transactions){
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_row, parent,false);
            return new TransactionHolder(itemView);

        } else if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_transaction_list_header, parent, false);
            return new HeaderViewHolder(headerView);

        } else {
            return null;
        }
//        } else if (viewType == TYPE_FOOTER) {
//
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_transaction_list_footer,
//                    parent, false);
//            return new FooterViewHolder(view);
//
//        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Transaction transaction = transactions.get(position);

        if (holder instanceof HeaderViewHolder) {

            //Установите значение из списка в соответствующий компонент пользовательского интерфейса, как показано ниже.
            ((HeaderViewHolder) holder).txtName.setText(transaction.toString());

            //Аналогично можно связывать другие компоненты пользовательского интерфейса

        }
        if (holder instanceof TransactionHolder) {
            ((TransactionHolder) holder).description.setText(transaction.description);
            ((TransactionHolder) holder).type.setText(transaction.type);
            ((TransactionHolder) holder).amount.setText(String.valueOf(transaction.amount));

        }

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


    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public View View;
        private final TextView txtName;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            View = itemView;

            // Добавьте свои компоненты ui здесь, как показано ниже
            txtName = (TextView) View.findViewById(R.id.transaction_item_header);

        }
    }

//    public class FooterViewHolder extends RecyclerView.ViewHolder {
//        public View View;
//        public ViewHolder(View v) {
//            super(v);
//            View = v;
//            // Добавьте компоненты пользовательского интерфейса здесь.
//        }
//
//        public FooterViewHolder(@NonNull android.view.View itemView) {
//            super(itemView);
//        }
//    }

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
