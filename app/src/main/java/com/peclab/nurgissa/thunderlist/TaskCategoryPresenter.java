package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskCategoryPresenter implements TaskCategoryContract.Presenter {
    private TaskCategoryContract.View view;
    private TaskCategoryContract.Interactor interactor;
    private List<TaskCategory> categories;

    public TaskCategoryPresenter(TaskCategoryContract.View view, TaskCategoryContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
        categories = new ArrayList<>();
    }

    @Override
    public boolean doesCategoriesExist() {
        List<TaskCategory> categories = interactor.getAll();
        if (!categories.isEmpty()) {
            Collections.swap(categories, 4, categories.size() - 1);
            this.categories = categories;
            view.notifyCategoryDataChanged();

            return true;
        }
        return false;
    }

    public void bindAdapterViewToData(TaskCategoryContract.AdapterView adapterView, int position) {
        TaskCategory taskCategory = categories.get(position);
        adapterView.setItemNav(taskCategory.getName(), taskCategory.getImage(), taskCategory.getImageColor(), taskCategory.getTaskCount());
    }

    public void addNavDrawerCategory(TaskCategory taskCategory, int position) {
        categories.add(position, taskCategory);

        interactor.add(taskCategory);

        view.notifyItemCategoryAdded(position);
    }

    @Override
    public void initDefaultCategories(List<TaskCategory> categories) {
        this.categories = categories;
        interactor.initDefaultCategory(categories);

        view.notifyCategoryDataChanged();
    }

    public int getCategoryItemCount() {
        return categories.size();
    }

    public void onItemCategoryClicked(int adapterPosition) {
        TaskCategory taskCategory = categories.get(adapterPosition);

        String[] array = new String[]{String.valueOf(taskCategory.getId()), taskCategory.getName()};

        if (taskCategory.getName().equals("Add taskCategory")) {
            view.createNewItemCategory(adapterPosition);
        } else {
            view.deliverCategory(array);
        }
    }
}
