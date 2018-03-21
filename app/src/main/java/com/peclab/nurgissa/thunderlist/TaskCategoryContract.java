package com.peclab.nurgissa.thunderlist;


import java.util.List;

public interface TaskCategoryContract {

    interface View {
        void createNewItemCategory(int position);
        void notifyItemCategoryAdded(int position);
        void notifyCategoryDataChanged();
        void deliverCategory(String[] category);
    }

    interface AdapterView {
        void setItemNav(String title, int imageView, int imageColor, int itemCount);
    }

    interface Presenter {
        void initDefaultCategories(List<TaskCategory> categories);
        boolean doesCategoriesExist();
    }

    interface Interactor {
        void initDefaultCategory(List<TaskCategory> categories);
        void add(TaskCategory taskCategory);
        List<TaskCategory> getAll();
    }
}
