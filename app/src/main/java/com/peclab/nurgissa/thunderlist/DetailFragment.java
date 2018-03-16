package com.peclab.nurgissa.thunderlist;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class DetailFragment extends Fragment implements DetailContract.View {
    public static final String EXTRA_VALUE = "value";
    private DetailContract.Presenter presenter;
    private EditText edtTitle;
    private EditText edtNote;
    private TextView txvReminder;
    private CheckBox chbCompleted;
    private Listener contextListener;

    interface Listener {
        void showMainList();
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contextListener = (Listener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        edtTitle = view.findViewById(R.id.edt_detail_title);
        edtNote = view.findViewById(R.id.edt_detail_note);
        txvReminder = view.findViewById(R.id.txv_detail_reminder);
        chbCompleted = view.findViewById(R.id.chb_detail_completed);

        presenter = new DetailPresenter(this, new DetailInteractor());

        initToolbar(view);
        handleEditTextTitle();
        handleEditTextNote();
        handleTextViewReminder();

        return view;
    }

    private void initToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.tlb_detail);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
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
                presenter.saveDetail();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleEditTextTitle() {
        if (getArguments() != null) {
            String title = getArguments().getString(EXTRA_VALUE);
            if (title != null)
                presenter.initDetailFromDatabase(title);
        }

        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.addTextTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void handleEditTextNote() {
        edtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.addTextNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void handleTextViewReminder() {
        txvReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminderDate();
            }
        });
    }

    private void setReminderDate() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "." + month + "." + year;
                presenter.addReminderDate(date);

                setReminderTime();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void setReminderTime() {
        Calendar calendar = Calendar.getInstance();

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                presenter.addReminderTime(time);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    @Override
    public void initDetailTitle(String title) {
        edtTitle.setText(title);
    }

    @Override
    public void initDetailReminder(String reminder) {
        txvReminder.setText(reminder);
    }

    @Override
    public void initDetailNote(String note) {
        edtNote.setText(note);
    }

    @Override
    public void moveToMainList() {
        contextListener.showMainList();
    }
}
