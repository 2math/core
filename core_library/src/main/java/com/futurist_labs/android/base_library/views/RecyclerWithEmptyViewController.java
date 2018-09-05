package com.futurist_labs.android.base_library.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Galeen on 4/24/2018.
 * Controller btw RecyclerView and empty View
 * on set adapter according to the items count will show and load the adapter
 * in the RecyclerView or will hide it and show the empty view (which is probably a TextView or ImageView)
 */
public class RecyclerWithEmptyViewController {
    private RecyclerView recyclerView;
    private View emptyView;

    public RecyclerWithEmptyViewController(RecyclerView recyclerView, View emptyView) {
        this.recyclerView = recyclerView;
        this.emptyView = emptyView;
    }

    public boolean setAdapter(RecyclerView.Adapter adapter) {
        return setAdapter(adapter, adapter.getItemCount());
    }

    public boolean setAdapter(RecyclerView.Adapter adapter, int itemsCount) {
        return setAdapter(adapter, itemsCount, false);
    }

    /**
     *
     * @param adapter adapter
     * @param itemsCount if 0 and noEmptyView = false will show EmptyView else will show the list
     * @param noEmptyView true just show empty list
     * @return true if list is shown
     */
    public boolean setAdapter(RecyclerView.Adapter adapter, int itemsCount, boolean noEmptyView) {
        boolean res;
            if (!noEmptyView && itemsCount == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                res = false;
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                res = true;
            }
        recyclerView.setAdapter(adapter);
        return res;
    }
}
