package com.example.geomonitor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    static public String DB_NAME = "LOCATION_DB";
    static public int DB_VERSION = 1;
    static public String TABLE_NAME = "LOCATION";
    static public String FIELD_1 = "LATITUDE";
    static public String FIELD_2 = "LONGTITUDE";
    static public String FIELD_3 = "ACTION";
    static public String FIELD_4 = "TIMESTAMP";

    static public String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            + " (" + "'" + FIELD_1 + "' REAL, " + "'" + FIELD_2 + "' REAL, "
            + "'" + FIELD_3 + "' TEXT, " + "'" + FIELD_4 + "' REAL);";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public static void clearDb (SQLiteDatabase db) { db.execSQL("DELETE FROM " + TABLE_NAME + ";"); }

    public Cursor selectAll() {
        return this.getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null, null);
    }
}
