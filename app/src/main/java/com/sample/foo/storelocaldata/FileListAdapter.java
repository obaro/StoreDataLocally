package com.sample.foo.storelocaldata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.foo.storelocaldata.databinding.FileListItemBinding;

import java.io.File;
import java.util.List;

/**
 * Created by Obaro on 17/09/2016.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {

    private List<File> mFileList;

    public FileListAdapter(List<File> files) {
        mFileList = files;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_list_item, parent, false);
        FileViewHolder viewHolder = new FileViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        FileListItemBinding itemBinding = holder.getItemBinding();
        itemBinding.fileFullPathTextView.setText(mFileList.get(position).getAbsolutePath());
        itemBinding.fileNameTextView.setText(mFileList.get(position).getName());
        itemBinding.fileTypeTextView.setText(
                mFileList.get(position).isDirectory() ? "Is Directory" : "Is File");
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {

        private FileListItemBinding itemBinding;

        public FileViewHolder(View itemView) {
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);
        }

        public FileListItemBinding getItemBinding() {
            return itemBinding;
        }
    }
}
