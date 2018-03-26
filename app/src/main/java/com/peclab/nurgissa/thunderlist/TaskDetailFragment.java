package com.peclab.nurgissa.thunderlist;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;


public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {
    public static final String EXTRA_VALUE = "value";
    public static final String EXTRA_CATEGORY = "category";
    private TaskDetailContract.Presenter presenter;
    private EditText edtTitle;
    private EditText edtNote;
    private TextView txvCategory;
    private TextView txvReminder;
    private TextView txvCreatedTime;
    private ImageView imvReminder;
    private ImageView imvCategory;
    private Listener contextListener;
    private AppCompatActivity appCompatActivity;

    interface Listener {
        void showTaskList(String[] category);
    }

    public TaskDetailFragment() {
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
        txvCategory = view.findViewById(R.id.txv_detail_category);
        txvReminder = view.findViewById(R.id.txv_detail_reminder);
        imvReminder = view.findViewById(R.id.imv_detail_reminder);
        imvCategory = view.findViewById(R.id.imv_detail_category);
        txvCreatedTime = view.findViewById(R.id.txv_detail_created_time);

        presenter = new TaskDetailPresenter(this, new TaskDetailInteractor(DatabaseHelper.getInstance(getContext())));

        setToolbar(view);
        handleEditTextTitle();
        handleEditTextNote();
        handleTextViewReminder();
        handleTextViewCategory();

        return view;
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.tlb_detail);
        appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void setToolbarTitle(String value) {
        appCompatActivity.getSupportActionBar().setTitle(value);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            case R.id.menu_item_accept_action:
                handleTextViewCreatedTime();
                presenter.saveDetail();
                return true;

            case R.id.menu_item_share_action:
                shareTask();
                return true;

            case R.id.menu_item_delete_action:
                presenter.deleteTask();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleEditTextTitle() {
        if (getArguments() != null) {
            String title = getArguments().getString(EXTRA_VALUE);
            String[] category = getArguments().getStringArray(EXTRA_CATEGORY);
            if (category != null) {
                presenter.setCategory(category);
            }
            if (title != null) {
                presenter.initDetailFromDatabase(title);
                getArguments().remove(EXTRA_VALUE);
            }
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

    private void handleTextViewCategory() {
        txvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] categoryName = presenter.getAllCategoryName();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Category");
                builder.setSingleChoiceItems(categoryName, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedCategoryName = categoryName[which];
                        presenter.setSelectedCategory(selectedCategoryName);
                        setToolbarTitle(selectedCategoryName);
                        txvCategory.setText(selectedCategoryName);
                        imvCategory.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_green));
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void setReminderDate() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "." + month + "." + year;

                presenter.addReminderDate(date);

                setReminderTime(year, month, dayOfMonth);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void setReminderTime(final int year, final int month, final int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    presenter.setLocalDateTime(LocalDateTime.of(year, month + 1, dayOfMonth, hour, minute));
                }

                presenter.addReminderTime(time);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    private void handleTextViewCreatedTime() {
        String dateTime = new SimpleDateFormat("EEEE, dd MMM @ HH:mm").format(Calendar.getInstance().getTime());
        presenter.setCreatedTime(dateTime);
        txvCreatedTime.setText(dateTime);
    }

    @Override
    public void initDetailTitle(String title) {
        edtTitle.setText(title);
    }

    @Override
    public void initDetailReminder(String reminder) {
        txvReminder.setText(reminder);
        imvReminder.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_blue));
    }

    @Override
    public void initDetailNote(String note) {
        edtNote.setText(note);
    }

    @Override
    public void initDetailCreatedTime(String value) {
        txvCreatedTime.setText(value);
    }

    @Override
    public void moveToMainList(String[] category) {
        contextListener.showTaskList(category);
    }

    private void shareTask() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, presenter.getTask().getTitle());
        intent.setType("text/plain");

        startActivity(intent);
    }

    @Override
    public void setAlarmNotification(LocalDateTime localDateTime, String title) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        long milli = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            TimeZone tz = TimeZone.getDefault();
            ZonedDateTime zdt = localDateTime.atZone(ZoneId.of(tz.getID()));
            milli = zdt.toInstant().toEpochMilli();
        }

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("EXTRA_TITLE", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, milli, pendingIntent);
        }
    }
}
