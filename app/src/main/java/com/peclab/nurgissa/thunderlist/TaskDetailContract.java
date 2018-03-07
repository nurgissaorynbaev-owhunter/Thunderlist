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

    interface TaskTitleAdapterView {
        void feelView(int image, String value);
    }

    interface AdapterView {
        void createTaskTitleViewHolder();
        void createSubtastViewHolder();
        void createBasicViewHolder();

        void bindTaskTitleViewHolder();
        void bindBasicViewHolder();
        void bindSubtaskViewHolder();
    }
}
