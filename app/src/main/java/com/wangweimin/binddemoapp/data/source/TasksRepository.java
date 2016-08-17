package com.wangweimin.binddemoapp.data.source;

import android.support.annotation.NonNull;

import com.wangweimin.binddemoapp.data.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wangweimin on 16/8/9.
 */

public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private final TasksDataSource mTasksRemoteDataSource;

    private final TasksDataSource mTasksLocalDataSource;

    Map<String, Task> mCachedTasks;

    boolean mCachedIsDirty = false;

    private TasksRepository(@NonNull TasksDataSource tasksRemoteDataSource,
                            @NonNull TasksDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = tasksRemoteDataSource;
        mTasksLocalDataSource = tasksLocalDataSource;
    }

    public static TasksRepository getInstance(TasksDataSource tasksRemoteDataSource,
                                              TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        if (mCachedTasks != null && !mCachedIsDirty) {
            callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
            return;
        }

        if (mCachedIsDirty) {
            getTasksFromRemoteDataSource(callback);
        } else {
            mTasksLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.saveTask(task);
        mTasksRemoteDataSource.saveTask(task);

        if (mCachedTasks == null)
            mCachedTasks = new LinkedHashMap<>();

        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.completeTask(task);
        mTasksRemoteDataSource.completeTask(task);

        Task completeTask = new Task(task.getTitle(), task.getDescription(), task.getId(), task.isCompleted());

        if (mCachedTasks == null)
            mCachedTasks = new LinkedHashMap<>();

        mCachedTasks.put(completeTask.getId(), completeTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.activateTask(task);
        mTasksRemoteDataSource.activateTask(task);

        Task activateTask = new Task(task.getTitle(), task.getDescription(), task.getId(), task.isCompleted());

        if (mCachedTasks == null)
            mCachedTasks = new LinkedHashMap<>();

        mCachedTasks.put(activateTask.getId(), activateTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTask() {
        mTasksLocalDataSource.clearCompletedTask();
        mTasksRemoteDataSource.clearCompletedTask();

        if (mCachedTasks == null)
            mCachedTasks = new LinkedHashMap<>();

        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted())
                it.remove();
        }
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);
        if (cachedTask != null) {
            //一级缓存
            callback.onTaskLoaded(cachedTask);
            return;
        }

        //二级缓存
        mTasksLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    //设置标志位,去更新
    @Override
    public void refreshTasks() {
        mCachedIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();
        mTasksRemoteDataSource.deleteAllTasks();

        if (mCachedTasks == null)
            mCachedTasks = new LinkedHashMap<>();

        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        checkNotNull(taskId);
        mTasksLocalDataSource.deleteTask(taskId);
        mTasksRemoteDataSource.deleteTask(taskId);

        mCachedTasks.remove(taskId);
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback) {
        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null)
            mCachedTasks = new LinkedHashMap<>();

        mCachedTasks.clear();

        for (Task task : tasks)
            mCachedTasks.put(task.getId(), task);

        mCachedIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        mTasksLocalDataSource.deleteAllTasks();
        for (Task task : tasks)
            mTasksLocalDataSource.saveTask(task);
    }

    private Task getTaskWithId(String taskId) {
        checkNotNull(taskId);
        if (mCachedTasks == null || mCachedTasks.isEmpty())
            return null;
        else return mCachedTasks.get(taskId);
    }
}
