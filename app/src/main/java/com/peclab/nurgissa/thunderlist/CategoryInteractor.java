package com.peclab.nurgissa.thunderlist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CategoryInteractor implements NavDrawerContract.Interactor {
    private static final String TABLE_NAME = "Category";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COUNT = "task_count";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_IMAGE_COLOR = "image_color";

    private DatabaseHelper databaseHelper;

    public CategoryInteractor(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void initDefaultCategory(List<Category> categories) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for (Category c : categories) {
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
    public void add(Category category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, category.getName());
        contentValues.put(COLUMN_IMAGE, category.getImage());
        contentValues.put(COLUMN_IMAGE_COLOR, category.getImageColor());
        contentValues.put(COLUMN_COUNT, category.getTaskCount());

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    @Override
    public List<Category> getAll() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Category> categories = new ArrayList<>();

        Cursor cursor = db.rawQuery("Select * From " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {

            do {
                Category category = new Category();

                category.setId(Integer.parseInt(cursor.getString(0)));
                category.setName(cursor.getString(1));
                category.setImage(Integer.parseInt(cursor.getString(2)));
                category.setImageColor(Integer.parseInt(cursor.getString(3)));
                category.setTaskCount(Integer.parseInt(cursor.getString(4)));

                categories.add(category);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
    }
}
