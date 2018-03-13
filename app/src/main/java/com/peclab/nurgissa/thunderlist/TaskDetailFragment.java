package com.peclab.nurgissa.thunderlist;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View, TaskDetailRecyclerViewAdapter.Listener {
    public static final String EXTRA_VALUE = "value";
    private TaskDetailContract.Presenter presenter;
    private TaskDetailRecyclerViewAdapter adapter;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_task, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_detail);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_task_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        presenter = new TaskDetailPresenter(this, new TaskDetailInteractor());

        initializeRecyclerView();

        adapter = new TaskDetailRecyclerViewAdapter(this, presenter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            case R.id.menu_item_accept_action:
                handleAcceptMenuItemClick();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleAcceptMenuItemClick() {
        presenter.saveTaskDetail();

        TaskListFragment listFragment = new TaskListFragment();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, listFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void initializeRecyclerView() {
        String value = null;
        if (getArguments() != null) {
            value = getArguments().getString(EXTRA_VALUE);
            getArguments().remove(EXTRA_VALUE);
        }

        presenter.initializeTaskDetail(value);
    }

    @Override
    public void onNoteItemClick(View view) {
        EditTextFragment fragment = new EditTextFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, fragment, null)
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
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onScheduleItemClick(View view) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "." + month + "." + year;
                setTimePicker();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void setTimePicker() {
        Calendar calendar = Calendar.getInstance();

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    @Override
    public void notifySubtaskAddedToDetailTask() {
        adapter.notifyDataSetChanged();
    }
}
