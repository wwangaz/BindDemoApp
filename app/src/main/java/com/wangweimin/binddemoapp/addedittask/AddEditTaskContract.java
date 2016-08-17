package com.wangweimin.binddemoapp.addedittask;

import com.wangweimin.binddemoapp.BasePresenter;
import com.wangweimin.binddemoapp.BaseView;
import com.wangweimin.binddemoapp.data.Task;

/**
 * Created by wangweimin on 16/8/10.
 */

public interface AddEditTaskContract {
    interface View extends BaseView<Presenter> {
        void showEmptyTaskError();

        void showTasksList();

        void setTask(Task task);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void createTask(String title, String description);

        void updateTask(String title, String description);

        void populateTask();
    }
}
