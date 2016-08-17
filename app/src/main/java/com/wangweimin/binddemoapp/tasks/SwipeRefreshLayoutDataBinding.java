package com.wangweimin.binddemoapp.tasks;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by wangweimin on 16/8/11.
 */

public class SwipeRefreshLayoutDataBinding {
    @BindingAdapter("android:onRefresh")
    public static void setSwipeRefreshLayoutOnRefreshListener(ScrollChildSwipeRefreshLayout view,
                                                              final TasksContract.Presenter presenter){
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadTasks(true);
            }
        });
    }
}
