package com.sample.foo.storelocaldata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.foo.storelocaldata.databinding.SharedPrefListItemBinding;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Obaro on 17/09/2016.
 */
public class SharedPrefListAdapter extends RecyclerView.Adapter<SharedPrefListAdapter.SharedPrefViewHolder> {

    private Map<String, String> mPreferences;
    private ArrayList<String> keyList;

    public class SharedPrefViewHolder extends RecyclerView.ViewHolder {
        private SharedPrefListItemBinding listItemBinding;

        public SharedPrefViewHolder(View itemView) {
            super(itemView);
            listItemBinding = DataBindingUtil.bind(itemView);
        }

        public SharedPrefListItemBinding getListItemBinding() {
            return listItemBinding;
        }
    }

    public SharedPrefListAdapter(Map<String, String> preferences){
        mPreferences = preferences;
        keyList = new ArrayList<>(mPreferences.keySet());
    }

    @Override
    public SharedPrefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shared_pref_list_item, parent, false);
        SharedPrefViewHolder viewHolder = new SharedPrefViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SharedPrefViewHolder holder, int position) {
        SharedPrefListItemBinding listItemBinding = holder.getListItemBinding();
        listItemBinding.keyTextView.setText(keyList.get(position));
        listItemBinding.valueTextView.setText(mPreferences.get(keyList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mPreferences.size();
    }
}
