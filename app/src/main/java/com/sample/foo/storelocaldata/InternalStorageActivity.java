package com.sample.foo.storelocaldata;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sample.foo.storelocaldata.databinding.ActivityInternalStorageBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class InternalStorageActivity extends AppCompatActivity {

    private ActivityInternalStorageBinding internalStorageBinding;
    private String TAG = "InternalStorageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        internalStorageBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_internal_storage);

        internalStorageBinding.fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLayout();
            }
        });

        internalStorageBinding.folderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLayout();
            }
        });

        internalStorageBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        internalStorageBinding.loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLayout();
    }

    private void save() {
        int checkedButton = internalStorageBinding.fileOrFolderGroup.getCheckedRadioButtonId();
        String filename = internalStorageBinding.saveFileNameEditText.getText().toString();
        if (filename.isEmpty()) {
            Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkedButton == internalStorageBinding.fileButton.getId()) {
            try {
                FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(internalStorageBinding.saveFileEditText.getText().toString().getBytes());
                fos.close();
            } catch (Exception e) {
                Log.e(TAG, "Exception writing file", e);
                Toast.makeText(this, "Exception writing file", Toast.LENGTH_SHORT).show();
            }
        } else {
            File directory = getDir(filename, MODE_PRIVATE);
            File[] files = directory.listFiles();
            for (File file: files) {
                Log.d(TAG, "File ==> " + file.getAbsolutePath());
            }
        }
    }

    private void load() {
        int checkedButton = internalStorageBinding.fileOrFolderGroup.getCheckedRadioButtonId();
        String filename = internalStorageBinding.saveFileNameEditText.getText().toString();

        if (checkedButton == internalStorageBinding.fileButton.getId()) {
            if (filename.isEmpty()) {
                Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                FileInputStream fis = openFileInput(filename);
                Scanner scanner = new Scanner(fis);
                scanner.useDelimiter("\\Z");
                String content = scanner.next();
                scanner.close();
                internalStorageBinding.showFileContentEditText.setText(content);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found", e);
            }
        } else {
            File directory;
            if (filename.isEmpty()) {
                directory = getFilesDir();
            }
            else {
                directory = getDir(filename, MODE_PRIVATE);
            }
            File[] files = directory.listFiles();
            internalStorageBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));
            internalStorageBinding.recycleView.setAdapter(
                    new FileListAdapter(Arrays.asList(files)));
            for (File file: files) {
                try {
                    Log.d(TAG, "File ==> " + file.getCanonicalPath());
                } catch (IOException e) {
                    Log.e(TAG, "Exception", e);
                }
            }
        }
    }

    private void checkLayout() {
        int checkedButton = internalStorageBinding.fileOrFolderGroup.getCheckedRadioButtonId();
        if (checkedButton == internalStorageBinding.fileButton.getId()) {
//            internalStorageBinding.saveFilenameEditText.setVisibility(View.GONE);
            internalStorageBinding.recycleView.setVisibility(View.GONE);

            internalStorageBinding.saveTextView.setVisibility(View.VISIBLE);
            internalStorageBinding.saveFileEditText.setVisibility(View.VISIBLE);
            internalStorageBinding.showFileContentTextView.setVisibility(View.VISIBLE);
            internalStorageBinding.showFileContentEditText.setVisibility(View.VISIBLE);
        } else {
//            internalStorageBinding.saveFilenameEditText.setVisibility(View.VISIBLE);
            internalStorageBinding.recycleView.setVisibility(View.VISIBLE);

            internalStorageBinding.saveTextView.setVisibility(View.GONE);
            internalStorageBinding.saveFileEditText.setVisibility(View.GONE);
            internalStorageBinding.showFileContentTextView.setVisibility(View.GONE);
            internalStorageBinding.showFileContentEditText.setVisibility(View.GONE);
        }
    }
}
