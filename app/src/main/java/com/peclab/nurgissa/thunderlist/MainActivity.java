package com.peclab.nurgissa.thunderlist;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements  TaskListFragment.Listener, TaskCategoryContract.View, TaskDetailFragment.Listener {
    private DrawerLayout drawerLayout;
    private TaskCategoryPresenter presenter;
    private TaskCategoryRecyclerViewAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.nav_recycler_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        initializeDatabase();

        presenter = new TaskCategoryPresenter(this, new TaskCategoryInteractor(databaseHelper));
        adapter = new TaskCategoryRecyclerViewAdapter(this, presenter);

        initializeNavDrawerDefaultCategory();

        refreshDrawerLayout();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        TaskListFragment fragment = new TaskListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void initializeDatabase() {
        databaseHelper = DatabaseHelper.getInstance(this);
    }

    private void initializeNavDrawerDefaultCategory() {
        if (!presenter.doesCategoriesExist()) {

            List<TaskCategory> categories = new ArrayList<>();
            categories.add(new TaskCategory("Inbox", R.drawable.ic_inbox_black_24dp, R.color.light_blue));
            categories.add(new TaskCategory("Groceries", R.drawable.ic_shopping_cart_black_24dp, R.color.light_violet));
            categories.add(new TaskCategory("Work", R.drawable.ic_work_black_24dp, R.color.dark_orange));
            categories.add(new TaskCategory("Completed", R.drawable.ic_done_all_black_24dp, R.color.dark_green));
            categories.add(new TaskCategory("Add category", R.drawable.ic_add_white_24dp, R.color.light_gray));

            presenter.initDefaultCategories(categories);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(String value, String[] category) {
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TaskDetailFragment.EXTRA_VALUE, value);
        bundle.putStringArray(TaskDetailFragment.EXTRA_CATEGORY, category);

        taskDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_fragment_container, taskDetailFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onClickFAB(String[] category) {
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArray(TaskDetailFragment.EXTRA_CATEGORY, category);
        taskDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, taskDetailFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void createNewItemCategory(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText edtInput = new EditText(this);
        final InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

        edtInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(edtInput);
        builder.setTitle(getResources().getString(R.string.add_category));

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = edtInput.getText().toString();
                int image = R.drawable.ic_format_list_bulleted_black_24dp;
                int imageColor = R.color.dark_gray;

                presenter.addNavDrawerCategory(new TaskCategory(categoryName, image, imageColor), position);
                imm.hideSoftInputFromWindow(edtInput.getWindowToken(), 0);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imm.hideSoftInputFromWindow(edtInput.getWindowToken(), 0);
                dialog.cancel();
            }
        });

        builder.show();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void notifyItemCategoryAdded(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyCategoryDataChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMainList(String[] category) {
        TaskListFragment taskListFragment = new TaskListFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArray(TaskListFragment.EXTRA_CATEGORY, category);
        taskListFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, taskListFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void deliverCategory(String[] category) {
        TaskListFragment taskListFragment = new TaskListFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArray(TaskListFragment.EXTRA_CATEGORY, category);

        taskListFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_fragment_container, taskListFragment);
        transaction.addToBackStack(null);

        transaction.commit();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void refreshDrawerLayout() {

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset == 1) {
                    presenter.doesCategoriesExist();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }
}
