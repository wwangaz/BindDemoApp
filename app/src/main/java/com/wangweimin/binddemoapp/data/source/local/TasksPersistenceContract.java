package com.wangweimin.binddemoapp.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by wangweimin on 16/8/9.
 */

public class TasksPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TasksPersistenceContract(){}

    public static abstract class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}
