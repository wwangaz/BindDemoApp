package com.wangweimin.binddemoapp.tasks;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.wangweimin.binddemoapp.R;
import com.wangweimin.binddemoapp.addedittask.AddEditTaskActivity;
import com.wangweimin.binddemoapp.data.Task;
import com.wangweimin.binddemoapp.databinding.TaskItemBinding;
import com.wangweimin.binddemoapp.databinding.TasksFragBinding;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wangweimin on 16/8/11.
 */

public class TasksFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter mPresenter;

    private TasksViewModel mTasksViewModel;

    private TasksAdapter mListAdapter;

    public TasksFragment() {

    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TasksFragBinding tasksFragBinding = TasksFragBinding.inflate(inflater, container, false);
        tasksFragBinding.setActionHandler(mPresenter);
        tasksFragBinding.setTasks(mTasksViewModel);
        ListView listView = tasksFragBinding.tasksList;

        mListAdapter = new TasksAdapter(new ArrayList<Task>(0), mPresenter);
        listView.setAdapter(mListAdapter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addNewTask();
            }
        });

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = tasksFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        swipeRefreshLayout.setScrollUpChild(listView);

        setHasOptionsMenu(true);

        View root = tasksFragBinding.getRoot();
        return root;
    }

    public void setViewModel(TasksViewModel viewModel){
        mTasksViewModel = viewModel;
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_clear:
                mPresenter.clearCompleteTasks();
                break;
            case R.id.menu_filter:
                showFilteringPopUpWindow();
                break;
            case R.id.menu_refresh:
                mPresenter.loadTasks(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showFilteringPopUpWindow(){
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));

        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.active:
                        mPresenter.setFiltering(TaskFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.all:
                        mPresenter.setFiltering(TaskFilterType.ALL_TASKS);
                        break;
                    case R.id.complete:
                        mPresenter.setFiltering(TaskFilterType.COMPLETED_TASKS);
                        break;
                }
                mPresenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if(getView() == null)
            return;

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mListAdapter.replaceData(tasks);
        mTasksViewModel.setTaskListSize(tasks.size());
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        // TODO: 16/8/16 show task detail
    }

    @Override
    public void showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public void showMessage(String message){
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    private static class TasksAdapter extends BaseAdapter {

        private List<Task> mTasks;

        private TasksContract.Presenter mUserActionListener;

        public TasksAdapter(List<Task> tasks, TasksContract.Presenter listener) {
            mTasks = tasks;
            mUserActionListener = listener;
        }

        public void replaceData(List<Task> tasks) {
            setList(tasks);
        }

        private void setList(List<Task> tasks) {
            mTasks = tasks;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTasks == null ? 0 : mTasks.size();
        }

        @Override
        public Object getItem(int i) {
            return mTasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Task task = mTasks.get(i);
            TaskItemBinding binding;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                binding = TaskItemBinding.inflate(inflater, viewGroup, false);
            } else {
                binding = DataBindingUtil.getBinding(view);
            }

            TasksItemActionHandler handler = new TasksItemActionHandler(mUserActionListener);
            binding.setTask(task);
            binding.setActionHandler(handler);
            binding.executePendingBindings();
            return binding.getRoot();
        }
    }
}
