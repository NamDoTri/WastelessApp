package com.wasteless.ui.transaction;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionItemDecorator extends RecyclerView.ItemDecoration {
    private int offset;

    public  TransactionItemDecorator(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

//        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.right = offset;
            outRect.left = offset;
            outRect.top = offset;
            outRect.bottom = offset;
//        }
    }



}
