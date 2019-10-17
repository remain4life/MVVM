package org.remain4life.mvvm.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.remain4life.mvvm.views.base.BaseRecyclerView;

public class GridRecyclerView extends BaseRecyclerView {

    public GridRecyclerView(Context context) {
        super(context);

        setClipToPadding(false);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayoutManager(createLayoutManager(context, attrs, defStyle));

        setClipToPadding(false);
    }

    @Override
    protected LayoutManager createLayoutManager(Context context, @Nullable AttributeSet attrs, int defStyle) {
        return new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        //return new GridLayoutManager(context, 2);
    }
}
