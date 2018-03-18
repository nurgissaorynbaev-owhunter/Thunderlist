package com.peclab.nurgissa.thunderlist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskListInteractor implements TaskListContract.Interactor {
    private static final String TABLE_NAME = "Task";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATEGORY_ID = "category_id";

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

        db.insert(TABLE_NAME, null, contentValues);

        db.close();

        return task;
    }

    @Override
    public Task get(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Task task = null;

        Cursor cursor = db.query("Task", new String[]{"_id, title"}, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            task.setId(cursor.getInt(0));
            task.setTitle(cursor.getString(1));
        }

        cursor.close();
        db.close();

        return task;
    }



    @Override
    public List<Task> getAll() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Task> tasks = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                Task task = new Task();

                int id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);

                task.setId(id);
                task.setTitle(title);

                tasks.add(task);

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return tasks;
    }

    @Override
    public List<Task> getAllByCategoryId(int categoryId, OnFinishedListener onFinishedListener) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Task> tasks = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * From Task Where category_id=?", new String[]{String.valueOf(categoryId)});

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

        onFinishedListener.onGetAllByCategoryFinished(tasks);

        cursor.close();
        db.close();

        return tasks;
    }
}
