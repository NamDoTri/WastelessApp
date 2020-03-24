package com.wasteless.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;

import java.text.BreakIterator;
import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.MyViewHolder> {

    private String[] buttonsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView buttonText = (TextView) itemView.findViewById(R.id.filter_button);
        }
    }

    public FiltersAdapter(String[] buttonsList) {
        this.buttonsList = buttonsList;
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
//        holder.buttonText.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return 4;
    }


}
