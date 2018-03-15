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


public class MainActivity extends AppCompatActivity implements TaskListFragment.Listener, TaskDetailFragment.Listener, EditTextFragment.Listener , NavDrawerContract.View {
    private DrawerLayout drawerLayout;
    private NavDrawerPresenter presenter;
    private NavDrawerRecyclerViewAdapter adapter;
    private Bundle state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDatabase();

        presenter = new NavDrawerPresenter(this);

        bindDrawerToggleToNavigation();
        initializeNavDrawerByDefaultContent();

        RecyclerView recyclerView = drawerLayout.findViewById(R.id.nav_recycler_view);
        adapter = new NavDrawerRecyclerViewAdapter(this, presenter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        TaskListFragment fragment = new TaskListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void initializeDatabase() {
        new DBConnection(this);
    }

    private void bindDrawerToggleToNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open_drawer, R.string.nav_close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeNavDrawerByDefaultContent() {
        presenter.addNavDrawerCategory(new TaskCategory("Inbox", R.drawable.ic_inbox_black_24dp, R.color.light_blue));
        presenter.addNavDrawerCategory(new TaskCategory("Groceries", R.drawable.ic_shopping_cart_black_24dp, R.color.light_violet));
        presenter.addNavDrawerCategory(new TaskCategory("Work", R.drawable.ic_work_black_24dp, R.color.dark_orange));
        presenter.addNavDrawerCategory(new TaskCategory("Completed", R.drawable.ic_done_all_black_24dp, R.color.dark_green));
        presenter.addNavDrawerCategory(new TaskCategory("Add category", R.drawable.ic_add_white_24dp, R.color.light_gray));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(String value) {
        TaskDetailFragment detailFragment = new TaskDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TaskDetailFragment.EXTRA_VALUE, value);

        detailFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_fragment_container, detailFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onClickFAB() {
        TaskDetailFragment detailFragment = new TaskDetailFragment();

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
    public void onEditTextNoteItemClick(Bundle state) {
        this.state = state;

        EditTextFragment editTextFragment = new EditTextFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, editTextFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void getText(String value) {
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
        state.putString(TaskDetailFragment.EXTRA_NOTE, value);
        taskDetailFragment.setArguments(state);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.main_fragment_container, taskDetailFragment);

        transaction.commit();
    }
}
