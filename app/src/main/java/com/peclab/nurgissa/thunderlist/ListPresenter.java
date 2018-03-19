package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class ListPresenter implements ListContract.Presenter, ListContract.Interactor.OnFinishedListener {
    private ListContract.View view;
    private ListContract.Interactor interactor;
    private List<Task> tasks;
    private ListContract.AdapterView adapterView;
    private String[] category;


    public ListPresenter(ListContract.View view, ListContract.Interactor interactor) {
        this.tasks = new ArrayList<>();
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void initializeListByCategory(String[] category) {
        this.category = category;
        interactor.getAllByCategoryId(Integer.parseInt(category[0]), this);
    }

    private void getAll() {
        List<Task> list = interactor.getAll();
        tasks.addAll(list);
    }

    @Override
    public void bindAdapterViewToValue(ListContract.AdapterView adapterView, int position) {
        this.adapterView = adapterView;

        adapterView.setTitle(tasks.get(position).getTitle());
        adapterView.setChecked(false);
    }

    @Override
    public void addQuickTask(int categoryId, String value) {
        Task task = new Task();
        task.setTitle(value);
        task.setCategoryId(categoryId);

        Task result = interactor.create(task, this);
        tasks.add(0, result);

        view.notifyDataAddedToTaskList(0);
    }

    @Override
    public void checkStatusChanged(boolean value, int position) {
        if (value) {
            Task task = tasks.get(position);
            interactor.delete(task);
            tasks.remove(task);

            adapterView.setChecked(false);

            view.notifyDataRemovedFromTaskList(position);
        }
    }

    @Override
    public int getTasksCount() {
        return tasks.size();
    }

    @Override
    public void taskClicked(int position) {
        view.deliverTaskDetailTitle(tasks.get(position).getTitle());
    }

    @Override
    public void onCreateFinished() {
    }

    @Override
    public void onGetAllByCategoryIdFinished(List<Task> tasks) {
        view.setToolbarName(category[1]);

        this.tasks = tasks;
        view.notifyListDataChanged();
    }

    @Override
    public String[] getCategory() {
        return category;
    }
}
