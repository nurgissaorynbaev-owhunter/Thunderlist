package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TasksListPresenter implements TasksListContract.Presenter {
    private TasksListContract.View view;
    private List<Task> tasks;
    private TasksListContract.AdapterView adapterView;


    public TasksListPresenter(TasksListContract.View view) {
        this.tasks = new ArrayList<>();
        this.view = view;
    }

    public void bindAdapterViewToValue(TasksListContract.AdapterView adapterView, int position) {
        this.adapterView = adapterView;

        adapterView.setTitle(tasks.get(position).getTitle());
    }

    @Override
    public void setQuickTask(String value) {
        tasks.add(new Task(value));
        view.notifyDataAddedToTasksList();
    }

    public void checkStatusChanged(boolean value, int position) {
        if (value) {
            tasks.remove(position);
            adapterView.setChecked(false);

            view.notifyDataRemovedFromTasksList(position, tasks.size());
        }
    }

    public int getTasksCount() {
        return tasks.size();
    }
}
