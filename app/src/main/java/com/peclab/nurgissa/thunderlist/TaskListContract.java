package com.peclab.nurgissa.thunderlist;


public interface TaskListContract {

    interface View {
        void notifyDataAddedToTaskList(int position);
        void notifyDataRemovedFromTaskList(int position);
        void deliverTaskTitle(String value);
    }

    interface Presenter {
        void addQuickTask(String value);
    }

    interface AdapterView {
        void setTitle(String value);
        void setChecked(boolean value);
    }
}
