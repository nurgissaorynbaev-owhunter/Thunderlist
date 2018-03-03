package com.peclab.nurgissa.thunderlist;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View, TaskDetailRecyclerViewAdapter.Listener {
    public static final String EXTRA_VALUE = "value";
    private TaskDetailPresenter presenter;
    private TaskDetailRecyclerViewAdapter adapter;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String value = getArguments().getString(EXTRA_VALUE);

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_task_detail, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(inflater.getContext());

        presenter = new TaskDetailPresenter(this);

        initializeRecyclerView(value);

        adapter = new TaskDetailRecyclerViewAdapter(this, presenter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return recyclerView;
    }

    private void initializeRecyclerView(String value) {
        presenter.addDetailTaskItem(R.drawable.task_value, value, DetailTaskItem.VALUE);
        presenter.addDetailTaskItem(R.drawable.task_shedule, getString(R.string.reminder_hint), DetailTaskItem.SCHEDULE);
        presenter.addDetailTaskItem(R.drawable.task_note, getString(R.string.note_hint), DetailTaskItem.NOTE);
        presenter.addDetailTaskItem(R.drawable.add_subtask, getString(R.string.sub_task_hint), DetailTaskItem.ADD_SUBTASK);
    }

    @Override
    public void onNoteItemClick(View view) {
        EditTextFragment fragment = new EditTextFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAddSubtaskItemClick(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getContext());
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle(getResources().getString(R.string.sub_task_hint));
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.getText().toString();
                presenter.addSubtask(value);

                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                dialog.cancel();
            }
        });

        builder.show();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    @Override
    public void notifySubtaskAddedToDetailTask() {
        adapter.notifyDataSetChanged();
    }
}
