package com.example.techstore.untilities;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spanCount;
    private final int widthItem;

    public GridSpacingItemDecoration(int spanCount, int widthItem) {
        this.spanCount = spanCount;
        this.widthItem = widthItem;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int totalWidth = parent.getWidth();
        int totalSpacing = totalWidth - widthItem * spanCount;
        int spacing = totalSpacing / (spanCount * 2);
        outRect.left = spacing;
        outRect.right = spacing;
    }
}
