package org.remain4life.mvvm.views.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.remain4life.mvvm.adapters.IListAdapter;

import java.util.List;
import java.util.Objects;

public abstract class BaseRecyclerView extends RecyclerView {
    public BaseRecyclerView(Context context) {
        super(context);

        setClipToPadding(false);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayoutManager(createLayoutManager(context, attrs, defStyle));

        setClipToPadding(false);
    }

    public <T> void setData(List<T> data) {
        //noinspection unchecked
        ((IListAdapter<T>) Objects.requireNonNull(getAdapter())).setData(data);
    }

    protected abstract LayoutManager createLayoutManager(Context context, @Nullable AttributeSet attrs, int defStyle);
}
