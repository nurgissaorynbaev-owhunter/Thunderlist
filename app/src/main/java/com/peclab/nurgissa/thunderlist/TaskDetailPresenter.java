package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TaskDetailPresenter implements TaskDetailContract.Interactor.OnFinishedListener {
    private static final String NOTE_HINT = "Add a note..";
    private static final String SCHEDULE_HINT = "Set Reminder";
    private static final String TITLE_HINT = "Task title..";
    private static final String ADD_SUBTASK_HINT = "Add a subtask";
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
        Task task = null;
        if (title != null) {
            task = interactor.getByTitle(title);
        }
        populateDetailViewItemValue(task);
    }

    private void populateDetailViewItemValue(Task task) {
        TaskDetail tdAddSubtask = new TaskDetail();
        TaskDetail tdNote = new TaskDetail();
        TaskDetail tdSchedule = new TaskDetail();
        TaskDetail tdEditValue = new TaskDetail();

        if (task != null) {
            tdNote.setText(task.getNote());
            tdSchedule.setText(String.valueOf(task.getDateTime()));
            tdEditValue.setText(task.getTitle());
        } else {
            tdNote.setText(NOTE_HINT);
            tdSchedule.setText(SCHEDULE_HINT);
            tdEditValue.setText(TITLE_HINT);
        }

        tdNote.setViewType(TaskDetail.NOTE);
        tdNote.setImage(TaskDetail.NOTE_IMAGE);
        tdSchedule.setViewType(TaskDetail.SCHEDULE);
        tdSchedule.setImage(TaskDetail.SCHEDULE_IMAGE);
        tdEditValue.setViewType(TaskDetail.EDIT_VALUE);
        tdEditValue.setImage(TaskDetail.EDIT_VALUE_IMAGE);

        tdAddSubtask.setText(ADD_SUBTASK_HINT);
        tdAddSubtask.setViewType(TaskDetail.ADD_SUBTASK);
        tdAddSubtask.setImage(TaskDetail.ADD_SUBTASK_IMAGE);

        taskDetails.add(0, tdEditValue);
        taskDetails.add(1, tdSchedule);
        taskDetails.add(2, tdNote);
        taskDetails.add(3, tdAddSubtask);
    }

}
