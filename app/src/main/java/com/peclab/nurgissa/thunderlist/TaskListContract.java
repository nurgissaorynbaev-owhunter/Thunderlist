package com.peclab.nurgissa.thunderlist;


public interface TaskListContract {

    interface View {
        void notifyDataAddedToTasksList();
        void notifyDataRemovedFromTasksList(int position, int itemCount);
        void deliverTaskTitle(String value);
    }

    interface Presenter {
        void setQuickTask(String value);
    }

    interface AdapterView {
        void setTitle(String value);
        void setChecked(boolean value);
    }
}
