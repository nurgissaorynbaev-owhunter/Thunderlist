package com.peclab.nurgissa.thunderlist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskListInteractor implements TaskListContract.Interactor {
    private static final String TABLE_NAME_TASK = "Task";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String CATEGORY_COLUMN_TASK_COUNT = "task_count";
    private static final String TABLE_NAME_CATEGORY = "TaskCategory";
    private static final String COLUMN_IS_COMPLETED = "is_completed";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final int COMPLETED_CATEGORY_ID = 4;

    private DatabaseHelper databaseHelper;

    public TaskListInteractor(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public Task create(Task task, OnFinishedListener listener) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, task.getTitle());
        contentValues.put(COLUMN_CATEGORY_ID, task.getCategoryId());
        contentValues.put(COLUMN_IS_COMPLETED, task.getCompletedFlag());

        db.insert(TABLE_NAME_TASK, null, contentValues);

        db.close();

        updateCategoryTableCount(task.getCategoryId());

        return task;
    }

    private void updateCategoryTableCount(int categoryId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        db.execSQL("Update " + TABLE_NAME_CATEGORY + " Set " + CATEGORY_COLUMN_TASK_COUNT + "=" + CATEGORY_COLUMN_TASK_COUNT + "+1" + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(categoryId)});

        db.close();
    }

    @Override
    public Task get(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Task task = null;

        Cursor cursor = db.query(TABLE_NAME_TASK, new String[]{"_id, title"}, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            task.setId(cursor.getInt(0));
            task.setTitle(cursor.getString(1));
        }

        cursor.close();
        db.close();

        return task;
    }

    @Override
    public List<Task> getAllByCategoryId(int categoryId, OnFinishedListener onFinishedListener) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Task> tasks = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * From Task Where category_id=? and is_completed=?", new String[]{String.valueOf(categoryId), String.valueOf(0)});

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                int taskId = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);

                task.setId(taskId);
                task.setTitle(title);

                tasks.add(task);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return tasks;
    }

    @Override
    public void moveTaskToCompleteCategory(Task task) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

//        db.execSQL("Update " + TABLE_NAME_TASK + " Set " + COLUMN_CATEGORY_ID + "=" + COMPLETED_CATEGORY_ID + " Where " + COLUMN_ID + "=?", new String[] {String.valueOf(task.getId())});
        db.execSQL("Update " + TABLE_NAME_TASK + " Set " + COLUMN_IS_COMPLETED + "=" + task.getCompletedFlag() + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(task.getId())});

        db.execSQL("Update " + TABLE_NAME_CATEGORY + " Set " + CATEGORY_COLUMN_TASK_COUNT + "=" + CATEGORY_COLUMN_TASK_COUNT + "+1" + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(task.getCategoryId())});
        db.execSQL("Update " + TABLE_NAME_CATEGORY + " Set " + CATEGORY_COLUMN_TASK_COUNT + "=" + CATEGORY_COLUMN_TASK_COUNT + "-1" + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(4)});

        db.close();
    }

    @Override
    public void moveTaskToPreviousCategory(Task task) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL("Update " + TABLE_NAME_TASK + " Set " + COLUMN_IS_COMPLETED + "=" + task.getCompletedFlag() + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(task.getId())});

        db.execSQL("Update " + TABLE_NAME_CATEGORY + " Set " + CATEGORY_COLUMN_TASK_COUNT + "=" + CATEGORY_COLUMN_TASK_COUNT + "-1" + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(task.getCategoryId())});
        db.execSQL("Update " + TABLE_NAME_CATEGORY + " Set " + CATEGORY_COLUMN_TASK_COUNT + "=" + CATEGORY_COLUMN_TASK_COUNT + "+1" + " Where " + COLUMN_ID + "=?", new String[]{String.valueOf(4)});


        db.close();
    }

    @Override
    public List<Task> getAllCompleted() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * From " + TABLE_NAME_TASK + " Where " + COLUMN_IS_COMPLETED + "=?", new String[]{String.valueOf(1)});

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                String note = cursor.getString(4);
                String created_time = cursor.getString(5);
                int is_completed = cursor.getInt(6);
                int category_id = cursor.getInt(7);

                task.setId(id);
                task.setTitle(title);
                task.setReminderDate(date);
                task.setReminderTime(time);
                task.setNote(note);
                task.setCreatedTime(created_time);
                task.setCompletedFlag(is_completed);
                task.setCategoryId(category_id);

                tasks.add(task);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return tasks;
    }

    @Override
    public void updateCategoryName(String[] category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY_NAME, category[1]);

        db.update(TABLE_NAME_CATEGORY, contentValues, COLUMN_ID + "=?", new String[] {category[0]});

        db.close();
    }

    @Override
    public void deleteCategory(String[] category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY_NAME, category[1]);

        db.delete(TABLE_NAME_CATEGORY, COLUMN_ID + "=?", new String[] {category[0]});

        db.close();
    }
}
