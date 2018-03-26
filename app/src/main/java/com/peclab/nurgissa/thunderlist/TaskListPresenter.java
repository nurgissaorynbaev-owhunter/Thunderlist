package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TaskListPresenter implements TaskListContract.Presenter, TaskListContract.Interactor.OnFinishedListener {
    private TaskListContract.View view;
    private TaskListContract.Interactor interactor;
    private List<Task> tasks;
    private TaskListContract.AdapterView adapterView;
    private String[] category;



    public TaskListPresenter(TaskListContract.View view, TaskListContract.Interactor interactor) {
        this.tasks = new ArrayList<>();
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void initializeListByCategory(String[] category) {
        this.category = category;

        if (category[1].equals("Completed")) {
            tasks = interactor.getAllCompleted();
            updateTaskList();

        } else {
            tasks = interactor.getAllByCategoryId(Integer.parseInt(category[0]), this);
            updateTaskList();
        }
    }

    @Override
    public void bindAdapterViewToValue(TaskListContract.AdapterView adapterView, int position) {
        this.adapterView = adapterView;

        if (category[1].equals("Completed")) {
            adapterView.setCrossedItem();
            adapterView.setChecked(true);
        }
        adapterView.setTitle(tasks.get(position).getTitle());
    }

    @Override
    public void addQuickTask(String value) {
        Task task = new Task();
        task.setTitle(value);
        task.setCompletedFlag(0);
        task.setCategoryId(Integer.parseInt(category[0]));

        Task result = interactor.create(task, this);
        tasks.add(0, result);

        view.notifyDataAddedToTaskList(0);
    }

    @Override
    public void checkStatusChanged(boolean isChecked, int position) {
        Task task = tasks.get(position);

        if (isChecked) {
            if (!checkForCompleted(task)) {
                task.setCompletedFlag(1);

                interactor.moveTaskToCompleteCategory(task);
                tasks.remove(task);

                adapterView.setChecked(false);

                view.notifyDataRemovedFromTaskList(position);
            }
        }

        if (!isChecked) {
            if (checkForCompleted(task)) {
                task.setCompletedFlag(0);

                interactor.moveTaskToPreviousCategory(task);
                tasks.remove(task);

                adapterView.setChecked(false);

                view.notifyDataRemovedFromTaskList(position);
            }
        }
    }

    private boolean checkForCompleted(Task task) {
        if (task.getCompletedFlag() == 1)
            return true;

        return false;
    }

    @Override
    public int getTasksCount() {
        return tasks.size();
    }

    @Override
    public void taskClicked(int position) {
        view.deliverTaskDetailTitle(tasks.get(position).getTitle(), category);
    }

    @Override
    public void onCreateFinished() {
    }

    private void updateTaskList() {
        view.setToolbarName(category[1]);
        view.notifyListDataChanged();
    }

    @Override
    public String[] getCategory() {
        return category;
    }

    @Override
    public boolean checkDefaultTaskCategory() {
        for (int i = 0; i < 3; i++) {
            if (Integer.parseInt(category[0]) == i)
                return true;
        }
        return false;
    }

    @Override
    public void changeTaskCategoryName(String value) {
        category[1] = value;
        view.setToolbarName(value);

        interactor.updateCategoryName(category);
    }

    @Override
    public void deleteTaskCategory() {
        interactor.deleteCategory(category);
    }
}
