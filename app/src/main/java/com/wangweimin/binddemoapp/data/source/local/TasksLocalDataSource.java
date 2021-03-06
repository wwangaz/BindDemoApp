package com.wangweimin.binddemoapp.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.wangweimin.binddemoapp.data.Task;
import com.wangweimin.binddemoapp.data.source.TasksDataSource;
import com.wangweimin.binddemoapp.data.source.local.TasksPersistenceContract.TaskEntry;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wangweimin on 16/8/9.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;

    private TasksDBHelper mDBHelper;

    private TasksLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDBHelper = new TasksDBHelper(context);
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null)
            INSTANCE = new TasksLocalDataSource(context);
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED
        };

        Cursor c = db.query(TaskEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                boolean completed = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                Task task = new Task(title, description, itemId, completed);
                tasks.add(task);
            }
        }

        if (c != null) {
            c.close();
        }

        db.close();

        if (tasks.isEmpty())
            callback.onDataNotAvailable();
        else
            callback.onTasksLoaded(tasks);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED
        };

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId};

        Cursor c = db.query(TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Task task = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
            String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
            String description = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
            boolean completed = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;

            task = new Task(title, description, itemId, completed);
        }

        if (c != null)
            c.close();

        db.close();

        if (task != null)
            callback.onTaskLoaded(task);
        else callback.onDataNotAvailable();
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

        db.insert(TaskEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void completeTask(@NonNull Task task) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull Task task) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTask() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";
        String[] selectionArgs = {"1"};

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        db.delete(TaskEntry.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId};

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }
}
