package com.peclab.nurgissa.thunderlist;


import java.util.List;

public interface TaskListContract {

    interface View {
        void notifyDataAddedToTaskList(int position);
        void notifyDataRemovedFromTaskList(int position);
        void deliverTaskDetailTitle(String value);
    }

    interface Presenter {
        void addQuickTask(String value);
        int getTasksCount();
        void bindAdapterViewToValue(TaskListContract.AdapterView adapterView, int position);
        void checkStatusChanged(boolean isChecked, int position);
        void taskClicked(int position);
    }

    interface AdapterView {
        void setTitle(String value);
        void setChecked(boolean value);
    }

    interface Interactor {
        interface OnFinishedListener {
            void onCreateFinished();
        }

        Task create(Task task, OnFinishedListener onFinishedListener);
        Task get(int id);
        List<Task> getAll();
    }
}
