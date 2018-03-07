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

public class TaskListFragment extends Fragment implements TaskListContract.View {
    private EditText edtQuickTask;
    private TaskListPresenter presenter;
    private TaskListRecyclerViewAdapter adapter;
    private Listener contextListener;

    interface Listener {
        void onItemClick(String value);
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = new TaskListPresenter(this);

        final View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_task_list);
        edtQuickTask = view.findViewById(R.id.edit_text_quick_task);

        adapter = new TaskListRecyclerViewAdapter(presenter);

        handleQuickTaskEditText();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void notifyDataAddedToTaskList(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyDataRemovedFromTaskList(int position) {
        adapter.notifyItemRemoved(position);
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

    @Override
    public void deliverTaskTitle(String value) {
        contextListener.onItemClick(value);
    }
}
