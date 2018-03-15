package com.peclab.nurgissa.thunderlist;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {
    public static final String EXTRA_VALUE = "value";
    public static final String EXTRA_NOTE = "note";
    private static final String SCHEDULE = "schedule";
    private static final String EXTRA_TITLE = "editValue";
    private String dateTime;
    private TaskDetailContract.Presenter presenter;
    private TaskDetailRecyclerViewAdapter adapter;
    private Listener listener;

    interface Listener {
        void onEditTextNoteItemClick(Bundle state);
    }

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_task, container, false);

        setToolbar(view);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_task_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        presenter = new TaskDetailPresenter(this, new TaskDetailInteractor());
        adapter = new TaskDetailRecyclerViewAdapter(this, presenter);

        initializeRecyclerView();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_detail);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    private void initializeRecyclerView() {
        if (getArguments() != null) {
            String value = getArguments().getString(EXTRA_VALUE);

            if (value != null) {
                presenter.initializeTaskDetail(value);
                getArguments().remove(EXTRA_VALUE);
            } else {
                setEditTextNoteValue();
            }
        } else {
            presenter.initializeTaskDetail(null);
        }
    }

    private void setEditTextNoteValue() {
        String schedule = getArguments().getString(SCHEDULE);
        String title = getArguments().getString(EXTRA_TITLE);
        String note = getArguments().getString(EXTRA_NOTE);

        Task task = new Task();
        task.setNote(note);
        task.setTitle(title);
        task.setSchedule(schedule);

        presenter.setNoteValue(task);

        if (schedule != null)
            getArguments().remove(SCHEDULE);

        if (title != null)
            getArguments().remove(EXTRA_TITLE);

        if (note != null)
            getArguments().remove(EXTRA_NOTE);
    }

    public void onNoteItemClick() {
        listener.onEditTextNoteItemClick(getState());
    }

    public Bundle getState() {
        Bundle bundle = new Bundle();

        TaskDetail tdSchedule = presenter.getTaskDetails().get(TaskDetail.VIEW_TYPE_SCHEDULE);
        TaskDetail tdValue = presenter.getTaskDetails().get(TaskDetail.VIEW_TYPE_EDIT_VALUE);

        if (!tdSchedule.isHint()) {
            bundle.putString(SCHEDULE, tdSchedule.getText());
        }

        if (!tdValue.isHint()) {
            bundle.putString(EXTRA_TITLE, tdValue.getText());
        }
        return bundle;
    }

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

    public void onScheduleItemClick(View view) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "." + month + "." + year;
                dateTime = date;
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
                dateTime = dateTime + " at " + time;
                presenter.setScheduleValue(dateTime);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    @Override
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifySubtaskAddedToDetailTask() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getColorLightOrange() {
        return ContextCompat.getColor(getContext(), R.color.orange);
    }

    @Override
    public int getColorLightBlue() {
        return ContextCompat.getColor(getContext(), R.color.light_blue);
    }

    @Override
    public int getColorLightGray() {
        return ContextCompat.getColor(getContext(), R.color.light_gray);
    }

    @Override
    public int getColorDarkGray() {
        return ContextCompat.getColor(getContext(), R.color.dark_gray);
    }
}
