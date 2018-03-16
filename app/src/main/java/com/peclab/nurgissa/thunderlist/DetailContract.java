package com.peclab.nurgissa.thunderlist;


public interface DetailContract {

    interface View {
        void moveToMainList();
        void initDetailTitle(String title);
        void initDetailReminder(String reminder);
        void initDetailNote(String note);
    }

    interface Presenter {
        void initDetailFromDatabase(String title);
        void addTextTitle(String title);
        void addTextNote(String note);
        void saveDetail();
        void addReminderDate(String date);
        void addReminderTime(String time);
    }

    interface Interactor {
        interface OnFinishedListener {
            void onSaveFinished();
            void onUpdateFinished();
        }
        void save(OnFinishedListener onFinishedListener, Task task);
        void update(OnFinishedListener onFinishedListener, Task task);
        Task getByTitle(String title);
    }
}
