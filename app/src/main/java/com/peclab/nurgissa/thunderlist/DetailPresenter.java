package com.peclab.nurgissa.thunderlist;


public class DetailPresenter implements DetailContract.Presenter, DetailContract.Interactor.OnFinishedListener {
    private DetailContract.View view;
    private DetailContract.Interactor interactor;
    private Task task;

    public DetailPresenter(DetailContract.View view, DetailContract.Interactor interactor) {
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
            interactor.save(this, task);
        } else {
            interactor.update(this, task);
        }
    }

    @Override
    public void onSaveFinished() {
        view.moveToMainList();
    }

    @Override
    public void onUpdateFinished() {
        view.moveToMainList();
    }
}
