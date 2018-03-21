package com.peclab.nurgissa.thunderlist;


import java.util.List;

public interface TaskListContract {

    interface View {
        void notifyDataAddedToTaskList(int position);
        void notifyDataRemovedFromTaskList(int position);
        void notifyListDataChanged();
        void deliverTaskDetailTitle(String value);
        void setToolbarName(String value);
    }

    interface Presenter {
        void addQuickTask(int categoryId, String value);
        int getTasksCount();
        void bindAdapterViewToValue(TaskListContract.AdapterView adapterView, int position);
        void checkStatusChanged(boolean isChecked, int position);
        void taskClicked(int position);
        void initializeListByCategory(String[] category);
        String[] getCategory();
    }

    interface AdapterView {
        void setTitle(String value);
        void setChecked(boolean value);
    }

    interface Interactor {
        interface OnFinishedListener {
            void onCreateFinished();
            void onGetAllByCategoryIdFinished(List<Task> tasks);
        }

        Task create(Task task, OnFinishedListener onFinishedListener);
        Task get(int id);
        void getAllByCategoryId(int categoryId, OnFinishedListener onFinishedListener);
        List<Task> getAll();
        void moveToCompleteCategory(Task task);
    }
}
