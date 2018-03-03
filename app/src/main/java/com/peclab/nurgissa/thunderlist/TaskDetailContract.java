package com.peclab.nurgissa.thunderlist;


public interface TaskDetailContract {

    interface View {
        void notifySubtaskAddedToDetailTask();
    }

    interface BasicAdapterView {
        void feelView(int image, String value);
    }

    interface SubtaskAdapterView {
        void feelView(String value);
    }

    interface BasicAdapterValueView {
        void feelView(int image, String value);
    }
}
