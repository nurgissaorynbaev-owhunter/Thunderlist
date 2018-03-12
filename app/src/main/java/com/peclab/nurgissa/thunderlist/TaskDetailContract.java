package com.peclab.nurgissa.thunderlist;


public interface TaskDetailContract {

    interface View {
        void notifySubtaskAddedToDetailTask();
    }

    interface BasicAdapterView {
        void setImage(int image);
        void setText(String text);
    }

    interface SubtaskAdapterView {
        void setText(String text);
    }

    interface TaskTitleAdapterView {
        void setImage(int image);
        void setText(String text);
        void setTextHint(String text);
    }

    interface Interactor {
        interface OnFinishedListener {
            void onCreateFinished();
        }

        Task create(Task task);

        Task getByTitle(String title);
    }
}
