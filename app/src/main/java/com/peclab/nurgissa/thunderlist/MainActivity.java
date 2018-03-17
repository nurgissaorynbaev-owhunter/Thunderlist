package com.peclab.nurgissa.thunderlist;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TaskListFragment.Listener, NavDrawerContract.View, DetailFragment.Listener {
    private DrawerLayout drawerLayout;
    private NavDrawerPresenter presenter;
    private NavDrawerRecyclerViewAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.nav_recycler_view);

        initializeDatabase();

        presenter = new NavDrawerPresenter(this, new CategoryInteractor(databaseHelper));
        adapter = new NavDrawerRecyclerViewAdapter(this, presenter);

        initializeNavDrawerDefaultCategory();

        bindDrawerToggleToNavigation();

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

    private void bindDrawerToggleToNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open_drawer, R.string.nav_close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeNavDrawerDefaultCategory() {
        if (!presenter.doesCategoriesExist()) {

            List<Category> categories = new ArrayList<>();
            categories.add(new Category("Inbox", R.drawable.ic_inbox_black_24dp, R.color.light_blue));
            categories.add(new Category("Groceries", R.drawable.ic_shopping_cart_black_24dp, R.color.light_violet));
            categories.add(new Category("Work", R.drawable.ic_work_black_24dp, R.color.dark_orange));
            categories.add(new Category("Completed", R.drawable.ic_done_all_black_24dp, R.color.dark_green));
            categories.add(new Category("Add category", R.drawable.ic_add_white_24dp, R.color.light_gray));

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
    public void onItemClick(String value) {
        DetailFragment detailFragment = new DetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(DetailFragment.EXTRA_VALUE, value);

        detailFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_fragment_container, detailFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onClickFAB() {
        DetailFragment detailFragment = new DetailFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, detailFragment);
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

                presenter.addNavDrawerCategory(new Category(categoryName, image, imageColor), position);
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
    public void showMainList() {
        TaskListFragment listFragment = new TaskListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, listFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
