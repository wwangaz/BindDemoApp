package com.wangweimin.binddemoapp.addedittask;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wangweimin.binddemoapp.data.Task;
import com.wangweimin.binddemoapp.data.source.TasksDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wangweimin on 16/8/10.
 */

public class AddTaskPresenter implements AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallback {
    @NonNull
    private final TasksDataSource mTaskRepository;

    @NonNull
    private final AddEditTaskContract.View mAddTaskView;

    @Nullable
    private String mTaskId;

    public AddTaskPresenter(@Nullable String taskId, @NonNull TasksDataSource taskRepository,
                            @NonNull AddEditTaskContract.View addTaskView) {
        mTaskId = taskId;
        mTaskRepository = checkNotNull(taskRepository);
        mAddTaskView = checkNotNull(addTaskView);

        mAddTaskView.setPresenter(this);
    }

    @Override
    public void onTaskLoaded(Task task) {
        if (mAddTaskView.isActive())
            mAddTaskView.setTask(task);
    }

    @Override
    public void onDataNotAvailable() {
        if (mAddTaskView.isActive())
            mAddTaskView.showEmptyTaskError();
    }

    @Override
    public void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty())
            mAddTaskView.showEmptyTaskError();
        else {
            mTaskRepository.saveTask(newTask);
            mAddTaskView.showTasksList();
        }
    }

    @Override
    public void updateTask(String title, String description) {
        if (mTaskId == null)
            throw new RuntimeException("updateTask() was called but task is new.");

        mTaskRepository.saveTask(new Task(title, description, mTaskId));
        mAddTaskView.showTasksList();
    }

    @Override
    public void populateTask() {
        if (mTaskId == null)
            throw new RuntimeException("populateTask() was called but task is new.");

        mTaskRepository.getTask(mTaskId, this);
    }

    @Override
    public void start() {
        if (mTaskId != null)
            populateTask();
    }
}
