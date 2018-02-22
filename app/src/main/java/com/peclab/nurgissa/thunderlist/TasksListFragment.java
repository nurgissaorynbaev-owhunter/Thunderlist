package com.peclab.nurgissa.thunderlist;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class TasksListFragment extends Fragment implements TasksListContract.View {
    private TasksRecyclerViewAdapter adapter;

    private Context context;
    private EditText etQuickTask;
    private TasksListPresenter presenter;


    public TasksListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = inflater.getContext();
        presenter = new TasksListPresenter(this);

        final View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);

        RecyclerView rvTask = view.findViewById(R.id.recycler_view_task);
        etQuickTask = view.findViewById(R.id.edit_text_quick_task);

        handleQuickTaskEditText();

        adapter = new TasksRecyclerViewAdapter(presenter, new TasksRecyclerViewAdapter.TasksClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("Item clicked.");
            }

            @Override
            public void onLongItemClick(View view, int position) {
                System.out.println("Long Item clicked.");
            }
        });

        rvTask.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvTask.setLayoutManager(layoutManager);


        return view;
    }

    private void handleQuickTaskEditText() {
        etQuickTask.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    presenter.setQuickTask(v.getText().toString());

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    etQuickTask.getText().clear();

                    return true;
                }
                return false;
            }
        });
    }
}
