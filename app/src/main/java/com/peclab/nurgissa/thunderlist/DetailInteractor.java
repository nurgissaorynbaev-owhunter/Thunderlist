package com.peclab.nurgissa.thunderlist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DetailInteractor implements DetailContract.Interactor {
    private static final String TABLE_NAME = "Task";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_REMINDER_DATE = "reminder_date";
    private static final String COLUMN_REMINDER_TIME = "reminder_time";
    private static final String COLUMN_NOTE = "note";


    @Override
    public void save(DetailContract.Interactor.OnFinishedListener onFinishedListener, Task task) {
        SQLiteDatabase db = DBConnection.getConnection().getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, task.getTitle());
        contentValues.put(COLUMN_REMINDER_DATE, task.getReminderDate());
        contentValues.put(COLUMN_REMINDER_TIME, task.getReminderTime());
        contentValues.put(COLUMN_NOTE, task.getNote());

        db.insert(TABLE_NAME, null, contentValues);

        onFinishedListener.onSaveFinished();

        db.close();
    }

    @Override
    public Task getByTitle(String title) {
        SQLiteDatabase db = DBConnection.getConnection().getReadableDatabase();
        Task task = new Task();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_REMINDER_DATE, COLUMN_REMINDER_TIME, COLUMN_NOTE}, COLUMN_TITLE + "=?", new String[]{title}, null, null, null);

        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(0));
            String t = cursor.getString(1);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            String note = cursor.getString(4);

            task.setId(id);
            task.setTitle(t);
            task.setReminderDate(date);
            task.setReminderTime(time);
            task.setNote(note);
        }
        cursor.close();
        db.close();

        return task;
    }

    @Override
    public void update(OnFinishedListener onFinishedListener, Task task) {
        SQLiteDatabase db = DBConnection.getConnection().getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, task.getTitle());
        contentValues.put(COLUMN_REMINDER_DATE, task.getReminderDate());
        contentValues.put(COLUMN_REMINDER_TIME, task.getReminderTime());
        contentValues.put(COLUMN_NOTE, task.getNote());

        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[] {String.valueOf(task.getId())});

        onFinishedListener.onUpdateFinished();

        db.close();
    }
}
