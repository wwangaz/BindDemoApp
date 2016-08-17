package com.wangweimin.binddemoapp.addedittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wangweimin.binddemoapp.R;
import com.wangweimin.binddemoapp.data.source.TasksRepository;
import com.wangweimin.binddemoapp.data.source.local.TasksLocalDataSource;
import com.wangweimin.binddemoapp.data.source.remote.TasksRemoteDataSource;
import com.wangweimin.binddemoapp.util.ActivityUtils;

/**
 * Created by wangweimin on 16/8/10.
 */

public class AddEditTaskActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AddEditTaskFragment addEditTaskFragment = (AddEditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String taskId = null;
        if(addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment.newInstance();

            if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                taskId = getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID);
                actionBar.setTitle(R.string.edit_task);
                Bundle bundle = new Bundle();
                bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                addEditTaskFragment.setArguments(bundle);
            }else {
                actionBar.setTitle(R.string.add_task);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), addEditTaskFragment, R.id.contentFrame);
        }

        new AddTaskPresenter(taskId,
                TasksRepository.getInstance(TasksRemoteDataSource.getInstance(), TasksLocalDataSource.getInstance(getApplicationContext())),
                        addEditTaskFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
