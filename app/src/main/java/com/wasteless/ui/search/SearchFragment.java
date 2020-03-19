package com.wasteless.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;

public class SearchFragment extends Fragment  {

    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        findViewById(R.id.addNewTodoItem).setOnClickListener(this);
////        ListView listView = findViewById(R.id.todoListView);
////        listView.setOnItemClickListener(this);

        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        return root;
    }
}
