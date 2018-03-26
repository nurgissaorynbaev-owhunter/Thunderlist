package com.peclab.nurgissa.thunderlist;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class TaskListFragment extends Fragment implements TaskListContract.View {
    public static final String EXTRA_CATEGORY = "categoryName";
    private EditText edtQuickTask;
    private TaskListContract.Presenter presenter;
    private TaskListRecyclerViewAdapter adapter;
    private Listener contextListener;
    private FloatingActionButton fab;
    private AppCompatActivity appCompatActivity;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    interface Listener {
        void onItemTaskClicked(String value, String[] category);

        void onClickFAB(String[] category);
    }

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.contextListener = (Listener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);

        presenter = new TaskListPresenter(this, new TaskListInteractor(DatabaseHelper.getInstance(getActivity())));
        adapter = new TaskListRecyclerViewAdapter(presenter);

        setToolbar(view);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_task_list);
        edtQuickTask = view.findViewById(R.id.edit_text_quick_task);
        fab = view.findViewById(R.id.fab_add_task);

        initializeListByCategory();
        handleQuickTaskEditText();
        onClickFAB();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.tlb_task_list);

        appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        drawerLayout = appCompatActivity.findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(appCompatActivity, drawerLayout, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);

        drawerToggle.syncState();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menu_item_edit_category:
                editTaskCategory();
                return true;
            case R.id.menu_item_delete_category:
                deleteTaskCategory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeListByCategory() {
        if (getArguments() != null) {
            String[] category = getArguments().getStringArray(EXTRA_CATEGORY);

            if (category != null) {
                presenter.initializeListByCategory(category);
                getArguments().remove(EXTRA_CATEGORY);
            }
        } else {
            String[] inboxCategory = new String[]{"1", "Inbox"};
            presenter.initializeListByCategory(inboxCategory);
        }
    }

    @Override
    public void setToolbarName(String name) {
        appCompatActivity.getSupportActionBar().setTitle(name);
    }

    @Override
    public void notifyDataAddedToTaskList(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyDataRemovedFromTaskList(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyListDataChanged() {
        adapter.notifyDataSetChanged();
    }

    private void handleQuickTaskEditText() {
        edtQuickTask.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    presenter.addQuickTask(v.getText().toString());

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    edtQuickTask.getText().clear();

                    return true;
                }
                return false;
            }
        });
    }

    private void onClickFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contextListener.onClickFAB(presenter.getCategory());
            }
        });
    }

    @Override
    public void deliverTaskDetailTitle(String value, String[] category) {
        contextListener.onItemTaskClicked(value, category);
    }

    private void editTaskCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText edtInput = new EditText(getContext());
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

        edtInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(edtInput);
        builder.setTitle(getResources().getString(R.string.edit_category));

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = edtInput.getText().toString();
                if (categoryName != null) {
                    presenter.changeTaskCategoryName(categoryName);
                }

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

    private void deleteTaskCategory() {
        presenter.deleteTaskCategory();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem itemEditCategory = menu.findItem(R.id.menu_item_edit_category);
        MenuItem itemDeleteCategory = menu.findItem(R.id.menu_item_delete_category);

        if (checkDefaultTaskCategory()) {
            itemEditCategory.setEnabled(false);
            itemDeleteCategory.setEnabled(false);

        } else {
            itemEditCategory.setEnabled(true);
            itemDeleteCategory.setEnabled(true);
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean checkDefaultTaskCategory() {
        return presenter.checkDefaultTaskCategory();
    }
}
