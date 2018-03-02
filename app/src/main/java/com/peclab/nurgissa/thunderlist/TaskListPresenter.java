package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TaskListPresenter implements TaskListContract.Presenter {
    private TaskListContract.View view;
    private List<Task> tasks;
    private TaskListContract.AdapterView adapterView;


    public TaskListPresenter(TaskListContract.View view) {
        this.tasks = new ArrayList<>();
        this.view = view;
    }

    public void bindAdapterViewToValue(TaskListContract.AdapterView adapterView, int position) {
        this.adapterView = adapterView;

        adapterView.setTitle(tasks.get(position).getTitle());
    }

    @Override
    public void setQuickTask(String value) {
        Task task = new Task();
        task.setTitle(value);

        tasks.add(task);
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

    public void onItemClick(int position) {
        view.deliverTaskTitle(tasks.get(position).getTitle());
    }
}
