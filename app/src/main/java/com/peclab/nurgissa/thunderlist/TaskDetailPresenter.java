package com.peclab.nurgissa.thunderlist;


import java.util.List;

public class TaskDetailPresenter implements TaskDetailContract.Presenter, TaskDetailContract.Interactor.OnFinishedListener {
    private TaskDetailContract.View view;
    private TaskDetailContract.Interactor interactor;
    private Task task;
    private String[] category;
    private List<TaskCategory> categories;

    public TaskDetailPresenter(TaskDetailContract.View view, TaskDetailContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
        this.task = new Task();
    }

    @Override
    public void initDetailFromDatabase(String title) {
        task = interactor.getByTitle(title);

        view.initDetailTitle(task.getTitle());

        if (task.getReminderDate() != null)
            view.initDetailReminder(task.getReminderDate() + " at " + task.getReminderTime());

        if (task.getNote() != null)
            view.initDetailNote(task.getNote());
    }

    @Override
    public void addTextTitle(String title) {
        task.setTitle(title);
    }

    @Override
    public void addTextNote(String note) {
        task.setNote(note);
    }

    @Override
    public void addReminderDate(String date) {
        task.setReminderDate(date);
    }

    @Override
    public void addReminderTime(String time) {
        task.setReminderTime(time);

        view.initDetailReminder(task.getReminderDate() + " at " + task.getReminderTime());
    }

    @Override
    public void saveDetail() {
        if (task.getId() == 0) {
            task.setCategoryId(Integer.parseInt(category[0]));
            interactor.save(this, task);
        } else {
            interactor.update(this, task);
        }
    }

    @Override
    public void onSaveFinished() {
        view.moveToMainList(category);
    }

    @Override
    public void onUpdateFinished() {
        view.moveToMainList(category);
    }

    @Override
    public void setCategory(String[] category) {
        this.category = category;
        view.setToolbarTitle(category[1]);
    }

    @Override
    public Task getTask() {
        return task;
    }

    @Override
    public void deleteTask() {
        interactor.delete(this, task);
    }

    @Override
    public void onDeleteFinished() {
        view.moveToMainList(category);
    }

    @Override
    public String[] getAllCategoryName() {
        categories = interactor.getAllCategory();
        String[] categoryName = new String[categories.size()];
        for (int i = 0; i < categoryName.length; i++) {
            categoryName[i] = categories.get(i).getName();
        }

        return categoryName;
    }

    @Override
    public void setSelectedCategory(String value) {
        for (TaskCategory tc : categories) {
            if (tc.getName().equals(value)) {
                category[0] = String.valueOf(tc.getId());
                category[1] = tc.getName();
            }
        }
        task.setCategoryId(Integer.parseInt(category[0]));
    }
}
