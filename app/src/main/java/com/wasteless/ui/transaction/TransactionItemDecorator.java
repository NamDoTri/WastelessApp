package com.wasteless.ui.transaction;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionItemDecorator extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable divider;

    /**
     * Default divider will be used
     */
    public TransactionItemDecorator(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public TransactionItemDecorator(Context context, int resId) {
        divider = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
            outRect.top = divider.getIntrinsicHeight() + 3;
            outRect.left = divider.getIntrinsicWidth() / 2 + 15;
            outRect.right = divider.getIntrinsicWidth() / 2 + 15;
            Log.d("ItemDecorator", "" + outRect.top);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + 15;
        int right = parent.getWidth() - parent.getPaddingRight() - 15;
        Log.d("ItemDecorator", "left: " + left + " right: " + right);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight() + 3;
            Log.d("ItemDecorator", "top: " + top + " bottom: " + bottom);

            divider.setBounds(left, top, right, bottom);
//            divider.drawText()
            divider.draw(c);
        }
    }




}
