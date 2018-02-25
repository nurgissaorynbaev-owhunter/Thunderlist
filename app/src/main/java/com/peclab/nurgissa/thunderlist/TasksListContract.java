package com.peclab.nurgissa.thunderlist;


public interface TasksListContract {

    interface View {
        void notifyDataAddedToTasksList();
        void notifyDataRemovedFromTasksList(int position, int itemCount);
    }

    interface Presenter {
        void setQuickTask(String value);
    }

    interface AdapterView {
        void setTitle(String value);
        void setChecked(boolean value);
    }
}
