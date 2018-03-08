package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TaskDetailPresenter {
    private List<DetailTaskItem> detailTaskItems;
    private TaskDetailContract.View view;

    public TaskDetailPresenter(TaskDetailContract.View view) {
        this.view = view;
        detailTaskItems = new ArrayList<>();
    }

    public void bindTaskTitleViewHolderToValue(TaskDetailRecyclerViewAdapter.TaskTitleViewHolder adapterView, int position) {
        DetailTaskItem dt = detailTaskItems.get(position);
        adapterView.feelView(dt.getImage(), dt.getText());
    }

    public void bindBasicViewHolderToValue(TaskDetailContract.BasicAdapterView adapterView, int position) {
        DetailTaskItem dt = detailTaskItems.get(position);
        adapterView.feelView(dt.getImage(), dt.getText());
    }

    public void bindSubtaskViewHolderToValue(TaskDetailContract.SubtaskAdapterView adapterView, int position) {
        DetailTaskItem dt = detailTaskItems.get(position);
        adapterView.feelView(dt.getText());
    }

    public void addDetailTaskItem(int image, String value, int viewType) {
        detailTaskItems.add(new DetailTaskItem(image, value, viewType));
    }

    public int getDetailTaskItemCount() {
        return detailTaskItems.size();
    }

    public int getViewType(int position) {
        return detailTaskItems.get(position).getViewType();
    }

    public void addSubtask(String value) {
        DetailTaskItem detailItem = new DetailTaskItem();
        detailItem.setText(value);
        detailItem.setViewType(DetailTaskItem.SUBTASK);

        detailTaskItems.add(DetailTaskItem.SUBTASK, detailItem);

        view.notifySubtaskAddedToDetailTask();
    }
}
