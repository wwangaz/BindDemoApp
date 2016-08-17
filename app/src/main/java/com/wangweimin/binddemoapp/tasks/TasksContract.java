package com.wangweimin.binddemoapp.tasks;

import android.support.annotation.NonNull;

import com.wangweimin.binddemoapp.BasePresenter;
import com.wangweimin.binddemoapp.BaseView;
import com.wangweimin.binddemoapp.data.Task;

import java.util.List;

/**
 * Created by wangweimin on 16/8/11.
 */

public class TasksContract {
    public interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void showTaskDetailsUi(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showSuccessfullySavedMessage();

        boolean isActive();
    }

    public interface Presenter extends BasePresenter{

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openTaskDetails(@NonNull Task requestedTask);

        void completeTask(@NonNull Task completeTask);

        void activateTask(@NonNull Task activateTask);

        void clearCompleteTasks();

        void setFiltering(TaskFilterType requestType);

        TaskFilterType getFiltering();
    }
}
