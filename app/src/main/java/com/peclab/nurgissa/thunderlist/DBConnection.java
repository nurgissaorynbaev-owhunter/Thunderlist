package com.peclab.nurgissa.thunderlist;


import android.content.Context;

public class DBConnection {
    private static final String DATABASE_NAME = "Thunderlist";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper databaseHelper;

    public DBConnection(Context context) {
        databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getConnection() {
        return databaseHelper;
    }
}
