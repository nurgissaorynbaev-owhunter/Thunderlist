package com.peclab.nurgissa.thunderlist;


public interface TaskDetailContract {

    interface BasicAdapterView {
        void feelView(int image, String value);
    }

    interface SubtaskAdapterView {
        void feelView(String value);
    }
}
