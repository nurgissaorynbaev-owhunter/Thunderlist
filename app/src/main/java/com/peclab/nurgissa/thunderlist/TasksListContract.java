package com.peclab.nurgissa.thunderlist;


public interface TasksListContract {

    interface View {

    }

    interface Presenter {
        void setQuickTask(String value);
    }

    interface AdapterView {
        void setTitle(String value);
        void notifyDataChanged();
        void setChecked(boolean value);
    }
}
