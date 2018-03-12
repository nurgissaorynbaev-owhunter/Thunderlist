package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class NavDrawerPresenter {
    private NavDrawerContract.View view;
    private List<TaskCategory> categories;

    public NavDrawerPresenter(NavDrawerContract.View view) {
        this.view = view;
        categories = new ArrayList<>();
    }

    public void bindAdapterViewToData(NavDrawerContract.AdapterView adapterView, int position) {
        TaskCategory taskCategory = categories.get(position);
        adapterView.setItemNav(taskCategory.getTitle(), taskCategory.getImage(), taskCategory.getImageColor(), taskCategory.getItemCount());
    }

    public void addNavDrawerCategory(TaskCategory taskCategory, int position) {
        categories.add(position, taskCategory);
        view.notifyItemCategoryAdded(position);
    }

    public void addNavDrawerCategory(TaskCategory taskCategory) {
        categories.add(taskCategory);
    }

    public int getCategoryItemCount() {
        return categories.size();
    }

    public void onItemCategoryClicked(int adapterPosition) {
        int lastIndex = categories.size() - 1;

        if (lastIndex == adapterPosition) {
            view.createNewItemCategory(adapterPosition);
        }
    }
}
