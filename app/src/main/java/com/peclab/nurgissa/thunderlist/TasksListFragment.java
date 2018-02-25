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
    private Context context;
    private EditText etQuickTask;
    private TasksListPresenter presenter;
    private TasksRecyclerViewAdapter adapter;


    public TasksListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = new TasksListPresenter(this);

        final View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);

        RecyclerView rvTask = view.findViewById(R.id.recycler_view_task);
        etQuickTask = view.findViewById(R.id.edit_text_quick_task);

        adapter = new TasksRecyclerViewAdapter(presenter);

        handleQuickTaskEditText();

        rvTask.setAdapter(adapter);
        rvTask.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    @Override
    public void notifyDataAddedToTasksList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyDataRemovedFromTasksList(int position, int itemCount) {
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, itemCount);
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
