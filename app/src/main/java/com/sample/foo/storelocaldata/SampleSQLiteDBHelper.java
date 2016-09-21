package com.sample.foo.storelocaldata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Obaro on 19/09/2016.
 */
public class SampleSQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "sample_database";
    public static final String PERSON_TABLE_NAME = "person";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_NAME = "name";
    public static final String PERSON_COLUMN_AGE = "age";
    public static final String PERSON_COLUMN_GENDER = "gender";

    public SampleSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + PERSON_TABLE_NAME + " (" +
                PERSON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PERSON_COLUMN_NAME + " TEXT, " +
                PERSON_COLUMN_AGE + " INT UNSIGNED, " +
                PERSON_COLUMN_GENDER + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
