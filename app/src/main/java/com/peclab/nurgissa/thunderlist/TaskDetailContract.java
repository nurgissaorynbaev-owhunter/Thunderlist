package com.peclab.nurgissa.thunderlist;


import java.util.List;

public interface TaskDetailContract {

    interface View {
        void notifySubtaskAddedToDetailTask();
        void notifyDataSetChanged();

        int getColorLightOrange();
        int getColorLightBlue();
        int getColorLightGray();
        int getColorDarkGray();
    }

    interface BasicAdapterView {
        void setItem(String text, int image, int color, int textColor);
        void setItemHint(String text, int image, int color);
    }

    interface SubtaskAdapterView {
        void setText(String text);
    }

    interface TaskTitleAdapterView {
        void setItem(String text, int image, int color, int textColor);
        void setItemHint(String text, int image, int color);
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
        List<TaskDetail> getTaskDetails();

        int getViewType(int position);
        int getDetailTaskItemCount();
        void bindTaskTitleViewToValue(TaskTitleAdapterView adapterView, int position);
        void bindBasicViewToValue(BasicAdapterView adapterView, int position);
        void bindSubtaskViewToValue(SubtaskAdapterView adapterView, int position);

        void setNoteValue(Task task);
        void setScheduleValue(String value);
    }
}
