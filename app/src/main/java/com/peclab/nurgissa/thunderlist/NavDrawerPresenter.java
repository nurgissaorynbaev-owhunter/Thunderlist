package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class NavDrawerPresenter {
    private NavDrawerContract.View view;
    private List<Category> categories;

    public NavDrawerPresenter(NavDrawerContract.View view) {
        this.view = view;
        categories = new ArrayList<>();
    }

    public void bindAdapterViewToData(NavDrawerContract.AdapterView adapterView, int position) {
        Category category = categories.get(position);
        adapterView.setItemNav(category.getTitle(), category.getImage(), category.getImageColor(), category.getItemCount());
    }

    public void addNavDrawerCategory(Category category) {
        categories.add(category);
    }

    public int getCategoryItemCount() {
        return categories.size();
    }
}
