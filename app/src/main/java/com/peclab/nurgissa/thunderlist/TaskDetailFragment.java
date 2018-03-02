package com.peclab.nurgissa.thunderlist;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskDetailFragment extends Fragment implements TaskDetailRecyclerViewAdapter.Listener {
    public static final String EXTRA_VALUE = "value";
    private TaskDetailPresenter presenter;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String value = getArguments().getString(EXTRA_VALUE);

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_task_detail, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(inflater.getContext());

        presenter = new TaskDetailPresenter();

        feelRecyclerViewByDefaultData(value);

        TaskDetailRecyclerViewAdapter adapter = new TaskDetailRecyclerViewAdapter(this, presenter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return recyclerView;
    }

    private void feelRecyclerViewByDefaultData(String value) {
        presenter.addDetailTask(R.drawable.note_96, value);
        presenter.addDetailTask(R.drawable.schedule_96, getString(R.string.reminder_hint));
        presenter.addDetailTask(R.drawable.note_96, getString(R.string.note_hint));
        presenter.addDetailTask(R.drawable.plus_96, getString(R.string.subTask_hint));
    }

    @Override
    public void onNoteItemClick(View view) {
        EditTextFragment fragment = new EditTextFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .addToBackStack(null)
                .commit();
    }
}
