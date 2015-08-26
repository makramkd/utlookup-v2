package org.lag.utlookup.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 8/26/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create the tables
        for (int i = 0; i < DatabaseContract.CREATE_TABLE_ARRAY.length; ++i) {
            sqLiteDatabase.execSQL(DatabaseContract.CREATE_TABLE_ARRAY[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // drop the tables
        for (int i = 0; i < DatabaseContract.DROP_TABLE_ARRAY.length; ++i) {
            sqLiteDatabase.execSQL(DatabaseContract.DROP_TABLE_ARRAY[i]);
        }

        // create again
        onCreate(sqLiteDatabase);
    }
}
