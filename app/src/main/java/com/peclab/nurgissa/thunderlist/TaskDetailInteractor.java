package com.peclab.nurgissa.thunderlist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailInteractor implements TaskDetailContract.Interactor {
    private static final String TABLE_NAME_TASK = "Task";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_REMINDER_DATE = "reminder_date";
    private static final String COLUMN_REMINDER_TIME = "reminder_time";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_CREATED_TIME = "created_time";
    private static final String COLUMN_COMPLETED_FLAG = "is_completed";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String CATEGORY_COLUMN_TASK_COUNT = "task_count";
    private static final String TABLE_NAME_CATEGORY = "TaskCategory";
    private static final int COLUMN_ADD_CATEGORY = 5;

    private DatabaseHelper databaseHelper;

    public TaskDetailInteractor(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void create(OnFinishedListener onFinishedListener, Task task) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, task.getTitle());
        contentValues.put(COLUMN_REMINDER_DATE, task.getReminderDate());
        contentValues.put(COLUMN_REMINDER_TIME, task.getReminderTime());
        contentValues.put(COLUMN_NOTE, task.getNote());
        contentValues.put(COLUMN_CATEGORY_ID, task.getCategoryId());
        contentValues.put(COLUMN_CREATED_TIME, task.getCreatedTime());
        contentValues.put(COLUMN_COMPLETED_FLAG, task.getCompletedFlag());

        db.insert(TABLE_NAME_TASK, null, contentValues);
        db.close();

        updateCategoryTaskCount(task.getCategoryId());

        onFinishedListener.onSaveFinished();
    }

    private void updateCategoryTaskCount(int categoryId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL("Update " + TABLE_NAME_CATEGORY + " Set " + CATEGORY_COLUMN_TASK_COUNT + "=" + CATEGORY_COLUMN_TASK_COUNT + "+1" + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(categoryId)});

        db.close();
    }

    @Override
    public Task getByTitle(String title) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Task task = new Task();

        Cursor cursor = db.query(TABLE_NAME_TASK, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_REMINDER_DATE, COLUMN_REMINDER_TIME, COLUMN_NOTE, COLUMN_CREATED_TIME, COLUMN_COMPLETED_FLAG, COLUMN_CATEGORY_ID}, COLUMN_TITLE + "=?", new String[]{title}, null, null, null);

        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(0));
            String t = cursor.getString(1);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            String note = cursor.getString(4);
            String created_time = cursor.getString(5);
            int completedFlag = cursor.getInt(6);
            int category_id = cursor.getInt(7);

            task.setId(id);
            task.setTitle(t);
            task.setReminderDate(date);
            task.setReminderTime(time);
            task.setNote(note);
            task.setCreatedTime(created_time);
            task.setCompletedFlag(completedFlag);
            task.setCategoryId(category_id);
        }
        cursor.close();
        db.close();

        return task;
    }

    @Override
    public void update(OnFinishedListener onFinishedListener, Task task) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, task.getTitle());
        contentValues.put(COLUMN_REMINDER_DATE, task.getReminderDate());
        contentValues.put(COLUMN_REMINDER_TIME, task.getReminderTime());
        contentValues.put(COLUMN_NOTE, task.getNote());
        contentValues.put(COLUMN_CREATED_TIME, task.getCreatedTime());
        contentValues.put(COLUMN_CATEGORY_ID, task.getCategoryId());
        contentValues.put(COLUMN_COMPLETED_FLAG, task.getCompletedFlag());

        db.update(TABLE_NAME_TASK, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.close();

        onFinishedListener.onUpdateFinished();
    }

    @Override
    public void delete(OnFinishedListener onFinishedListener, Task task) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_NAME_TASK, COLUMN_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.execSQL("Update " + TABLE_NAME_CATEGORY + " Set " + CATEGORY_COLUMN_TASK_COUNT + "=" + CATEGORY_COLUMN_TASK_COUNT + "-1" + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(task.getCategoryId())});
        db.close();

        onFinishedListener.onDeleteFinished();
    }

    @Override
    public List<TaskCategory> getAllCategory() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<TaskCategory> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * From " + TABLE_NAME_CATEGORY, null);

        if (cursor.moveToFirst()) {
            do {
                TaskCategory category = new TaskCategory();
                int id = cursor.getInt(0);
                String name = cursor.getString(1);

                if (id != COLUMN_ADD_CATEGORY) {
                    category.setId(id);
                    category.setName(name);

                    list.add(category);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }
}
