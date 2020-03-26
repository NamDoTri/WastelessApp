//package com.wasteless.ui.search;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.wasteless.R;
//
//import java.nio.DoubleBuffer;
//import java.text.BreakIterator;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.MyViewHolder> {
//
//    private ArrayList<Filter> buttonsList = new ArrayList<Filter>();
//
//    public FiltersAdapter( ArrayList<Filter> buttonsText) {
//        buttonsList = buttonsText;
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView buttonText;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
////            buttonText = itemView.findViewById(R.id.filter_button);
//        }
//    }
//
////    public FiltersAdapter(String)
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.fragment_search_filter, parent, false);
//        Log.d("onCreateViewHolder", "the shit is here: " + itemView);
////        MyViewHolder filterView = new MyViewHolder(itemView);
//        return new MyViewHolder(itemView);
////        return filterView;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Filter filter = buttonsList.get(position);
//        holder.buttonText.setText(filter.getFilterName());
//        Log.d("onBindViewHolder", "the text is: " + filter.getFilterName());
//    }
//
//    @Override
//    public int getItemCount() {
//        Log.d("getItemCount", "the shit is here: " + buttonsList.size());
//        return buttonsList.size();
//    }
//
//
//}
