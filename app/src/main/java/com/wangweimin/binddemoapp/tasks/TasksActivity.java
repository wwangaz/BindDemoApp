package com.wangweimin.binddemoapp.tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wangweimin.binddemoapp.R;
import com.wangweimin.binddemoapp.data.source.TasksRepository;
import com.wangweimin.binddemoapp.data.source.local.TasksLocalDataSource;
import com.wangweimin.binddemoapp.data.source.remote.TasksRemoteDataSource;
import com.wangweimin.binddemoapp.util.ActivityUtils;

/**
 * Created by wangweimin on 16/8/16.
 */

public class TasksActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    private TasksPresenter mTasksPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            setupDrawerContent(navigationView);

        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (tasksFragment == null) {
            tasksFragment = TasksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }

        mTasksPresenter = new TasksPresenter(
                TasksRepository.getInstance(TasksRemoteDataSource.getInstance(), TasksLocalDataSource.getInstance(getApplicationContext()))
                , tasksFragment);

        TasksViewModel tasksViewModel = new TasksViewModel(getApplicationContext(), mTasksPresenter);

        tasksFragment.setViewModel(tasksViewModel);

        if (savedInstanceState != null) {
            TaskFilterType currentFiltering = (TaskFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_navigation_menu_item:
                        //do nothing
                        break;
                    case R.id.statistics_navigation_menu_item:
                        //go to statistics
                        break;
                    default:
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }
}
