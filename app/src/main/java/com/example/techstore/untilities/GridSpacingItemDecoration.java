package com.example.techstore.untilities;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
//    private final int spanCount;
//    private final int widthItem;
//
//    public GridSpacingItemDecoration(int spanCount, int widthItem) {
//        this.spanCount = spanCount;
//        this.widthItem = widthItem;
//    }
//
//
//    @Override
//    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        int totalWidth = parent.getWidth();
//        int totalSpacing = totalWidth - widthItem * spanCount;
//        int spacing = totalSpacing / (spanCount * 2);
//        outRect.left = spacing;
//        outRect.right = spacing;
//    }
    private final int spanCount;
    private final int space;

    public GridSpacingItemDecoration(int spanCount, int space) {
        this.spanCount = spanCount;
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        outRect.left = space;
        outRect.right = space;
        outRect.top = space;
        outRect.bottom = space;
//        outRect.left = space / 2;
//        outRect.right = space / 2;
//        outRect.top = space / 2;
//        outRect.bottom = space / 2;
//        if (column == 0) outRect.left = space;
//        if (column == spanCount - 1) outRect.right = space;
    }

}
