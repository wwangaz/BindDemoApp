package com.wangweimin.binddemoapp.tasks;

import com.wangweimin.binddemoapp.data.Task;

/**
 * Created by wangweimin on 16/8/11.
 */

public class TasksItemActionHandler {
    private TasksContract.Presenter mListener;

    public TasksItemActionHandler(TasksContract.Presenter listener){
        mListener = listener;
    }

    public void completeChanged(Task task, boolean isChecked){
        if(isChecked)
            mListener.completeTask(task);
        else mListener.activateTask(task);
    }

    public void taskClicked(Task task){
        mListener.openTaskDetails(task);
    }
}
