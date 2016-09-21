package com.sample.foo.storelocaldata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sample.foo.storelocaldata.databinding.ActivitySqliteDatabaseBinding;

public class SqliteDatabaseActivity extends AppCompatActivity {

    private ActivitySqliteDatabaseBinding activityBinding;
    private String TAG = "SqliteDatabaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_sqlite_database);

        activityBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });

        activityBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
            }
        });

        activityBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void saveToDB() {
        SQLiteDatabase database = new SampleSQLiteDBHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_NAME, activityBinding.nameEditText.getText().toString());
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_AGE, activityBinding.ageEditText.getText().toString());
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_GENDER, activityBinding.genderEditText.getText().toString());
        long newRowId = database.insert(SampleSQLiteDBHelper.PERSON_TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    private void readFromDB() {
        String name = activityBinding.nameEditText.getText().toString();
        String gender = activityBinding.genderEditText.getText().toString();
        String age = activityBinding.ageEditText.getText().toString();
        if(age.isEmpty())
            age = "0";

        SQLiteDatabase database = new SampleSQLiteDBHelper(this).getReadableDatabase();

        String[] projection = {
                SampleSQLiteDBHelper.PERSON_COLUMN_ID,
                SampleSQLiteDBHelper.PERSON_COLUMN_NAME,
                SampleSQLiteDBHelper.PERSON_COLUMN_AGE,
                SampleSQLiteDBHelper.PERSON_COLUMN_GENDER
        };

        String selection =
                SampleSQLiteDBHelper.PERSON_COLUMN_NAME + " like ? and " +
                        SampleSQLiteDBHelper.PERSON_COLUMN_AGE + " > ? and " +
                        SampleSQLiteDBHelper.PERSON_COLUMN_GENDER + " like ?";

        String[] selectionArgs = {"%" + name + "%", age, "%" + gender + "%"};

        Cursor cursor = database.query(
                SampleSQLiteDBHelper.PERSON_TABLE_NAME,   // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        Log.d(TAG, "The total cursor count is " + cursor.getCount());
        activityBinding.recycleView.setAdapter(new MyRecyclerViewCursorAdapter(this, cursor));
    }
}
