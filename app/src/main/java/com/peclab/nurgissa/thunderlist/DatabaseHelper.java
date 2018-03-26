package com.peclab.nurgissa.thunderlist;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Thunderlist";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
           instance = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return instance;
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table Task (" +
                "_id integer primary key autoincrement," +
                "title text," +
                "reminder_date text, " +
                "reminder_time text, " +
                "note text, " +
                "created_time text, " +
                "is_completed integer, " +
                "category_id integer, constraint fk_tasks foreign key(category_id) references TaskCategory(_id) on delete cascade);");

        db.execSQL("Create table TaskCategory (" +
                "_id integer primary key autoincrement, " +
                "name text, " +
                "image int, " +
                "image_color int, " +
                "task_count integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + "Task");
        db.execSQL("Drop table if exists " + "TaskCategory");
        onCreate(db);
    }
}
