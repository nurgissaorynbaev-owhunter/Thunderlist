package com.peclab.nurgissa.thunderlist;


public interface TasksListContract {

    interface View {

    }

    interface Presenter {
        void setQuickTask(String value);
    }

    interface RecycleViewAdapterView {
        void notifyDataChanged();
    }
}
