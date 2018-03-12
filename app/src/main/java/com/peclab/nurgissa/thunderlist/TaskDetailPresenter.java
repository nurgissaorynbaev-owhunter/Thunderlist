package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TaskDetailPresenter implements TaskDetailContract.Interactor.OnFinishedListener {
    private List<TaskDetail> taskDetails;
    private TaskDetailContract.View view;
    private TaskDetailContract.Interactor interactor;

    public TaskDetailPresenter(TaskDetailContract.View view, TaskDetailContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
        taskDetails = new ArrayList<>();
    }

    public void bindTaskTitleViewToValue(TaskDetailRecyclerViewAdapter.TaskTitleViewHolder adapterView, int position) {
        TaskDetail td = taskDetails.get(position);

        adapterView.setImage(td.getImage());

        if (td.getText().equals("Task title..")) {
            adapterView.setTextHint(td.getText());
        } else {
            adapterView.setText(td.getText());
        }
    }

    public void bindBasicViewToValue(TaskDetailContract.BasicAdapterView adapterView, int position) {
        TaskDetail td = taskDetails.get(position);
        adapterView.setText(td.getText());
        adapterView.setImage(td.getImage());
    }

    public void bindSubtaskViewToValue(TaskDetailContract.SubtaskAdapterView adapterView, int position) {
        TaskDetail td = taskDetails.get(position);
        adapterView.setText(td.getText());
    }

    public void addDetailTaskItem(int image, String value, int viewType) {
//        Task task = new Task();
//        task.setText(value);
//        task.setImage(image);
//        task.setItemViewType(new DetailItemViewType(viewType));
//
//        Task result = interactor.create(task);
//
//        taskDetails.add(result);
    }

    public int getDetailTaskItemCount() {
        return taskDetails.size();
    }

    public int getViewType(int position) {
        return taskDetails.get(position).getViewType();
    }

    public void addSubtask(String value) {
//        DetailTaskItem detailItem = new DetailTaskItem();
//        detailItem.setText(value);
//        detailItem.setType(DetailTaskItem.SUBTASK);
//
//        taskDetails.add(DetailTaskItem.SUBTASK, detailItem);
//
//        view.notifySubtaskAddedToDetailTask();
    }

    @Override
    public void onCreateFinished() {
        System.out.println("Show toast -> task created!");
    }

    public void initializeTaskDetail(String title) {
        Task task = new Task();
        if (title != null) {
            task = interactor.getByTitle(title);
        }

        populateEditValueItem(task);
        populateScheduleItem(task);
        populateNoteItem(task);
        populateAddSubtaskItem(task);
    }

    private void populateAddSubtaskItem(Task task) {
        TaskDetail td = new TaskDetail();
        td.setText("Add a subtask");
        td.setViewType(TaskDetail.ADD_SUBTASK);
        td.setImage(TaskDetail.ADD_SUBTASK_IMAGE);

        taskDetails.add(td);
    }

    private void populateNoteItem(Task task) {
        TaskDetail td = new TaskDetail();

        if (task.getNote() != null) {
            td.setText(task.getNote());
        } else {
            td.setText("Add a note..");
        }

        td.setViewType(TaskDetail.NOTE);
        td.setImage(TaskDetail.NOTE_IMAGE);

        taskDetails.add(td);
    }

    private void populateScheduleItem(Task task) {
        TaskDetail td = new TaskDetail();

        if (task.getDateTime() != null) {
            td.setText(String.valueOf(task.getDateTime()));
        } else {
            td.setText("Set Reminder");
        }

        td.setViewType(TaskDetail.SCHEDULE);
        td.setImage(TaskDetail.SCHEDULE_IMAGE);

        taskDetails.add(td);
    }

    private void populateEditValueItem(Task task) {
        TaskDetail td = new TaskDetail();

        if (task.getTitle() != null) {
            td.setText(task.getTitle());
        } else {
            td.setText("Task title..");
        }
        td.setViewType(TaskDetail.EDIT_VALUE);
        td.setImage(TaskDetail.EDIT_VALUE_IMAGE);

        taskDetails.add(td);
    }
}
