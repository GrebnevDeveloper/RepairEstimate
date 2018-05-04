package ru.grebnev.repairestimate.employment.list.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.grebnev.repairestimate.R;

public class SimpleDeviderItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable devider;

    public SimpleDeviderItemDecoration(Context context) {
        devider = ContextCompat.getDrawable(context, R.drawable.item_devider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + devider.getIntrinsicHeight();

            devider.setBounds(left, top, right, bottom);
            devider.draw(c);
        }
    }
}
