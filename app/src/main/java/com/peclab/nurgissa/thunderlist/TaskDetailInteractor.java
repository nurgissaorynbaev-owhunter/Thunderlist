package com.peclab.nurgissa.thunderlist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskDetailInteractor implements TaskDetailContract.Interactor{
    private static final String TABLE_NAME = "Task";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_ID = "_id";

    @Override
    public Task create(Task task) {
        return null;
    }

    @Override
    public Task getByTitle(String title) {
        SQLiteDatabase db = DBConnection.getConnection().getReadableDatabase();
        Task task = new Task();

        Cursor cursor = db.query(TABLE_NAME, new String[] {COLUMN_ID, COLUMN_TITLE}, "title=?", new String[] {title}, null, null, null);
        if (cursor.moveToFirst()) {
            task.setId(Integer.parseInt(cursor.getString(0)));
            task.setTitle(cursor.getString(1));
        }

        cursor.close();
        db.close();

        return task;
    }
}
