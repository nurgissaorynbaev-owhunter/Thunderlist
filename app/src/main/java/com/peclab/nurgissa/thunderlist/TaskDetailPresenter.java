package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TaskDetailPresenter {
    private List<DetailTask> detailTasks;

    public TaskDetailPresenter() {
        detailTasks = new ArrayList<>();
    }

    public void bindBasicViewHolderToData(TaskDetailContract.BasicAdapterView adapterView, int position) {
        DetailTask dt = detailTasks.get(position);
        adapterView.feelView(dt.getImage(), dt.getHint());
    }

    public void bindSubtaskViewHolderToData(TaskDetailContract.SubtaskAdapterView adapterView, int position) {
    }

    public void addDetailTask(int image, String value) {
        detailTasks.add(new DetailTask(image, value));
    }

    public int getDetailTaskCount() {
        return detailTasks.size();
    }
}
