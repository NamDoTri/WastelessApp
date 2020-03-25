package com.wasteless.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;

import java.nio.DoubleBuffer;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.MyViewHolder> {

    private ArrayList<Filter> buttonsList;

    public FiltersAdapter( ArrayList<Filter> buttonsList) {
        this.buttonsList = buttonsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView buttonText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonText = itemView.findViewById(R.id.filter_button);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_search_filter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Filter filter = buttonsList.get(position);
        holder.buttonText.setText(filter.getFilterName());
    }

    @Override
    public int getItemCount() {
        return buttonsList.size();
    }


}
