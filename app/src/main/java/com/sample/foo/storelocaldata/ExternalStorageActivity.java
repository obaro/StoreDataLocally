package com.sample.foo.storelocaldata;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sample.foo.storelocaldata.databinding.ActivityExternalStorageBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ExternalStorageActivity extends AppCompatActivity {
    private ActivityExternalStorageBinding externalStorageBinding;
    private String TAG = "ExternalStorageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_internal_storage);
        externalStorageBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_external_storage);

        externalStorageBinding.fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLayout();
            }
        });

        externalStorageBinding.folderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLayout();
            }
        });

        externalStorageBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        externalStorageBinding.loadButton.setOnClickListener(new View.OnClickListener() {
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
        if(!isExternalStorageWritable()) {
            Toast.makeText(this, "The External storage is NOT writable", Toast.LENGTH_LONG).show();
            return;
        }

        String filename = externalStorageBinding.saveFileNameEditText.getText().toString();
        if (filename.isEmpty()) {
            Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            File targetFolder = getTargetFolder();
            targetFolder.mkdirs();
            File newFile = new File(targetFolder.getAbsoluteFile() + File.separator +
                    filename);

            int checkedButton = externalStorageBinding.fileOrFolderGroup.getCheckedRadioButtonId();
            if (checkedButton == externalStorageBinding.fileButton.getId()) {
                newFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(externalStorageBinding.saveFileEditText.getText().toString().getBytes());
                fos.close();
                Log.e(TAG, "Wrote to " + newFile.getAbsolutePath());
            } else {
                newFile.mkdir();
                Log.e(TAG, "Wrote to " + newFile.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception writing file", e);
            Toast.makeText(this, "Exception writing file", Toast.LENGTH_SHORT).show();
        }
    }

    private void load() {
        if(!isExternalStorageReadable()) {
            Toast.makeText(this, "The External storage is NOT readable", Toast.LENGTH_LONG).show();
            return;
        }

        int checkedButton = externalStorageBinding.fileOrFolderGroup.getCheckedRadioButtonId();
        String filename = externalStorageBinding.saveFileNameEditText.getText().toString();

        if (checkedButton == externalStorageBinding.fileButton.getId()) {
            if (filename.isEmpty()) {
                Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                File targetFolder = getTargetFolder();
                File newFile = new File(targetFolder.getAbsoluteFile() + File.separator +
                        filename);
                Scanner scanner = new Scanner(newFile);
                scanner.useDelimiter("\\Z");
                String content = scanner.next();
                externalStorageBinding.showFileContentEditText.setText(content);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found", e);
            }
        } else {
            File targetFolder = getTargetFolder();
            File directory;
            if (filename.isEmpty()) {
                directory = targetFolder;
            } else {
                directory = new File(targetFolder.getAbsoluteFile() + File.separator +
                        filename);
            }
            File[] files = directory.listFiles();
            externalStorageBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));
            externalStorageBinding.recycleView.setAdapter(
                    new FileListAdapter(Arrays.asList(files)));
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void checkLayout() {
        int checkedButton = externalStorageBinding.fileOrFolderGroup.getCheckedRadioButtonId();
        if (checkedButton == externalStorageBinding.fileButton.getId()) {
            externalStorageBinding.recycleView.setVisibility(View.GONE);

            externalStorageBinding.saveTextView.setVisibility(View.VISIBLE);
            externalStorageBinding.saveFileEditText.setVisibility(View.VISIBLE);
            externalStorageBinding.showFileContentTextView.setVisibility(View.VISIBLE);
            externalStorageBinding.showFileContentEditText.setVisibility(View.VISIBLE);
        } else {
            externalStorageBinding.recycleView.setVisibility(View.VISIBLE);

            externalStorageBinding.saveTextView.setVisibility(View.GONE);
            externalStorageBinding.saveFileEditText.setVisibility(View.GONE);
            externalStorageBinding.showFileContentTextView.setVisibility(View.GONE);
            externalStorageBinding.showFileContentEditText.setVisibility(View.GONE);
        }
    }

    /* Choose either a private folder (getExternalFilesDir())
     * or public folder (getExternalStoragePublicDirectory())
     */
    private File getTargetFolder() {
        int checkedButton = externalStorageBinding.privateOrPublicGroup.getCheckedRadioButtonId();
        if (checkedButton == externalStorageBinding.privateButton.getId())
            return getExternalFilesDir(getDirectoryType());
        else
            return Environment.getExternalStoragePublicDirectory(getDirectoryType());
    }

    /* Get one of DOCUMENTS, PICTURES or MOVIES
     * There are many others, including RINGTONES, ALARMS, MUSIC, etc which we ignore
     */
    private String getDirectoryType() {
        int checkedButton = externalStorageBinding.fileTypeGroup.getCheckedRadioButtonId();
        if (checkedButton == externalStorageBinding.documentButton.getId())
            return Environment.DIRECTORY_DOCUMENTS;
        if (checkedButton == externalStorageBinding.pictureButton.getId())
            return Environment.DIRECTORY_PICTURES;
        return Environment.DIRECTORY_MOVIES;
    }
}
