package com.wangweimin.binddemoapp.addedittask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangweimin.binddemoapp.R;
import com.wangweimin.binddemoapp.data.Task;
import com.wangweimin.binddemoapp.databinding.AddtaskFragBinding;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wangweimin on 16/8/10.
 */

public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditTaskContract.Presenter mPresenter;

    private TextView mTitle;

    private TextView mDescription;

    private AddtaskFragBinding mViewDataBinding;

    public static AddEditTaskFragment newInstance(){
        return new AddEditTaskFragment();
    }

    public AddEditTaskFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNewTask()){
                    mPresenter.createTask(
                            mTitle.getText().toString(),
                            mDescription.getText().toString()
                    );
                }else {
                    mPresenter.updateTask(
                            mTitle.getText().toString(),
                            mDescription.getText().toString()
                    );
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addtask_frag, container, false);
        mTitle = (TextView) root.findViewById(R.id.add_task_title);
        mDescription = (TextView) root.findViewById(R.id.add_task_description);

        mViewDataBinding = AddtaskFragBinding.bind(root);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(mTitle, "TO DOs cannot be empty", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTask(Task task) {
        mViewDataBinding.setTask(task);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(AddEditTaskContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private boolean isNewTask(){
        return getArguments() == null || !getArguments().containsKey(ARGUMENT_EDIT_TASK_ID);
    }
}
