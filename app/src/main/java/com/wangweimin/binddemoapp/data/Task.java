package com.wangweimin.binddemoapp.data;

import android.support.annotation.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by wangweimin on 16/8/9.
 */

public final class Task {
    private final String mId;

    @Nullable
    private final String mTitle;

    @Nullable
    private final String mDescription;

    private boolean mCompleted;

    public Task(@Nullable String title, @Nullable String description) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    public Task(@Nullable String title, @Nullable String description, String id) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    public Task(@Nullable String title, @Nullable String description, boolean completed) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    public Task(@Nullable String title, @Nullable String description, String id, boolean completed) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getTitleForList() {
        if (mTitle != null && !mTitle.equals(""))
            return mTitle;
        else return mDescription;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty() {
        return (mTitle == null || "".equals(mTitle)) && (mDescription == null || "".equals(mDescription));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(mId, task.mId) &&
                Objects.equals(mTitle, task.mTitle) &&
                Objects.equals(mDescription, task.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Task with title " + mTitle;
    }
}
