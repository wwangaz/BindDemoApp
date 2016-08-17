package com.wangweimin.binddemoapp.tasks;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.wangweimin.binddemoapp.BR;
import com.wangweimin.binddemoapp.R;

/**
 * Created by wangweimin on 16/8/11.
 */

public class TasksViewModel extends BaseObservable {

    private int mTaskListSize = 0;

    private final TasksContract.Presenter mPresenter;

    private Context mContext;

    public TasksViewModel(Context context, TasksContract.Presenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    @Bindable
    public String getCurrentFilteringLabel() {
        switch (mPresenter.getFiltering()) {
            case ALL_TASKS:
                return mContext.getResources().getString(R.string.label_all);
            case COMPLETED_TASKS:
                return mContext.getResources().getString(R.string.label_completed);
            case ACTIVE_TASKS:
                return mContext.getResources().getString(R.string.label_active);
        }
        return null;
    }

    @Bindable
    public String getNoTasksLabel() {
        switch (mPresenter.getFiltering()) {
            case ALL_TASKS:
                return mContext.getResources().getString(R.string.no_tasks_all);
            case COMPLETED_TASKS:
                return mContext.getResources().getString(R.string.no_tasks_completed);
            case ACTIVE_TASKS:
                return mContext.getResources().getString(R.string.no_tasks_active);
        }
        return null;
    }

    @Bindable
    public Drawable getNoTaskIconRes(){
        switch (mPresenter.getFiltering()){
            case ALL_TASKS:
                return mContext.getResources().getDrawable(R.drawable.ic_assignment_turned_in_24dp);
            case ACTIVE_TASKS:
                return mContext.getResources().getDrawable(R.drawable.ic_check_circle_24dp);
            case COMPLETED_TASKS:
                return mContext.getResources().getDrawable(R.drawable.ic_verified_user_24dp);
        }
        return null;
    }

    @Bindable
    public boolean getTasksAddViewVisible(){
        return mPresenter.getFiltering() == TaskFilterType.ALL_TASKS;
    }

    @Bindable
    public boolean isNotEmpty(){
        return mTaskListSize != 0;
    }

    public void setTaskListSize(int taskListSize){
        mTaskListSize = taskListSize;
        notifyPropertyChanged(BR.noTaskIconRes);
        notifyPropertyChanged(BR.noTasksLabel);
        notifyPropertyChanged(BR.notEmpty);
        notifyPropertyChanged(BR.currentFilteringLabel);
        notifyPropertyChanged(BR.tasksAddViewVisible);
    }

}
