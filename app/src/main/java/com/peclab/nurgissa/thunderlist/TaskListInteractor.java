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

    private DatabaseHelper databaseHelper;

    public TaskListInteractor(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public Task create(Task task, OnFinishedListener listener) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, task.getTitle());
        db.insert(TABLE_NAME, null, values);

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
}
