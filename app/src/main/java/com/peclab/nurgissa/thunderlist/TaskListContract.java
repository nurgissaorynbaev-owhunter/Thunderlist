package com.peclab.nurgissa.thunderlist;


import java.util.List;

public interface TaskListContract {

    interface View {
        void notifyDataAddedToTaskList(int position);
        void notifyDataRemovedFromTaskList(int position);
        void notifyListDataChanged();
        void deliverTaskDetailTitle(String value, String[] category);
        void setToolbarName(String value);
        boolean checkDefaultTaskCategory();
    }

    interface Presenter {
        void addQuickTask(String value);
        int getTasksCount();
        void bindAdapterViewToValue(TaskListContract.AdapterView adapterView, int position);
        void checkStatusChanged(boolean isChecked, int position);
        void taskClicked(int position);
        void initializeListByCategory(String[] category);
        String[] getCategory();
        boolean checkDefaultTaskCategory();
        void changeTaskCategoryName(String value);
        void deleteTaskCategory();
    }

    interface AdapterView {
        void setTitle(String value);
        void setChecked(boolean value);
        void setCrossedItem();
        void hideCheckbox();
    }

    interface Interactor {
        interface OnFinishedListener {
            void onCreateFinished();
        }

        Task create(Task task, OnFinishedListener onFinishedListener);
        Task get(int id);
        List<Task> getAllByCategoryId(int categoryId, OnFinishedListener onFinishedListener);
        void moveTaskToCompleteCategory(Task task);
        void moveTaskToPreviousCategory(Task task);
        List<Task> getAllCompleted();
        void updateCategoryName(String[] category);
        void deleteCategory(String[] category);
    }
}
