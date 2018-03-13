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
        Task save(Task task);
        Task saveById(Task task, int id);
        Task getByTitle(String title);
    }

    interface Presenter {
        void initializeTaskDetail(String value);
        void addSubtask(String value);
        void saveTaskDetail();

        int getViewType(int position);
        int getDetailTaskItemCount();
        void bindTaskTitleViewToValue(TaskTitleAdapterView adapterView, int position);
        void bindBasicViewToValue(BasicAdapterView adapterView, int position);
        void bindSubtaskViewToValue(SubtaskAdapterView adapterView, int position);
    }
}
