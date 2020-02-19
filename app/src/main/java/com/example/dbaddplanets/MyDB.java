package com.example.dbaddplanets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class MyDB {

    public final static String TABLE = "Planeta"; // name of table
    public final static String ID = "_id";
    public final static String NAME = "name";
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    /**
     * @param context
     */
    public MyDB(Context context) {
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long createRecords(String id, String name) {
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(NAME, name);
        return database.insert(TABLE, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[]{ID, NAME};
        Cursor mCursor = database.query(true, TABLE, cols, null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public int getCount() {
        int count = (int) DatabaseUtils.queryNumEntries(database, TABLE);
        return count;
    }

    public boolean deletePlanet(String id) {
        return database.delete(TABLE, ID + "=" + id, null) > 0;
    }
}

