package com.peclab.nurgissa.thunderlist;


import java.util.List;

public interface TaskDetailContract {

    interface View {
        void moveToMainList(String[] category);
        void initDetailTitle(String title);
        void initDetailReminder(String reminder);
        void initDetailNote(String note);
        void setToolbarTitle(String value);
    }

    interface Presenter {
        void initDetailFromDatabase(String title);
        void addTextTitle(String title);
        void addTextNote(String note);
        void saveDetail();
        void addReminderDate(String date);
        void addReminderTime(String time);
        void setCategory(String[] category);
        Task getTask();
        void deleteTask();
        String[] getAllCategoryName();
        void setSelectedCategory(String value);
    }

    interface Interactor {
        interface OnFinishedListener {
            void onSaveFinished();
            void onUpdateFinished();
            void onDeleteFinished();
        }
        void save(OnFinishedListener onFinishedListener, Task task);
        void update(OnFinishedListener onFinishedListener, Task task);
        Task getByTitle(String title);
        void delete(OnFinishedListener onFinishedListener, Task task);
        List<TaskCategory> getAllCategory();
    }
}
