package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TasksListPresenter implements TasksListContract.Presenter {
    private TasksListContract.View view;
    private List<Task> tasks;
    private TasksRecyclerViewAdapter adapter;


    public TasksListPresenter(TasksListContract.View view) {
        this.tasks = new ArrayList<>();
        this.view = view;
    }

    @Override
    public void setQuickTask(String value) {
        tasks.add(new Task(value));
        adapter.notifyDataSetChanged();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setAdapter(TasksRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
