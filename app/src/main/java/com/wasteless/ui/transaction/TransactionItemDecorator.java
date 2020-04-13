package com.wasteless.ui.transaction;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;

public class TransactionItemDecorator extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable divider;
    private int primaryLight;

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
        primaryLight = ContextCompat.getColor(context,R.color.colorPrimaryLight);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
            outRect.top = divider.getIntrinsicHeight() + 2;
            outRect.left = divider.getIntrinsicWidth();
            outRect.right = divider.getIntrinsicWidth();
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        Paint paint = new Paint();
        paint.setColor(primaryLight);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(45);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        Log.d("ItemDecorator", "left: " + left + " right: " + right);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
//            String date = parent.getContext().getTextView(R.id.date);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight() + 3;
            Log.d("ItemDecorator", "top: " + top + " bottom: " + bottom);

            divider.setBounds(left, top, right, bottom);
            canvas.drawText("Test text", left, top, paint);
            divider.draw(canvas);
        }
    }




}
