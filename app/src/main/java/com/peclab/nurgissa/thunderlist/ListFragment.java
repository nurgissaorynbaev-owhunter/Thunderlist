package com.peclab.nurgissa.thunderlist;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class ListFragment extends Fragment implements ListContract.View {
    public static final String EXTRA_CATEGORY = "categoryName";
    private EditText edtQuickTask;
    private ListContract.Presenter presenter;
    private ListRecyclerViewAdapter adapter;
    private Listener contextListener;
    private FloatingActionButton fab;
    private AppCompatActivity appCompatActivity;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int categoryId;

    interface Listener {
        void onItemClick(String value, String[] category);

        void onClickFAB(String[] category);
    }

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.contextListener = (Listener) context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = new ListPresenter(this, new ListInteractor(DatabaseHelper.getInstance(getActivity())));
        adapter = new ListRecyclerViewAdapter(presenter);

        final View view = inflater.inflate(R.layout.fragment_list, container, false);

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
        Toolbar toolbar = view.findViewById(R.id.tlb_list);
        appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        drawerLayout = appCompatActivity.findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(appCompatActivity, drawerLayout, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);

        setHasOptionsMenu(true);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);

        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeListByCategory() {
        if (getArguments() != null) {
            String[] category = getArguments().getStringArray(EXTRA_CATEGORY);

            if (category != null) {
                presenter.initializeListByCategory(category);
                categoryId = Integer.parseInt(category[0]);
                getArguments().remove(EXTRA_CATEGORY);
            }
        } else {
            categoryId = 1;
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

                    presenter.addQuickTask(categoryId, v.getText().toString());

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
    public void deliverTaskDetailTitle(String value) {
        System.out.println(presenter.getCategory());
        contextListener.onItemClick(value, presenter.getCategory());
    }
}
