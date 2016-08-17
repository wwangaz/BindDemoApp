package com.wangweimin.binddemoapp.tasks;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.wangweimin.binddemoapp.addedittask.AddEditTaskActivity;
import com.wangweimin.binddemoapp.data.Task;
import com.wangweimin.binddemoapp.data.source.TasksDataSource;
import com.wangweimin.binddemoapp.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wangweimin on 16/8/12.
 */

public class TasksPresenter implements TasksContract.Presenter {

    private final TasksRepository mTasksRepository;

    private final TasksContract.View mTasksView;

    private TaskFilterType mCurrentFilterType = TaskFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public TasksPresenter(@NonNull TasksRepository tasksRepository, @NonNull TasksContract.View tasksView){
        mTasksRepository = checkNotNull(tasksRepository);
        mTasksView = checkNotNull(tasksView);

        mTasksView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if(AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode)
            mTasksView.showSuccessfullySavedMessage();
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    public void loadTasks(boolean forceUpdate, final boolean showLoadingUI){
        if (showLoadingUI)
            mTasksView.setLoadingIndicator(true);

        if(forceUpdate)
            mTasksRepository.refreshTasks();

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                for(Task task : tasks){
                    switch (mCurrentFilterType){
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive())
                                tasksToShow.add(task);
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted())
                                tasksToShow.add(task);
                            break;
                        default:
                            tasksToShow.add(task);
                    }
                }

                if(showLoadingUI)
                    mTasksView.setLoadingIndicator(false);

                mTasksView.showTasks(tasksToShow);

            }

            @Override
            public void onDataNotAvailable() {
                if(!mTasksView.isActive())
                    return;

                mTasksView.showLoadingTasksError();
            }
        });
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {
        checkNotNull(requestedTask, "requestedTask cannot be null");
        mTasksView.showTaskDetailsUi(requestedTask.getId());
    }

    @Override
    public void completeTask(@NonNull Task completeTask) {
        checkNotNull(completeTask, "completeTask cannot be null");
        mTasksRepository.completeTask(completeTask);
        mTasksView.showTaskMarkedComplete();
    }

    @Override
    public void activateTask(@NonNull Task activateTask) {
        checkNotNull(activateTask, "activateTask cannot be null");
        mTasksRepository.activateTask(activateTask);
        mTasksView.showTaskMarkedActive();
    }

    @Override
    public void clearCompleteTasks() {
        mTasksRepository.clearCompletedTask();
        mTasksView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    @Override
    public void setFiltering(TaskFilterType requestType) {
        mCurrentFilterType = requestType;
    }

    @Override
    public TaskFilterType getFiltering() {
        return mCurrentFilterType;
    }

    @Override
    public void start() {
        loadTasks(false);
    }
}
