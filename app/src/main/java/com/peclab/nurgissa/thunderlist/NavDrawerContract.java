package com.peclab.nurgissa.thunderlist;


import java.util.List;

public interface NavDrawerContract {

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
        void initDefaultCategories(List<Category> categories);
        boolean doesCategoriesExist();
    }

    interface Interactor {
        void initDefaultCategory(List<Category> categories);
        void add(Category category);
        List<Category> getAll();
    }
}
