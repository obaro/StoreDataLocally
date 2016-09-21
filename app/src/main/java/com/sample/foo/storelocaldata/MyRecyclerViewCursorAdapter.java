package com.sample.foo.storelocaldata;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.foo.storelocaldata.databinding.PersonListItemBinding;

/**
 * Created by Obaro on 19/09/2016.
 */
public class MyRecyclerViewCursorAdapter extends RecyclerView.Adapter<MyRecyclerViewCursorAdapter.ViewHolder> {

    private MyCursorAdapter mCursorAdapter;

    Context mContext;

    public MyRecyclerViewCursorAdapter(Context context, Cursor cursor) {

        mContext = context;
        mCursorAdapter = new MyCursorAdapter(mContext, cursor, 0);
    }

    public static class MyCursorAdapter extends CursorAdapter {

        public MyCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(
                    R.layout.person_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
        }

        public void bindViewHolder(ViewHolder viewHolder, Context context, Cursor cursor) {
            viewHolder.bindCursor(cursor);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PersonListItemBinding personBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            personBinding = DataBindingUtil.bind(itemView);
        }

        public void bindCursor(Cursor cursor) {
            personBinding.nameLabel.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_NAME)
            ));
            personBinding.ageLabel.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_AGE)
            ));
            personBinding.genderLabel.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_GENDER)
            ));
        }
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindViewHolder(holder, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
}