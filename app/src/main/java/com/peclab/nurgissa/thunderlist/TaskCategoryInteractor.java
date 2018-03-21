package com.peclab.nurgissa.thunderlist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskCategoryInteractor implements TaskCategoryContract.Interactor {
    private static final String TABLE_NAME = "TaskCategory";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COUNT = "task_count";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_IMAGE_COLOR = "image_color";

    private DatabaseHelper databaseHelper;

    public TaskCategoryInteractor(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void initDefaultCategory(List<TaskCategory> categories) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for (TaskCategory c : categories) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_NAME, c.getName());
            contentValues.put(COLUMN_IMAGE, c.getImage());
            contentValues.put(COLUMN_IMAGE_COLOR, c.getImageColor());
            contentValues.put(COLUMN_COUNT, c.getTaskCount());

            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    @Override
    public void add(TaskCategory taskCategory) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, taskCategory.getName());
        contentValues.put(COLUMN_IMAGE, taskCategory.getImage());
        contentValues.put(COLUMN_IMAGE_COLOR, taskCategory.getImageColor());
        contentValues.put(COLUMN_COUNT, taskCategory.getTaskCount());

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    @Override
    public List<TaskCategory> getAll() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<TaskCategory> categories = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * From " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {

            do {
                TaskCategory taskCategory = new TaskCategory();

                taskCategory.setId(Integer.parseInt(cursor.getString(0)));
                taskCategory.setName(cursor.getString(1));
                taskCategory.setImage(Integer.parseInt(cursor.getString(2)));
                taskCategory.setImageColor(Integer.parseInt(cursor.getString(3)));
                taskCategory.setTaskCount(Integer.parseInt(cursor.getString(4)));

                categories.add(taskCategory);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
    }
}
