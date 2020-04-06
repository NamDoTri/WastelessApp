package com.wasteless.ui.transaction;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionItemDecorator extends RecyclerView.ItemDecoration {
    private int offset;
    private HeaderAdapter adapter;
    private SparseArray<View> headers;

    public interface HeaderAdapter {
        boolean hasHeader(int position);
        View getHeaderView(int position);
    }

    public  TransactionItemDecorator(int offset, HeaderAdapter adapter) {
        this.offset = offset;
        this.adapter = adapter;
        this.headers = new SparseArray();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildLayoutPosition(view);

        if (position != RecyclerView.NO_POSITION && adapter.hasHeader(position)) {
            View headerView = adapter.getHeaderView(position);
            headers.put(position, headerView);

            measureHeaderView(headerView, parent);
            outRect.top = headerView.getHeight();
        } else {
            headers.remove(position);
        }

    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position != RecyclerView.NO_POSITION && adapter.hasHeader(position)) {
                canvas.save();
                View headerView = headers.get(position);
                canvas.translate(0, child.getY() - headerView.getHeight());
                headerView.draw(canvas);
                canvas.restore();
            }
        }
    }

    protected void measureHeaderView(View view, ViewGroup parent) {
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        final DisplayMetrics displayMetrics = parent.getContext().getResources().getDisplayMetrics();

        int widthSpec = View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidth, childHeight);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }


}
