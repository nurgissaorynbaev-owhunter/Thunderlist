package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NavDrawerPresenter implements NavDrawerContract.Presenter {
    private NavDrawerContract.View view;
    private NavDrawerContract.Interactor interactor;
    private List<Category> categories;

    public NavDrawerPresenter(NavDrawerContract.View view, NavDrawerContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
        categories = new ArrayList<>();
    }

    @Override
    public boolean doesCategoriesExist() {
        List<Category> categories = interactor.getAll();
        if (!categories.isEmpty()) {
            Collections.swap(categories, 4, categories.size() - 1);
            this.categories = categories;
            view.notifyCategoryDataChanged();

            return true;
        }
        return false;
    }

    public void bindAdapterViewToData(NavDrawerContract.AdapterView adapterView, int position) {
        Category category = categories.get(position);
        adapterView.setItemNav(category.getName(), category.getImage(), category.getImageColor(), category.getTaskCount());
    }

    public void addNavDrawerCategory(Category category, int position) {
        categories.add(position, category);

        interactor.add(category);

        view.notifyItemCategoryAdded(position);
    }

    @Override
    public void initDefaultCategories(List<Category> categories) {
        this.categories = categories;
        interactor.initDefaultCategory(categories);

        view.notifyCategoryDataChanged();
    }

    public int getCategoryItemCount() {
        return categories.size();
    }

    public void onItemCategoryClicked(int adapterPosition) {
        Category category = categories.get(adapterPosition);

        String[] array = new String[] {String.valueOf(category.getId()), category.getName()};

        switch (category.getName()) {
            case "Inbox":
                view.deliverCategory(array);
                break;
            case "Groceries":
                view.deliverCategory(array);
                break;
            case "Work":
                view.deliverCategory(array);
                break;
            case "Completed":
                view.deliverCategory(array);
                break;
            case "Add category":
                view.createNewItemCategory(adapterPosition);
                break;
        }
    }
}
